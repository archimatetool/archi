package com.archimatetool.editor.model.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Set Concept Type Commands
 * 
 * @author Phillip Beauvoir
 */
public class SetConceptTypeCommandFactory {
    
    /**
     * Command to execute.
     * 
     * After the command has executed the new concept can be accessed with 
     */
    public static abstract class SetConceptTypeCommand extends AlwaysExecutingCompoundCommand {
        public SetConceptTypeCommand(String label) {
            super(label);
        }
        
        /**
         * @return The new concept created when this command has executed
         */
        public abstract IArchimateConcept getNewConcept();
    }
    
    /**
     * @param eClass The EClass to set to
     * @param element The element to change
     * @return The Command or null if the element's EClass equals eClass or element has already been removed from the model
     */
    public static SetConceptTypeCommand createSetElementTypeCommand(EClass eClass, IArchimateElement element, boolean doAddDocumentationNote) {
        // Same type, or element has already been removed from model, or Junction
        if(eClass.equals(element.eClass()) || element.getArchimateModel() == null ||
                eClass == IArchimatePackage.eINSTANCE.getJunction() ||
                element.eClass() == IArchimatePackage.eINSTANCE.getJunction()) {
            return null;
        }
        
        SetConceptTypeCommand command = new SetConceptTypeCommand(Messages.SetConceptTypeCommandFactory_0) {
            IArchimateElement newElement;

            @Override
            public void execute() {
                // Create a new element
                newElement = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
                newElement.setName(element.getName());
                
                // Copy Properties
                Collection<IProperty> props = EcoreUtil.copyAll(element.getProperties());
                newElement.getProperties().addAll(props);
                
                // Copy Features
                Collection<IFeature> features = EcoreUtil.copyAll(element.getFeatures());
                newElement.getFeatures().addAll(features);

                // Copy Documentation
                newElement.setDocumentation(element.getDocumentation());
                
                // Don't change Specialization because the class will be different
                
                // Add the new element to a folder in the model
                add(new AddListMemberCommand<EObject>(getFolderForNewConcept(element, newElement).getElements(), newElement));
                
                // Set source relations of old element to new element
                for(IArchimateRelationship relation : element.getSourceRelationships()) {
                    add(new Command() {
                        IArchimateConcept oldSource = relation.getSource();
                        
                        @Override
                        public void execute() {
                            relation.setSource(newElement);
                        }
                        
                        @Override
                        public void undo() {
                            relation.setSource(oldSource);
                        }
                        
                        @Override
                        public void dispose() {
                            oldSource = null;
                        }
                    });
                }
                
                // Set target relations of old element to new element
                for(IArchimateRelationship relation : element.getTargetRelationships()) {
                    add(new Command() {
                        IArchimateConcept oldTarget = relation.getTarget();
                        
                        @Override
                        public void execute() {
                            relation.setTarget(newElement);
                        }
                        
                        @Override
                        public void undo() {
                            relation.setTarget(oldTarget);
                        }
                        
                        @Override
                        public void dispose() {
                            oldTarget = null;
                        }
                    });
                }
                
                // Set all diagram object instances to the new element
                for(IDiagramModel dm : element.getArchimateModel().getDiagramModels()) {
                    for(IDiagramModelArchimateObject dmo : DiagramModelUtils.findDiagramModelObjectsForElement(dm, element)) {
                        add(new Command() {
                            IDiagramModelContainer parent = (IDiagramModelContainer)dmo.eContainer();
                            int index = parent.getChildren().indexOf(dmo);
                            int oldType = dmo.getType();
                            int newType = ArchiPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + newElement.eClass().getName());
                            
                            @Override
                            public void execute() {
                                // Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
                                // This will effectively remove the concept listener from the Edit Part
                                parent.getChildren().remove(dmo);

                                // Set it
                                dmo.setArchimateElement(newElement);

                                // Set its figure type
                                dmo.setType(newType);
                                
                                // And re-add it which will also update the UI
                                parent.getChildren().add(index, dmo);
                            }
                            
                            @Override
                            public void undo() {
                                // Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
                                // This will effectively remove the concept listener from the Edit Part
                                parent.getChildren().remove(dmo);
                                
                                // Set it back to old element
                                dmo.setArchimateElement(element);
                                
                                // Set its figure type
                                dmo.setType(oldType);
                                
                                // And re-add which will also update the UI
                                parent.getChildren().add(index, dmo);
                            }
                            
                            @Override
                            public void dispose() {
                                parent = null;
                            }
                        });
                    }
                }
                
                // Remove the old element from its folder
                add(new RemoveListMemberCommand<EObject>(((IFolder)element.eContainer()).getElements(), element));
                
                // Change any invalid source/target relations
                add(changeInvalidRelations(eClass, element, doAddDocumentationNote));
                
                // Now execute these commands
                super.execute();
            }

            @Override
            public IArchimateConcept getNewConcept() {
                return newElement;
            }
        };

