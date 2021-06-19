/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.IncompatibleModelException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.ICloneable;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;


/**
 * Model Importer
 * 
 * @author Phillip Beauvoir
 */
public class ModelImporter {
    
    private boolean update; // If true update target objects with source objects
    private boolean updateAll; // If true update model name, purpose, documentation and top level folders with source
    
    private IArchimateModel importedModel;
    private IArchimateModel targetModel;
    
    // Keep a cache of objects in the target model for speed
    private Map<String, IIdentifier> objectCache;
    
    // Status Messages
    private List<StatusMessage> statusMessages;
    
    // Undo/Redo commands
    private NonNotifyingCompoundCommand compoundCommand;
    
    public ModelImporter() {
    }

    public void doImport(File importedFile, IArchimateModel targetModel) throws IOException, ImportException {
        importedModel = loadModel(importedFile);

        this.targetModel = targetModel;
        
        objectCache = createObjectIDCache();
        
        statusMessages = new ArrayList<>();
        
        compoundCommand = new NonNotifyingCompoundCommand(Messages.ModelImporter_1);
        
        // Upate root model object if the option is set
        if(updateAll) {
            updateObject(importedModel, targetModel);
            addCommand(new EObjectFeatureCommand(null, targetModel, IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE, importedModel.getPurpose()));
            logMessage(StatusMessageLevel.INFO, Messages.ModelImporter_3, targetModel);
        }
        
        // Iterate through all model contents
        for(Iterator<EObject> iter = importedModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            // Update folders
            if(eObject instanceof IFolder) {
                new FolderImporter(this).importFolder((IFolder)eObject);
            }
            // Update concepts
            else if(eObject instanceof IArchimateConcept) {
                new ConceptImporter(this).importConcept((IArchimateConcept)eObject);
            }
            // Update Views
            else if(eObject instanceof IDiagramModel) {
                new ViewImporter(this).importView((IDiagramModel)eObject);
            }
        }
        
        // Check view connection ends are valid if we have done some commands and even if update is off
        if(compoundCommand.canExecute()) {
            addCommand(new SetArchimateReconnectionCommand(targetModel));
        }
        
        // Run Commands
        CommandStack stack = (CommandStack)targetModel.getAdapter(CommandStack.class);
        stack.execute(compoundCommand);
        
        // Resolve Diagram Model References *after* the commands have run
        resolveDiagramModelReferences();
        
        objectCache.clear();
        objectCache = null;
        importedModel = null;
        this.targetModel = null;
    }
    
    protected IArchimateModel getImportedModel() {
        return importedModel;
    }
    
    protected IArchimateModel getTargetModel() {
        return targetModel;
    }
    
    /**
     * If true update/replace target objects with source objects - sub-folders, concepts, folder structure, views
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public boolean shouldUpdate() {
        return update;
    }
    
    /**
     * If true update/replace model and top level folders' name, purpose, documentation and properties with source
     */
    public void setUpdateAll(boolean updateAll) {
        this.updateAll = updateAll;
        if(updateAll) {
            update = true; // If we are updating all then set this too
        }
    }
    
    public boolean shouldUpdateAll() {
        return updateAll;
    }
    
    public List<StatusMessage> getStatusMessages() {
        return statusMessages;
    }
    
