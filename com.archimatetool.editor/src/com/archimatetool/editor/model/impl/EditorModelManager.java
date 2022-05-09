/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.ModelChecker;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.IncompatibleModelException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.IModelContentListener;



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
     * Listener
     */
    private IModelContentListener fEContentListener = this::notifyChanged;
    
    /**
     * Models Open
     */
    private List<IArchimateModel> fModels;
    
    /**
     * Backing File
     */
    private File backingFile = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "models.xml"); //$NON-NLS-1$
    
    /**
     * Listen to the App closing so we can ask to save
     */
    private IWorkbenchListener workBenchListener = new IWorkbenchListener() {
        @Override
        public void postShutdown(IWorkbench workbench) {
        }

        @Override
        public boolean preShutdown(IWorkbench  workbench, boolean forced) {
            // Handle modified models
            if(fModels != null) { // Dont call getModels() - we don't want to call loadState();
                for(IArchimateModel model : fModels) {
                    if(isModelDirty(model)) {
                        try {
                            boolean result = askSaveModel(model);
                            if(!result) {
                                return false;
                            }
                        }
                        catch(IOException ex) {
                            ex.printStackTrace();
                            MessageDialog.openError(Display.getCurrent().getActiveShell(),
                                    Messages.EditorModelManager_14,
                                    NLS.bind(Messages.EditorModelManager_15, model.getFile())
                                    + "\n" + ex.getMessage()); //$NON-NLS-1$
                            return false;
                        }
                    }
                }
            }
            
            return true;
        }
    };
    
    public EditorModelManager() {
        if(PlatformUI.isWorkbenchRunning()) {
            PlatformUI.getWorkbench().addWorkbenchListener(workBenchListener);
        }
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
        model.setName(Messages.EditorModelManager_0);
        model.setDefaults();
        
        // Add one default diagram
        IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        diagramModel.setName(Messages.EditorModelManager_1);
        model.getFolder(FolderType.DIAGRAMS).getElements().add(diagramModel);
        
        // Register
        registerModel(model);
        
        return model;
    }
    
    @Override
    public void registerModel(IArchimateModel model) {
        if(model == null) {
            return;
        }
        
        // Add to Models
        if(!getModels().contains(model)) {
            getModels().add(model);
        }
        
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        firePropertyChange(this, PROPERTY_MODEL_CREATED, null, model);
        
        // Register Ecore listener
        model.addModelContentListener(fEContentListener);
    }
    
    @Override
    public IArchimateModel openModel(File file) {
        if(file == null || !file.exists()) {
            return null;
        }
        
        // If it is already loaded return it
        IArchimateModel model = locateLoadedModel(file);
        if(model != null) {
            return model;
        }
        
        model = loadModel(file);
        
        if(model != null) {
            // Open Views of newly opened model if set in Preferences up to a maximum for safety
            if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.OPEN_DIAGRAMS_ON_LOAD)) {
                int max = 0;
                for(IDiagramModel dm : model.getDiagramModels()) {
                    if(max++ < 30) {
                        EditorManager.openDiagramEditor(dm);
                    }
                }
            }
            
            firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
        }
        
        return model;
    }
    
    @Override
    public void openModel(IArchimateModel model) {
        if(model == null || getModels().contains(model)) {
            return;
        }
        
        // Add to Models
        getModels().add(model);
        
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        // Register Ecore listener
        model.addModelContentListener(fEContentListener);

        firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
    }
    
    @Override
    public IArchimateModel loadModel(File file) {
        if(file == null || !file.exists()) {
            return null;
        }
        
        // If it is already loaded return it
        IArchimateModel model = locateLoadedModel(file);
        if(model != null) {
            return model;
        }
        
        // Create the Resource
        Resource resource = IArchiveManager.FACTORY.createResource(file);

        // Check model compatibility
        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);
        
        // Load the model file
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. 
            try {
                modelCompatibility.checkErrors();
            }
            catch(IncompatibleModelException ex1) {
                // Was it a disaster?
                if(PlatformUI.isWorkbenchRunning()) {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.EditorModelManager_2,
                            NLS.bind(Messages.EditorModelManager_3, file)
                            + "\n" + ex1.getMessage()); //$NON-NLS-1$
                    
                }

                return null;
            }
        }
        
        model = (IArchimateModel)resource.getContents().get(0);

        // Once loaded - check for later model version
        if(PlatformUI.isWorkbenchRunning()) {
            boolean isLaterModelVersion = modelCompatibility.isLaterModelVersion(ModelVersion.VERSION);
            if(isLaterModelVersion) {
                boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                        Messages.EditorModelManager_4,
                        NLS.bind(Messages.EditorModelManager_5,
                                file, model.getVersion()));
                if(!answer) {
                    return null;
                }
            }
            // Check for unknown model features which might be OK to load
            else {
                List<Diagnostic> exceptions = modelCompatibility.getAcceptableExceptions();
                if(!exceptions.isEmpty()) {
                    String message = ""; //$NON-NLS-1$
                    for(int i = 0; i < exceptions.size(); i++) {
                        if(i == 3) {
                            message += (exceptions.size() - 3) + " " + Messages.EditorModelManager_12; //$NON-NLS-1$
                            break;
                        }
                        message += exceptions.get(i).getMessage() + "\n"; //$NON-NLS-1$
                    }
                    
                    boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                            Messages.EditorModelManager_4,
                            NLS.bind(Messages.EditorModelManager_13, file)
                            + "\n\n" + message); //$NON-NLS-1$
                    if(!answer) {
                        return null;
                    }
                }
            }
        }

        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }

        // Set file
        model.setFile(file);
        
        // Check defaults
        model.setDefaults();
        
        // Add to list of open models
        getModels().add(model);
        
        // Register Ecore listener
        model.addModelContentListener(fEContentListener);
        
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        // Initiate all diagram models to be marked as "saved" - this is for the editor view persistence
        markDiagramModelsAsSaved(model);

        // This last
        firePropertyChange(this, PROPERTY_MODEL_LOADED, null, model);
        
        return model;
    }
    
    @Override
    public IArchimateModel load(File file) throws IOException {
        if(file == null || !file.exists()) {
            return null;
        }
        
        // Create the Resource
        Resource resource = IArchiveManager.FACTORY.createResource(file);

        // Check model compatibility
        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);
        
        // Load the model file
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. 
            try {
                modelCompatibility.checkErrors();
            }
            catch(IncompatibleModelException ex1) {
                throw new IOException(NLS.bind(Messages.EditorModelManager_10, file) + "\n" + ex1.getMessage()); //$NON-NLS-1$
            }
        }
        
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);

        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }

        // Set file
        model.setFile(file);
        
        // Check defaults
        model.setDefaults();
        
        // New Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);
        
        // New Archive Manager and load images
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        archiveManager.loadImages();
        
        return model;
    }
    
    @Override
    public boolean closeModel(IArchimateModel model) throws IOException {
        return closeModel(model, true);
    }
    
    @Override
    public boolean closeModel(IArchimateModel model, boolean askSaveModel) throws IOException {
        if(model == null) {
            return true;
        }
        
        // Check if model needs saving
        if(PlatformUI.isWorkbenchRunning() && isModelDirty(model) && askSaveModel) {
            boolean result = askSaveModel(model);
            if(!result) {
                return false;
            }
        }
        
        // Close the corresponding GEF editor(s) for this model *FIRST* before removing from model
        EditorManager.closeDiagramEditors(model);
        
        // Remove the model from the local list of open models
        getModels().remove(model);

        // Fire this event *before* disposing of the model in case listeners need to access it or any of its members
        firePropertyChange(this, PROPERTY_MODEL_REMOVED, null, model);

        // Delete the CommandStack *LAST* because GEF Editor(s) will still reference it!
        deleteCommandStack(model);
  
        // Delete Archive Manager
        deleteArchiveManager(model);

        // *at the very last* dispose of this model so its contents can be garbage collected
        // Some Eclipse components such as the Properties View might still reference the model or some of its contents
        model.dispose();
        
        return true;
    }
    
    /**
     * Show dialog to save modified model
     * @param model
     * @return true if the user chose to save the model or chose not to save the model, false if cancelled
     * @throws IOException 
     */
    private boolean askSaveModel(IArchimateModel model) throws IOException {
        MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
                Messages.EditorModelManager_6,
                null,
                NLS.bind(Messages.EditorModelManager_7, model.getName()),
                MessageDialog.QUESTION,
                new String[] {
                    IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL },
                0);
        
        
        int result = dialog.open();
        
        // Yes
        if(result == 0) {
            return saveModel(model);
        }
        // No
        if(result == 1) {
            return true;
        }
        // Cancel
        return false;
    }

    @Override
    public boolean saveModel(IArchimateModel model) throws IOException {
        // Check integrity
        ModelChecker checker = new ModelChecker(model);
        if(!checker.checkAll()) {
            if(PlatformUI.isWorkbenchRunning()) {
                checker.showErrorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            }
            return false;
        }
        
        // First time to save...
        if(model.getFile() == null) {
            File file = askSaveModel();
            if(file == null) { // cancelled
                return false;
            }
            model.setFile(file);
        }
        
        File file = model.getFile();
        
        // Save backup (if set in Preferences)
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.BACKUP_ON_SAVE) && file.exists()) {
            FileUtils.copyFile(file, new File(model.getFile().getAbsolutePath() + ".bak"), false); //$NON-NLS-1$
        }
        
        // Set model version
        model.setVersion(ModelVersion.VERSION);
        
        // Use Archive Manager to save contents
        IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
        archiveManager.saveModel();
        
        // Set CommandStack Save point
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.markSaveLocation();
            // Send notification to Tree
            firePropertyChange(model, COMMAND_STACK_CHANGED, true, false);
        }
        
        // Set all diagram models to be marked as "saved" - this is for the editor view persistence
        markDiagramModelsAsSaved(model);
        
        firePropertyChange(this, PROPERTY_MODEL_SAVED, null, model);
        
        return true;
    }
    
    @Override
    public boolean saveModelAs(IArchimateModel model) throws IOException {
        if(model == null) {
            return false;
        }
        
        File file = askSaveModel();
        if(file == null) {
            return false;
        }
        
        // Set new file
        model.setFile(file);
        
        // And save it to new file
        return saveModel(model);
    }
    
    @Override
    public boolean isModelLoaded(File file) {
        return locateLoadedModel(file) != null;
    }
    
    private IArchimateModel locateLoadedModel(File file) {
        if(file != null) {
            for(IArchimateModel model : getModels()) {
                if(file.equals(model.getFile())) {
                    return model;
                }
            }
        }
        
        return null;
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
     * @return the file or null
     */
    private File askSaveModel() {
        // On Mac if the app is minimised in the dock Display.getCurrent().getActiveShell() will return null
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        shell.setActive(); // Get focus on Mac
        
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFilterExtensions(new String[] { ARCHIMATE_FILE_WILDCARD, "*.*" } ); //$NON-NLS-1$
        
        // Set to true for consistency on all OSs
        dialog.setOverwrite(true);
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(ARCHIMATE_FILE_EXTENSION)) {
            path += ARCHIMATE_FILE_EXTENSION;
        }
        
        File file = new File(path);
        
        // Make sure we don't already have it open
        for(IArchimateModel m : getModels()) {
            if(file.equals(m.getFile())) {
                MessageDialog.openWarning(shell,
                        Messages.EditorModelManager_8,
                        NLS.bind(Messages.EditorModelManager_9, file));
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
        
        if(PlatformUI.isWorkbenchRunning()) {
            // Forward on CommandStack Event to Tree
            cmdStack.addCommandStackEventListener(new CommandStackEventListener() {
                @Override
                public void stackChanged(CommandStackEvent event) {
                    // Send notification to listeners after the change event
                    if(event.isPostChangeEvent()) {
                        firePropertyChange(model, COMMAND_STACK_CHANGED, false, true);
                    }
                }
            });
            
            // Animate Commands
            AnimationUtil.registerCommandStack(cmdStack);
        }
        
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
    
    /**
     * Create a new ArchiveManager for the model
     */
    private IArchiveManager createNewArchiveManager(IArchimateModel model) {
        // dispose any previous one
        deleteArchiveManager(model);
        
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        
        // Load images now
        try {
            archiveManager.loadImages();
        }
        catch(IOException ex) {
            Logger.logError("Could not load images", ex); //$NON-NLS-1$
            ex.printStackTrace();
        }
        
        return archiveManager;
    }
    
    /**
     * Remove the model's ArchiveManager
     */
    private void deleteArchiveManager(IArchimateModel model) {
        IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
        if(archiveManager != null) {
            archiveManager.dispose();
        }
    }

    //========================== Persist backing file  ==========================

    @Override
    public void saveState() throws IOException {
        Document doc = new Document();
        Element rootElement = new Element("models"); //$NON-NLS-1$
        doc.setRootElement(rootElement);
        for(IArchimateModel model : getModels()) {
            File file = model.getFile(); // has been saved
            if(file != null) {
                Element modelElement = new Element("model"); //$NON-NLS-1$
                modelElement.setAttribute("file", file.getAbsolutePath()); //$NON-NLS-1$
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
                for(Object e : rootElement.getChildren("model")) { //$NON-NLS-1$
                    Element modelElement = (Element)e;
                    String filePath = modelElement.getAttributeValue("file"); //$NON-NLS-1$
                    if(filePath != null) {
                        loadModel(new File(filePath));
                    }
                }
            }
        }
    }
    
    //========================== Model Listener events  ==========================

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        fListeners.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        fListeners.removePropertyChangeListener(listener);
    }
    
    @Override
    public void firePropertyChange(Object source, String prop, Object oldValue, Object newValue) {
        fListeners.firePropertyChange(new PropertyChangeEvent(source, prop, oldValue, newValue));
    }
    
    private void notifyChanged(Notification notification) {
        firePropertyChange(this, PROPERTY_ECORE_EVENT, null, notification);
    }
}
