/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
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
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;


/**
 * Model Importer
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class ModelImporter {
    
    private boolean update; // If true update target objects with source objects
    private boolean updateAll; // If true update model name, purpose, documentation and top level folders with source
    private boolean updateFolderStructure = true; // If true folder structure is updated according to the imported model
    
    private IArchimateModel importedModel;
    private IArchimateModel targetModel;
    
    // Object matcher to uniquely identify objects in the target model
    private IObjectMatcher objectMatcher;
    
    // Status Messages
    private List<StatusMessage> statusMessages;
    
    // Undo/Redo commands
    private NonNotifyingCompoundCommand compoundCommand;
    
    // Ecore attributes that should not be imported
    private static Set<EAttribute> IGNORED_EATTRIBUTES = Set.of(
            IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, // Image Path - we will set this
            IArchimatePackage.Literals.ARCHIMATE_MODEL__FILE,                    // File path - definitely not!
            IArchimatePackage.Literals.ARCHIMATE_MODEL__VERSION                  // Model version
    );
    
    public ModelImporter() {
    }
    
    public void doImport(File importedFile, IArchimateModel targetModel) throws IOException, ImportException {
        doImport(loadModel(importedFile), targetModel);
    }
    
    public void doImport(IArchimateModel importedModel, IArchimateModel targetModel) throws IOException, ImportException {
        this.importedModel = importedModel;
        this.targetModel = targetModel;
        
        // Run Commands
        CommandStack stack = (CommandStack)targetModel.getAdapter(CommandStack.class);
        stack.execute(getCommand());
    }
    
    /**
     * Return the Command to do the import. Can be executed on a Command Stack (jArchi)
     */
    public Command getCommand(File importedFile, IArchimateModel targetModel) throws IOException, ImportException {
        return getCommand(loadModel(importedFile), targetModel);
    }
    
    /**
     * Return the Command to do the import. Can be executed on a Command Stack (jArchi)
     */
    public Command getCommand(IArchimateModel importedModel, IArchimateModel targetModel) throws IOException, ImportException {
        this.importedModel = importedModel;
        this.targetModel = targetModel;
        return getCommand();
    }
    
    private IArchimateModel loadModel(File importedFile) throws IOException {
        if(!importedFile.exists()) {
            throw new IOException(NLS.bind(Messages.ModelImporter_2, importedFile));
        }
        
        return IEditorModelManager.INSTANCE.load(importedFile);
    }
    
    private Command getCommand() throws IOException, ImportException {
        // Default Object Matcher
        if(objectMatcher == null) {
            objectMatcher = new DefaultObjectMatcher(targetModel);
        }
        
        statusMessages = new ArrayList<>();
        
        compoundCommand = new NonNotifyingCompoundCommand(Messages.ModelImporter_1) {
            @Override
            public void execute() {
                super.execute();
            	// Release objects for GC
                ModelImporter.this.dispose();
            }
        };
        
        // Upate root model object if the option is set
        if(updateAll) {
            updateObject(importedModel, targetModel);
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
            // Update Profiles
            else if(eObject instanceof IProfile) {
                new ProfileImporter(this).importProfile((IProfile)eObject);
            }
        }
        
        if(compoundCommand.canExecute()) {
            // Check view connection ends are valid if we have done some commands and even if update is off
            addCommand(new SetArchimateReconnectionCommand(targetModel));
            
            // Check duplicate names in Profiles
            // TODO: No more needed because we match profiles by name and can't generate duplicates
            // addCommand(new UnduplicateProfileNamesCommand(targetModel));
        }

        return compoundCommand;
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
     * Set an object matcher to use to match target objects
     */
    public void setObjectMatcher(IObjectMatcher objectMatcher) {
        this.objectMatcher = objectMatcher;
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
    
    public void setUpdateFolderStructure(boolean update) {
        updateFolderStructure = update;
    }
    
    public boolean shouldUpdateFolderStructure() {
        return updateFolderStructure;
    }
    
    public List<StatusMessage> getStatusMessages() {
        return statusMessages;
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
     * Find an object in the target model based on the eObject's unique identifier
     * Existing and newly added objects in the target model are added to the objectCache
     */
    @SuppressWarnings("unchecked")
    <T extends EObject> T findObjectInTargetModel(T eObject) throws ImportException {
        EObject foundObject = objectMatcher.getMatchingObject(eObject);
        
        // Not found
        if(foundObject == null) {
            return null;
        }
        
        // Not the right class, so that's an error we should report
        if(foundObject.eClass() != eObject.eClass()) {
            throw new ImportException("Found object with same id but different class: " + eObject); //$NON-NLS-1$
        }

        // Found an element with same key and the class is the same
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

            // Clear Profiles. These will be copied later
            if(newObject instanceof IProfiles) {
                ((IProfiles)newObject).getProfiles().clear();
            }
        }
        else {
            newObject = (IArchimateModelObject)EcoreUtil.create(eObject.eClass());
            updateObject(eObject, newObject);
        }
        
        newObject.setId(eObject.getId());
        
        // Add this to the object matcher so that all newly created objects can be found
        objectMatcher.add(newObject);
        
        return (T)newObject;
    }
    
    /**
     * Update target object with data from source object 
     */
    void updateObject(EObject importedObject, EObject targetObject) {
    	// EAttributes
        updateEObjectFeatures(importedObject, targetObject);
    	
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
     * Update all EAttributes (Name, Documentation, Access type, Influence streangth...) in a generic manner
     */
    void updateEObjectFeatures(EObject importedObject, EObject targetObject) {
        if(importedObject == null || targetObject == null) {
            return;
        }

        for(EStructuralFeature eStructuralFeature : importedObject.eClass().getEAllStructuralFeatures()) {
            if(eStructuralFeature instanceof EAttribute                     // EAttribute
                    && !IGNORED_EATTRIBUTES.contains(eStructuralFeature)    // Check ignored list
                    && eStructuralFeature.isChangeable()                    // Can change it
                    && !eStructuralFeature.isDerived()                      // Is not derived
                    && !((EAttribute)eStructuralFeature).isID())            // Is not ID
            {
                addCommand(new EObjectFeatureCommand(null, targetObject, eStructuralFeature, importedObject.eGet(eStructuralFeature)));
            }
        }
    }
    
    /**
     * Log a status message
     */
    void logMessage(StatusMessageLevel level, String message, Object... objs) {
        statusMessages.add(new StatusMessage(level, message, objs));
    }
    
    /**
     * Release objects for GC
     */
    public void dispose() {
        importedModel = null;
        targetModel = null;
        compoundCommand = null;
        objectMatcher = null;
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
            oldProperties = new ArrayList<>(targetObject.getProperties());
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
        public boolean canExecute() {
            return !EcoreUtil.equals(importedObject.getProperties(), oldProperties);
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
        private Collection<Entry<String, String>> oldFeatures;

        private UpdateFeaturesCommand(IFeatures importedObject, IFeatures targetObject) {
            this.importedObject = importedObject;
            this.targetObject = targetObject;
            oldFeatures = EcoreUtil.copyAll(targetObject.getFeatures());
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
        public boolean canExecute() {
            // TODO Not sure about this
            return !importedObject.getFeatures().equals(oldFeatures);
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

    /**
     * Check for duplicate names in Profiles and append a string
     * TODO: can be removed (if kept, then should be refactored so that suffixes are not duplicates)
     */
    @SuppressWarnings("unused")
    private static class UnduplicateProfileNamesCommand extends CompoundCommand {
        private IArchimateModel targetModel;
        
        private UnduplicateProfileNamesCommand(IArchimateModel targetModel) {
            this.targetModel = targetModel;
            
            // Add an empty Command so this always executes
            add(new Command() {});

        }
        
        @Override
        public void execute() {
            for(IProfile profile : targetModel.getProfiles()) {
                IProfile p = ArchimateModelUtils.getProfileByNameAndType(targetModel, profile.getName(), profile.getConceptType());
                if(p != null && p != profile) {
                    add(new EObjectFeatureCommand(null, profile, IArchimatePackage.Literals.NAMEABLE__NAME, profile.getName() + "_1")); //$NON-NLS-1$
                }
            }
            
            super.execute();
        }
        
        @Override
        public void dispose() {
            super.dispose();
            targetModel = null;
        }
    }
}