    /**
     * Load a model from file
     */
    private IArchimateModel loadModel(File file) throws IOException {
        if(!file.exists()) {
            throw new IOException(NLS.bind(Messages.ModelImporter_2, file));
        }
        
        // Is this is a legacy archive file?
        boolean isLegacyArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(file);
        
        // Create the Resource
        Resource resource = ArchimateResourceFactory.createNewResource(isLegacyArchiveFormat ?
                                                        IArchiveManager.FACTORY.createArchiveModelURI(file) :
                                                        URI.createFileURI(file.getAbsolutePath()));

        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);

        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. Was it a disaster?
            try {
                modelCompatibility.checkErrors();
            }
            // Incompatible
            catch(IncompatibleModelException ex1) {
                throw new IOException(NLS.bind(Messages.ModelImporter_0, file)
                        + "\n" + ex1.getMessage()); //$NON-NLS-1$
            }
        }
        
        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }
        
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        model.setFile(file);
        model.setDefaults();
        
        // Convert from legacy format
        if(isLegacyArchiveFormat) {
            IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
            archiveManager.convertImagesFromLegacyArchive(file);
        }
        
        return model;
    }
    
    /**
     * Create a cache of objects in the target model for speed
     */
    private Map<String, IIdentifier> createObjectIDCache() {
        HashMap<String, IIdentifier> map = new HashMap<String, IIdentifier>();
        
        for(Iterator<EObject> iter = targetModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IIdentifier) {
                map.put(((IIdentifier)eObject).getId(), (IIdentifier)eObject);
            }
        }
        
        return map;
    }
    
    /**
     * Resolve Diagram Model References *after* the import has happened.
     * New and Updated Diagram Model References will be pointing to the DM in the imported model.
     */
    private void resolveDiagramModelReferences() throws ImportException {
        for(Iterator<EObject> iter = targetModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelReference) {
                IDiagramModelReference ref = (IDiagramModelReference)eObject;
                IDiagramModel dm = ref.getReferencedModel(); 
                if(dm.getArchimateModel() == getImportedModel()) { // This could be the dm in the imported model
                    EObject targetDM = ArchimateModelUtils.getObjectByID(targetModel, dm.getId()); // Use its id to find the target dm
                    if(targetDM instanceof IDiagramModel) {
                        ref.setReferencedModel((IDiagramModel)targetDM);
                    }
                    else {
                        throw new ImportException("Could not get referenced diagram model"); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    // ===================================================================================
    // Shared methods
    // ===================================================================================
    
    /**
     * Add a command to the Compound Command for later execution
     */
    void addCommand(Command cmd) {
        if(cmd.canExecute()) {
            compoundCommand.add(cmd);
        }
    }

    /**
     * Find an object in the target model based on the eObject's identifier and class
     */
    @SuppressWarnings("unchecked")
    <T extends IIdentifier> T findObjectInTargetModel(T eObject) throws ImportException {
        EObject foundObject = objectCache.get(eObject.getId());
        
        // Not found
        if(foundObject == null) {
            return null;
        }
        
        // Not the right class, so that's an error we should report
        if(foundObject.eClass() != eObject.eClass()) {
            throw new ImportException("Found object with same id but different class: " + eObject.getId()); //$NON-NLS-1$
        }

        // Found an element with this id and the class is the same
        return (T)foundObject;
    }
    
    /**
     * Create a new object based on class of a given object and set its data to that in the given object
     */
    @SuppressWarnings("unchecked")
    <T extends IArchimateModelObject> T cloneObject(T eObject) {
        IArchimateModelObject newObject;
        
        if(eObject instanceof ICloneable) {
            newObject = (IArchimateModelObject)((ICloneable)eObject).getCopy();
        }
        else {
            newObject = (IArchimateModelObject)EcoreUtil.create(eObject.eClass());
            updateObject(eObject, newObject);
        }
        
        newObject.setId(eObject.getId());
        
        objectCache.put(newObject.getId(), newObject);
        
        return (T)newObject;
    }
    
    /**
     * Update target object with data from source object
     */
    void updateObject(EObject importedObject, EObject targetObject) {
        // Name
        if(importedObject instanceof INameable && targetObject instanceof INameable) {
            addCommand(new EObjectFeatureCommand(null, targetObject, IArchimatePackage.Literals.NAMEABLE__NAME, ((INameable)importedObject).getName()));
        }
        
        // Documentation
        if(importedObject instanceof IDocumentable && targetObject instanceof IDocumentable) {
            addCommand(new EObjectFeatureCommand(null, targetObject, IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, ((IDocumentable)importedObject).getDocumentation()));
        }
        
        // Properties
        if(importedObject instanceof IProperties  && targetObject instanceof IProperties) {
            addCommand(new UpdatePropertiesCommand((IProperties)importedObject, (IProperties)targetObject));
        }

        // Features
        if(importedObject instanceof IFeatures && targetObject instanceof IFeatures) {
            addCommand(new UpdateFeaturesCommand((IFeatures)importedObject, (IFeatures)targetObject));
        }
    }
    
    /**
     * Log a status message
     */
    void logMessage(StatusMessageLevel level, String message, Object... objs) {
        statusMessages.add(new StatusMessage(level, message, objs));
    }
    
    // ====================================================================================================
    // Commands
    // ====================================================================================================

    private static class UpdatePropertiesCommand extends Command {
        private IProperties importedObject;
        private IProperties targetObject;
        private List<IProperty> oldProperties;

        private UpdatePropertiesCommand(IProperties importedObject, IProperties targetObject) {
            this.importedObject = importedObject;
            this.targetObject = targetObject;
            oldProperties = new ArrayList<>(targetObject.getProperties());;
        }

        @Override
        public void execute() {
            targetObject.getProperties().clear();
            targetObject.getProperties().addAll(EcoreUtil.copyAll(importedObject.getProperties()));
        }

        @Override
        public void undo() {
            targetObject.getProperties().clear();
            targetObject.getProperties().addAll(oldProperties);
        }
        
        @Override
        public void dispose() {
            importedObject = null;
            targetObject = null;
            oldProperties = null;
        }
    }
    
    private static class UpdateFeaturesCommand extends Command {
        private IFeatures importedObject;
        private IFeatures targetObject;
        private ArrayList<IFeature> oldFeatures;

        private UpdateFeaturesCommand(IFeatures importedObject, IFeatures targetObject) {
            this.importedObject = importedObject;
            this.targetObject = targetObject;
            oldFeatures = new ArrayList<>(targetObject.getFeatures());
        }

        @Override
        public void execute() {
            targetObject.getFeatures().clear();
            targetObject.getFeatures().addAll(EcoreUtil.copyAll(importedObject.getFeatures()));
        }

        @Override
        public void undo() {
            targetObject.getFeatures().clear();
            targetObject.getFeatures().addAll(oldFeatures);
        }
        
        @Override
        public void dispose() {
            importedObject = null;
            targetObject = null;
            oldFeatures = null;
        }
    }
    
    /**
     * Archimate View Connections might need reconnecting or disconnecting
     * This can only be executed once all previous commands have run so that the target model is in the correct state
     */
    private class SetArchimateReconnectionCommand extends CompoundCommand {
        private IArchimateModel model;
        
        private SetArchimateReconnectionCommand(IArchimateModel model) {
            this.model = model;
            
            // Add an empty Command so this always executes
            add(new Command() {});
        }
        
        @Override
        public void execute() {
            for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                
                if(eObject instanceof IDiagramModelArchimateConnection) {
                    addArchimateReconnectionCommand((IDiagramModelArchimateConnection)eObject);
                }
            }
            
            super.execute();
        }
        
        @Override
        public void dispose() {
            super.dispose();
            model = null;
        }
        
        /**
         * Reconnect Archimate connections in case of relationship ends having changed
         */
        private void addArchimateReconnectionCommand(IDiagramModelArchimateConnection connection) {
            IArchimateRelationship relationship = connection.getArchimateRelationship();

            // Is source object valid?
            if(((IDiagramModelArchimateComponent)connection.getSource()).getArchimateConcept() != relationship.getSource()) {
                // Get the first instance of the new source in this view and connect to that
                List<IDiagramModelArchimateComponent> list = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(connection.getDiagramModel(),
                        relationship.getSource());
                if(!list.isEmpty()) {
                    IDiagramModelArchimateComponent matchingComponent = list.get(0);
                    IConnectable oldSource = connection.getSource();
                    
                    add(new Command() {
                        @Override
                        public void execute() {
                            connection.connect(matchingComponent, connection.getTarget());
                        }
                        
                        @Override
                        public void undo() {
                            connection.connect(oldSource, connection.getTarget());
                        }
                    });
                    
                    logMessage(StatusMessageLevel.WARNING, Messages.ModelImporter_4, connection, connection.getDiagramModel());
                }
                // Not found, so delete the matching connection
                else {
                    add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(connection));
                    logMessage(StatusMessageLevel.WARNING, Messages.ModelImporter_5, connection, connection.getDiagramModel());
                }
            }

            // Is target object valid?
            if(((IDiagramModelArchimateComponent)connection.getTarget()).getArchimateConcept() != relationship.getTarget()) {
                // Get the first instance of the new source in this view and connect to that
                List<IDiagramModelArchimateComponent> list = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(connection.getDiagramModel(), relationship.getTarget());
                if(!list.isEmpty()) {
                    IDiagramModelArchimateComponent matchingComponent = list.get(0);
                    IConnectable oldTarget = connection.getTarget();
                    
                    add(new Command() {
                        @Override
                        public void execute() {
                            connection.connect(connection.getSource(), matchingComponent);
                        }
                        
                        @Override
                        public void undo() {
                            connection.connect(connection.getSource(), oldTarget);
                        }
                    });
                    
                    logMessage(StatusMessageLevel.WARNING, Messages.ModelImporter_6, connection, connection.getDiagramModel());
                }
                // Not found, so delete the matching connection
                else {
                    add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(connection));
                    logMessage(StatusMessageLevel.WARNING, Messages.ModelImporter_5, connection, connection.getDiagramModel());
                }
            }
        }
    }

}