        return command;
    }

    /**
     * @param eClass The EClass to set to
     * @param element The relation to change
     * @return The Command or null if the relation's EClass equals eClass or if the relation would be invalid or relation has already been removed from the model
     */
    public static SetConceptTypeCommand createSetRelationTypeCommand(EClass eClass, IArchimateRelationship relation) {
        return createSetRelationTypeCommand(eClass, relation, false);
    }
    
    private static SetConceptTypeCommand createSetRelationTypeCommand(EClass eClass, IArchimateRelationship relation, boolean doAddDocumentationNote) {
        // Same type or relation has already been removed from model or invalid type
        if(eClass.equals(relation.eClass()) || relation.getArchimateModel() == null || !isValidTypeForConcept(eClass, relation)) {
            return null;
        }

        SetConceptTypeCommand compoundCmd = new SetConceptTypeCommand(Messages.SetConceptTypeCommandFactory_1) {
            IArchimateRelationship newRelation;
            
            @Override
            public void execute() {
                // Create a new relation
                newRelation = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(eClass);
                newRelation.setName(relation.getName());
                
                // Copy Properties
                Collection<IProperty> props = EcoreUtil.copyAll(relation.getProperties());
                newRelation.getProperties().addAll(props);
                
                // Copy Features
                Collection<IFeature> features = EcoreUtil.copyAll(relation.getFeatures());
                newRelation.getFeatures().addAll(features);

                // Copy Documentation
                String documentation = relation.getDocumentation();
                
                // Add a note that the relation was changed
                if(doAddDocumentationNote) {
                    newRelation.setDocumentation(NLS.bind(Messages.SetConceptTypeCommandFactory_2
                            + (StringUtils.isSet(documentation) ? "\n\n" : ""), //$NON-NLS-1$ //$NON-NLS-2$
                            ArchiLabelProvider.INSTANCE.getDefaultName(relation.eClass()))
                            + documentation);
                }
                else {
                    newRelation.setDocumentation(documentation);
                }
                
                // Don't change Specialization because the class will be different

                // Add the new relation to a folder in the model and connect it to old relation's source and target
                add(new Command() {
                    IFolder parent = getFolderForNewConcept(relation, newRelation);
                    
                    @Override
                    public void execute() {
                        newRelation.connect(relation.getSource(), relation.getTarget());
                        parent.getElements().add(newRelation);
                    }
                    
                    @Override
                    public void undo() {
                        newRelation.disconnect();
                        parent.getElements().remove(newRelation);
                    }
                    
                    @Override
                    public void redo() {
                        newRelation.reconnect();
                        parent.getElements().add(newRelation);
                    }
                    
                    @Override
                    public void dispose() {
                        parent = null;
                    }
                });
                
                // Set source relations of old relation to new relation
                for(IArchimateRelationship r : relation.getSourceRelationships()) {
                    add(new Command() {
                        IArchimateConcept oldSource = r.getSource();
                        
                        @Override
                        public void execute() {
                            r.setSource(newRelation);
                        }
                        
                        @Override
                        public void undo() {
                            r.setSource(oldSource);
                        }
                        
                        @Override
                        public void dispose() {
                            oldSource = null;
                        }
                    });
                }
                
                // Set target relations of old relation to new relation
                for(IArchimateRelationship r : relation.getTargetRelationships()) {
                    add(new Command() {
                        IArchimateConcept oldTarget = r.getTarget();
                        
                        @Override
                        public void execute() {
                            r.setTarget(newRelation);
                        }
                        
                        @Override
                        public void undo() {
                            r.setTarget(oldTarget);
                        }
                        
                        @Override
                        public void dispose() {
                            oldTarget = null;
                        }
                    });
                }

                // Set all diagram connection instances to the new relation
                for(IDiagramModel dm : relation.getArchimateModel().getDiagramModels()) {
                    for(IDiagramModelArchimateConnection dmc : DiagramModelUtils.findDiagramModelConnectionsForRelation(dm, relation)) {
                        add(new Command() {
                            @Override
                            public void execute() {
                                // This will deregister listeners on the concept and update the UI
                                dmc.disconnect();
                                
                                // Set it to new relation
                                dmc.setArchimateRelationship(newRelation);
                             
                                // Reconnect and update UI
                                dmc.reconnect();
                            }
                            
                            @Override
                            public void undo() {
                                // This will deregister listeners on the concept and update the UI
                                dmc.disconnect();
                                
                                // Set it back to old relation
                                dmc.setArchimateRelationship(relation);
                                
                                // Reconnect and update UI
                                dmc.reconnect();
                            }
                        });
                    }
                }
                
                // Disconnect the old relation from its source and target concepts
                add(new Command() {
                    @Override
                    public void execute() {
                        relation.disconnect();
                    }
                    
                    @Override
                    public void undo() {
                        relation.reconnect();
                    }
                });
                
                // Remove the old relation from its folder
                add(new RemoveListMemberCommand<EObject>(((IFolder)relation.eContainer()).getElements(), relation));
                
                // Last thing - change any invalid source/target relations
                // This is not really necessary as all relations from relations are Association
                add(changeInvalidRelations(eClass, relation, doAddDocumentationNote));
                
                // Now execute these commands
                super.execute();
            }

            @Override
            public IArchimateConcept getNewConcept() {
                return newRelation;
            }
        };
        
        return compoundCmd;
    }

    // ------------------------------------------------------------------------------------------
    // Shared
    // ------------------------------------------------------------------------------------------
    
    /**
     * If changing the concept type would result in invalid source/target relations then change each invalid relation to Association
     */
    private static Command changeInvalidRelations(EClass eClass, IArchimateConcept concept, boolean doAddDocumentationNote) {
        CompoundCommand compoundCmd = new CompoundCommand();
        
        // Use a Set in case the source and target relation is the same (circular relationship)
        Set<IArchimateRelationship> relations = new HashSet<>();

        for(IArchimateRelationship relation : concept.getSourceRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(eClass, relation.getTarget().eClass(), relation.eClass())) {
                relations.add(relation);
            }
        }
        
        for(IArchimateRelationship relation : concept.getTargetRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(relation.getSource().eClass(), eClass, relation.eClass())) {
                relations.add(relation);
            }
        }
        
        for(IArchimateRelationship relation : relations) {
            compoundCmd.add(createSetRelationTypeCommand(IArchimatePackage.eINSTANCE.getAssociationRelationship(), relation, doAddDocumentationNote));
        }
        
        return compoundCmd.canExecute() ? compoundCmd : null;
    }

    public static boolean isValidTypeForConcept(EClass eClass, IArchimateConcept concept) {
        // Check source relationships
        for(IArchimateRelationship rel : concept.getSourceRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(eClass, rel.getTarget().eClass(), rel.eClass())) {
                return false;
            }
        }
        
        // Check target relationships
        for(IArchimateRelationship rel : concept.getTargetRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(rel.getSource().eClass(), eClass, rel.eClass())) {
                return false;
            }
        }
        
        // If a relationship, check ends
        if(concept instanceof IArchimateRelationship) {
            if(!ArchimateModelUtils.isValidRelationship(((IArchimateRelationship)concept).getSource(),
                    ((IArchimateRelationship)concept).getTarget(), eClass)) {
                return false;
            }
        }
        
        return true;
    }

    private static IFolder getFolderForNewConcept(IArchimateConcept oldConcept, IArchimateConcept newConcept) {
        if(isCorrectFolderForObject((IFolder)oldConcept.eContainer(), newConcept)) {
            return (IFolder)oldConcept.eContainer();
        }
        
        return oldConcept.getArchimateModel().getDefaultFolderForObject(newConcept);
    }
    
    /**
     * @return true if the given folder is the correct folder to contain this concept
     */
    private static boolean isCorrectFolderForObject(IFolder folder, IArchimateConcept concept) {
        if(folder == null || concept == null) {
            return false;
        }
        
        // Check that the object is in the correct main category folder 
        IFolder topFolder = folder.getArchimateModel().getDefaultFolderForObject(concept);
        if(folder == topFolder) {
            return true;
        }
        
        EObject e = folder;
        while((e = e.eContainer()) != null) {
            if(e == topFolder) {
                return true;
            }
        }
        
        return false;
    }

}
