/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.impl.ModelVersionChecker.IncompatibleModelVersionException;
import uk.ac.bolton.archimate.editor.model.impl.ModelVersionChecker.LaterModelVersionException;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.EditorManager;
import uk.ac.bolton.archimate.editor.utils.FileUtils;
import uk.ac.bolton.archimate.editor.utils.JDOMUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.ModelVersion;
import uk.ac.bolton.archimate.model.util.ArchimateResourceFactory;


/**
 * Editor Model Manager.<p>
 * <p>
 * Acts as an adapter to the Archimate Models passing on notifications to listeners
 * so that clients only have to register here once rather than for each model.<p>
 * Also can pass on arbitrary PropertyChangeEvents to registered listeners.<br>
 * Also manages CommandStacks for models.<br>
 * Also handles persistence of models.
 * 
 * @author Phillip Beauvoir
 */
public class EditorModelManager
implements IEditorModelManager {
    
    /**
     * Listener list
     */
    private PropertyChangeSupport fListeners = new PropertyChangeSupport(this);
    
    /**
     * Models Open
     */
    private List<IArchimateModel> fModels;
    
    /**
     * Backing File
     */
    private File backingFile = new File(ArchimateEditorPlugin.INSTANCE.getUserDataFolder(), "models.xml"); //$NON-NLS-1$
    
    /**
     * Listen to the App closing so we can ask to save
     */
    private IWorkbenchListener workBenchListener = new IWorkbenchListener() {
        public void postShutdown(IWorkbench workbench) {
        }

        public boolean preShutdown(IWorkbench  workbench, boolean forced) {
            // Handle modified models
            for(IArchimateModel model : getModels()) {
                if(isModelDirty(model)) {
                    try {
                        boolean result = askSaveModel(model);
                        if(!result) {
                            return false;
                        }
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            return true;
        }
    };
    
    public EditorModelManager() {
        PlatformUI.getWorkbench().addWorkbenchListener(workBenchListener);
    }
    
    @Override
    public List<IArchimateModel> getModels() {
        if(fModels == null) {
            fModels = new ArrayList<IArchimateModel>();
            
            try {
                loadState();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return fModels;
    }

    @Override
    public IArchimateModel createNewModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("(new model)");
        model.setDefaults();
        
        // Add one default diagram
        IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createDiagramModel();
        diagramModel.setName("Default View");
        model.getFolder(FolderType.DIAGRAMS).getElements().add(diagramModel);
        
        // Add to Models
        getModels().add(model);
        
        // New Command Stack
        createNewCommandStack(model);
        
        firePropertyChange(this, PROPERTY_MODEL_CREATED, null, model);
        model.eAdapters().add(new ECoreAdapter());
        return model;
    }
    
    @Override
    public IArchimateModel openModel(File file) {
        if(file == null || !file.exists() || isModelLoaded(file)) {
            return null;
        }
        
        IArchimateModel  model = loadModel(file);
        if(model != null) {
            // Open Views of newly opened model if set in Preferences
            if(Preferences.doOpenDiagramsOnLoad()) {
                for(IDiagramModel dm : model.getDiagramModels()) {
                    EditorManager.openDiagramEditor(dm);
                }
            }

            firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
        }
        
        return model;
    }
    
    @Override
    public void openModel(IArchimateModel model) {
        // Add to Models
        getModels().add(model);
        
        // New Command Stack
        createNewCommandStack(model);

        model.eAdapters().add(new ECoreAdapter());

        firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
    }
    
    @Override
    public IArchimateModel loadModel(File file) {
        if(file == null || !file.exists()) {
            return null;
        }
        
        Resource resource = ArchimateResourceFactory.createResource(file);
        
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. Was it a disaster?
            try {
                ModelVersionChecker.checkErrors(resource);
            }
            // Incompatible
            catch(IncompatibleModelVersionException exception) {
                Logger.logError("Error opening model", exception);
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error opening model",
                        "Cannot open '" + file +  "'. This version is incompatible. Please update to the latest version of Archi.");
                return null;
            }
            // Wrong file type
            catch(Exception ex2) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error opening model",
                        "Cannot open '" + file +  "'.");
                return null;
            }
        }
        
        // Once loaded - Check version compatibility
        try {
            ModelVersionChecker.checkVersion(resource);
        }
        catch(LaterModelVersionException exception) {
            boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    "Opening model",
                    "'" + file +  "' is a later version model. Are you sure you want to continue opening it?");
            if(!answer) {
                return null;
            }
        }

        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        model.setFile(file);
        model.setDefaults();
        getModels().add(model);
        model.eAdapters().add(new ECoreAdapter());

        // New Command Stack
        createNewCommandStack(model);

        // Initiate all diagram models to be marked as "saved" - this is for the editor view persistence
        markDiagramModelsAsSaved(model);

        // This last
        firePropertyChange(this, PROPERTY_MODEL_LOADED, null, model);

        return model;
    }
    
    @Override
    public boolean closeModel(IArchimateModel model) throws IOException {
        // Check if model needs saving
        if(isModelDirty(model)) {
            boolean result = askSaveModel(model);
            if(!result) {
                return false;
            }
        }
        
        // Close the corresponding GEF editor(s) for this model *FIRST* before removing from model
        EditorManager.closeDiagramEditors(model);
        
        getModels().remove(model);
        model.eAdapters().clear();
        firePropertyChange(this, PROPERTY_MODEL_REMOVED, null, model);
        
        // Delete the CommandStack *LAST* because GEF Editor(s) will still reference it!
        deleteCommandStack(model);
        
        return true;
    }
    
    /**
     * Show dialog to save modified model
     * @param model
     * @return
     * @throws IOException 
     */
    private boolean askSaveModel(IArchimateModel model) throws IOException {
        MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
                "Save Model",
                null,
                "' " + model.getName() + "' has been modified. Save changes?",
                MessageDialog.QUESTION,
                new String[] {
                    IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL },
                0);
        
        
        int result = dialog.open();
        
        // Save
        if(result == 0) {
            return saveModel(model);
        }
        // Cancel
        else if(result == 2) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean saveModel(IArchimateModel model) throws IOException {
        // First time to save...
        if(model.getFile() == null) {
            File file = askSaveModel();
            if(file == null) { // cancelled
                return false;
            }
            model.setFile(file);
        }
        
        // Save backup
        if(model.getFile().exists()) {
            FileUtils.copyFile(model.getFile(), new File(model.getFile().getAbsolutePath() + ".bak"), false);
        }
        
        // Set model version
        model.setVersion(ModelVersion.VERSION);
        
        Resource resource = ArchimateResourceFactory.createResource(model.getFile());
        resource.getContents().add(model);
        resource.save(null);
        resource.getContents().remove(model);
        
        // Set CommandStack Save point
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        stack.markSaveLocation();
        // Send notification to Tree
        firePropertyChange(model, COMMAND_STACK_CHANGED, true, false);
        
        // Set all diagram models to be marked as "saved" - this for the editor view persistence
        markDiagramModelsAsSaved(model);
        
        firePropertyChange(this, PROPERTY_MODEL_SAVED, null, model);
        
        return true;
    }
    
    @Override
    public boolean saveModelAs(IArchimateModel model) throws IOException {
        File file = askSaveModel();
        if(file == null) {
            return false;
        }
        model.setFile(file);
        return saveModel(model);
    }
    
    @Override
    public boolean isModelLoaded(File file) {
        if(file != null) {
            for(IArchimateModel model : getModels()) {
                if(file.equals(model.getFile())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean isModelDirty(IArchimateModel model) {
        if(model == null) {
            return false;
        }
        
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        return stack != null && stack.isDirty();
    }

    /**
     * Ask user for file name to save model
     * @return
     */
    private File askSaveModel() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] { ARCHIMATE_FILE_WILDCARD, "*.xml", "*.*" } );
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(ARCHIMATE_FILE_EXTENSION)) {
            path += ARCHIMATE_FILE_EXTENSION;
        }
        else if(dialog.getFilterIndex() == 1 && !path.endsWith(".xml")) {
            path += ".xml";
        }
        
        File file = new File(path);
        
        // Make sure we don't already have it open
        for(IArchimateModel m : getModels()) {
            if(file.equals(m.getFile())) {
                MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Save Model", "' " + file +
                        "' is already already open. Please choose another file name.");
                return null;
            }
        }
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Save Model", "'" + file +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return null;
            }
        }
        
        return file;
    }
    
    /**
     * Create a new ComandStack for the Model
     * @param model
     */
    private void createNewCommandStack(final IArchimateModel model) {
        CommandStack cmdStack = new CommandStack();
        
        // Forward on CommandStack Event to Tree
        cmdStack.addCommandStackListener(new CommandStackListener() {
            public void commandStackChanged(EventObject event) {
                // Send notification to Tree
                firePropertyChange(model, COMMAND_STACK_CHANGED, false, true);
            }
        });
        
        // Animate Commands
        AnimationUtil.registerCommandStack(cmdStack);
        
        model.setAdapter(CommandStack.class, cmdStack);
    }
    
    /**
     * Remove a CommandStack
     * @param model
     */
    private void deleteCommandStack(IArchimateModel model) {
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.dispose();
        }
    }
    
    /**
     * Set all diagram models in a model to be marked as "saved" - this for the editor view persistence
     */
    private void markDiagramModelsAsSaved(IArchimateModel model) {
        for(IDiagramModel dm : model.getDiagramModels()) {
            dm.setAdapter(ADAPTER_PROPERTY_MODEL_SAVED, true);
        }
    }
    
    //========================== Persist backing file  ==========================

    public void saveState() throws IOException {
        Document doc = new Document();
        Element rootElement = new Element("models");
        doc.setRootElement(rootElement);
        for(IArchimateModel model : getModels()) {
            File file = model.getFile(); // has been saved
            if(file != null) {
                Element modelElement = new Element("model");
                modelElement.setAttribute("file", file.getAbsolutePath());
                rootElement.addContent(modelElement);
            }
        }
        JDOMUtils.write2XMLFile(doc, backingFile);
    }
    
    private void loadState() throws IOException, JDOMException {
        if(backingFile.exists()) {
            Document doc = JDOMUtils.readXMLFile(backingFile);
            if(doc.hasRootElement()) {
                Element rootElement = doc.getRootElement();
                for(Object e : rootElement.getChildren("model")) {
                    Element modelElement = (Element)e;
                    String filePath = modelElement.getAttributeValue("file");
                    if(filePath != null) {
                        loadModel(new File(filePath));
                    }
                }
            }
        }
    }
    
    //========================== Model Listener events  ==========================

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        fListeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        fListeners.removePropertyChangeListener(listener);
    }
    
    public void firePropertyChange(Object source, String prop, Object oldValue, Object newValue) {
        fListeners.firePropertyChange(new PropertyChangeEvent(source, prop, oldValue, newValue));
    }
    
    // ======================= ECore Adapter =========================================
    
    /**
     * Adapter listener class.
     * Forwards on messages so that listeners don't have to adapt to ECore objects
     */
    private class ECoreAdapter extends EContentAdapter {
        @Override
        public void notifyChanged(Notification msg) {
            super.notifyChanged(msg);
            // Forward on to listeners...
            firePropertyChange(this, PROPERTY_ECORE_EVENT, null, msg);
        }
    }
}
