/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
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
import com.archimatetool.model.IDiagramModelConnection;
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
     * A compound command that always executes and all sub-commands are executed after adding
     * and returns the new concept that was created
     */
    private static abstract class SetConceptTypeCommand extends AlwaysExecutingChainedCompoundCommand {
        /**
         * @return The new concept created when this command has executed
         */
        abstract IArchimateConcept getNewConcept();
    }

    /**
     * @param eClass The EClass to set to
     * @param element The element to change
     * @param doAddDocumentationNote If true append a note to the documentation field of any changed relationship that was changed to Association
     * @return The Command or null if the element's EClass equals eClass or the element has been removed from the model
     *                     or if the element or EClass is a Junction
     */
    public static CompoundCommand createSetElementTypeCommand(EClass eClass, IArchimateElement element, boolean doAddDocumentationNote) {
        return createSetElementTypeCommand(eClass, Set.of(element), doAddDocumentationNote);
    }
    
    /**
     * @param eClass The EClass to set to
     * @param elements The elements to change
     * @param doAddDocumentationNote If true append a note to the documentation field of any changed relationship that was changed to Association
     * @return The Command or null if the elements' EClass equals eClass or the element has been removed from the model
     *                     or if the element or EClass is a Junction
     */
    public static CompoundCommand createSetElementTypeCommand(EClass eClass, Set<IArchimateElement> elements, boolean doAddDocumentationNote) {
        final List<SetConceptTypeCommand> commands = new ArrayList<>();
        
        // Create the element type sub-commands first
        for(IArchimateElement element : elements) {
            SetConceptTypeCommand cmd = createSetElementTypeCommand(eClass, element);
            if(cmd != null) {
                commands.add(cmd);
            }
        }
        
        // If there are no commands return null
        if(commands.isEmpty()) {
            return null;
        }
        
        CompoundCommand command = new AlwaysExecutingChainedCompoundCommand(Messages.SetConceptTypeCommandFactory_0) {
            @Override
            public void execute() {
                // Add and execute the element type commands first
                for(SetConceptTypeCommand cmd : commands) {
                    add(cmd);
                }
                
                // Then change any invalid source/target relations
                for(SetConceptTypeCommand cmd : commands) {
                    add(changeInvalidRelations(eClass, cmd.getNewConcept(), doAddDocumentationNote));
                }
            }
        };
        
        return command;
    }
    
    /**
     * @param eClass The EClass to set to
     * @param element The element to change
     * @return The Command or null if the element's EClass equals eClass or the element has been removed from the model
     *                     or if the element or EClass is a Junction
     */
    private static SetConceptTypeCommand createSetElementTypeCommand(EClass eClass, IArchimateElement element) {
        // Element has been removed from model, same type or is a Junction
        if(element.getArchimateModel() == null ||
                eClass == element.eClass() ||
                eClass == IArchimatePackage.eINSTANCE.getJunction() ||
                element.eClass() == IArchimatePackage.eINSTANCE.getJunction()) {
            return null;
        }
        
        SetConceptTypeCommand command = new SetConceptTypeCommand() {
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
                
                // Don't set Specialization/Profile because the class will be different
                
                // Add the new element to a folder in the model
                add(new AddListMemberCommand<EObject>(getFolderForNewConcept(element, newElement).getElements(), newElement));
                
                // Set source and target relations of old element to new element 
                add(createRelationshipEndsCommand(element, newElement));
                
                // Set all diagram object instances to the new element
                for(IDiagramModel dm : element.getArchimateModel().getDiagramModels()) {
                    for(IDiagramModelArchimateObject dmo : DiagramModelUtils.findDiagramModelObjectsForElement(dm, element)) {
                        add(new Command() {
                            IDiagramModelContainer parent = (IDiagramModelContainer)dmo.eContainer();
                            int index = parent.getChildren().indexOf(dmo);
                            int oldType = dmo.getType();
                            int newType = ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + newElement.eClass().getName());
                            
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
            }

            @Override
            public IArchimateConcept getNewConcept() {
                return newElement;
            }
            
            @Override
            public void dispose() {
                newElement = null;
            }
        };

        return command;
    }

    /**
     * @param eClass The EClass to set to
     * @param relation The relation to change
     * @return The Command or null if relations contains a relation whose EClass equals eClass
     *          or if the relation would be invalid or relation has been removed from the model
     */
    public static CompoundCommand createSetRelationTypeCommand(EClass eClass, IArchimateRelationship relation) {
        return createSetRelationTypeCommand(eClass, relation, false);
    }

    /**
     * @param eClass The EClass to set to
     * @param relations The relations to change
     * @return The Command or null if relations contains all relations whose EClass equals eClass
     *         or if the relation would be invalid or relation has been removed from the model
     */
    public static CompoundCommand createSetRelationTypeCommand(EClass eClass, Set<IArchimateRelationship> relations) {
        CompoundCommand compoundCommand = new CompoundCommand(Messages.SetConceptTypeCommandFactory_1);
        
        for(IArchimateRelationship relation : relations) {
            compoundCommand.add(createSetRelationTypeCommand(eClass, relation, false));
        }
        
        return compoundCommand.isEmpty() ? null : compoundCommand;
    }

    /**
     * @param eClass The EClass to set to
     * @param relation The relation to change
     * @param doAddDocumentationNote If true append a note to the documentation field that the relationship was changed to Association
     * @return The Command or null if the relation's EClass equals eClass
     *         or if the relation would be invalid or relation has been removed from the model
     */
    private static SetConceptTypeCommand createSetRelationTypeCommand(EClass eClass, IArchimateRelationship relation, boolean doAddDocumentationNote) {
        // Relation has been removed from model, is the same type or is an invalid type
        if(relation.getArchimateModel() == null ||
                eClass == relation.eClass() ||
                !isValidTypeForRelationship(eClass, relation)) {
            return null;
        }

        SetConceptTypeCommand compoundCmd = new SetConceptTypeCommand() {
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
                
                // Don't set Specialization/Profile because the class will be different

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
                
                // Set source and target relations of old relation to new relation 
                add(createRelationshipEndsCommand(relation, newRelation));
                
                // Set all diagram connection instances to the new relation
                for(IDiagramModel dm : relation.getArchimateModel().getDiagramModels()) {
                    for(IDiagramModelArchimateConnection dmc : DiagramModelUtils.findDiagramModelConnectionsForRelation(dm, relation)) {
                        add(new Command() {
                            private int oldSourcePosition;
                            private int oldTargetPosition;
                            
                            @Override
                            public void execute() {
                                // Store these here
                                oldSourcePosition = dmc.getSource().getSourceConnections().indexOf(dmc);
                                oldTargetPosition = dmc.getTarget().getTargetConnections().indexOf(dmc);
                                
                                // This will deregister listeners on the concept and update the UI
                                dmc.disconnect();
                                
                                // Set it to new relation
                                dmc.setArchimateRelationship(newRelation);
                             
                                // Reconnect and update UI
                                dmc.reconnect();
                                
                                // Move these back
                                restoreConnectionPositions();
                            }
                            
                            @Override
                            public void undo() {
                                // This will deregister listeners on the concept and update the UI
                                dmc.disconnect();
                                
                                // Set it back to old relation
                                dmc.setArchimateRelationship(relation);
                                
                                // Reconnect and update UI
                                dmc.reconnect();
                                
                                // Move these back
                                restoreConnectionPositions();
                            }
                            
                            // Restore the connection positions in their lists
                            private void restoreConnectionPositions() {
                                EList<IDiagramModelConnection> sources = dmc.getSource().getSourceConnections();
                                if(oldSourcePosition >= 0 && oldSourcePosition < sources.size() && sources.contains(dmc)) {
                                    sources.move(oldSourcePosition, dmc);
                                }
                                
                                EList<IDiagramModelConnection> targets = dmc.getTarget().getTargetConnections();
                                if(oldTargetPosition >= 0 && oldTargetPosition < targets.size() && targets.contains(dmc)) {
                                    targets.move(oldTargetPosition, dmc);
                                }
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
                add(changeInvalidRelations(eClass, newRelation, doAddDocumentationNote));
            }

            @Override
            public IArchimateConcept getNewConcept() {
                return newRelation;
            }
            
            @Override
            public void dispose() {
                newRelation = null;
            }
        };
        
        return compoundCmd;
    }

    /**
     * @param oldConcept The old concept
     * @param newConcept The new concept
     * @return A command that sets the source and target relations of oldConcept to newConcept, or null
     */
    private static CompoundCommand createRelationshipEndsCommand(IArchimateConcept oldConcept, IArchimateConcept newConcept) {
        CompoundCommand compoundCommand = new CompoundCommand();
        
        // Set source relations of old concept to new concept
        for(IArchimateRelationship relation : oldConcept.getSourceRelationships()) {
            compoundCommand.add(new Command() {
                IArchimateConcept oldSource = relation.getSource();
                
                @Override
                public void execute() {
                    relation.setSource(newConcept);
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
        
        // Set target relations of old concept to new concept
        for(IArchimateRelationship relation : oldConcept.getTargetRelationships()) {
            compoundCommand.add(new Command() {
                IArchimateConcept oldTarget = relation.getTarget();
                
                @Override
                public void execute() {
                    relation.setTarget(newConcept);
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
        
        return compoundCommand.isEmpty() ? null : compoundCommand;
    }
    
    /**
     * Check if the given element will have valid relations if we change its type to eClass.
     * We check against the member elements because each element in elements will be changed to eClass
     * and that will be the final type.
     * 
     * @param eClass The EClass that we will change the element to.
     * @param element The element that we will change
     * @param elements Elements that we are also changing to this EClass in the calling operation.
     *                 This set will also include element so that circular relations can be checked.
     * @return true if valid
     */
    public static boolean isValidTypeForElement(EClass eClass, IArchimateElement element, Set<IArchimateElement> elements) {
        // Check source relationships
        for(IArchimateRelationship relation : element.getSourceRelationships()) {
            // If the target element of the relation is one of the elements to be changed to the desired EClass then check against the desired EClass
            // Else check against the the target element's EClass
            EClass targetType = elements.contains(relation.getTarget()) ? eClass : relation.getTarget().eClass();
            
            if(!ArchimateModelUtils.isValidRelationship(eClass, targetType, relation.eClass())) {
                return false;
            }
        }
        
        // Check target relationships
        for(IArchimateRelationship relation : element.getTargetRelationships()) {
            // If the source element of the relation is one of the elements to be changed to the desired EClass then check against the desired EClass
            // Else check against the the source element's EClass
            EClass sourceType = elements.contains(relation.getSource()) ? eClass : relation.getSource().eClass();
            
            if(!ArchimateModelUtils.isValidRelationship(sourceType, eClass, relation.eClass())) {
                return false;
            }
        }
        
        return true;
    }

    public static boolean isValidTypeForRelationship(EClass eClass, IArchimateRelationship relationship) {
        // Check source and target ends
        if(!ArchimateModelUtils.isValidRelationship(relationship.getSource(), relationship.getTarget(), eClass)) {
            return false;
        }
        
        // Source and target relationships of a relationship should only be Association
        // and should always be valid, but we'll check anyway to future-proof it.
        
        // Check source relationships of the relationship
        for(IArchimateRelationship relation : relationship.getSourceRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(eClass, relation.getTarget().eClass(), relation.eClass())) {
                return false;
            }
        }
        
        // Check target relationships of the relationship
        for(IArchimateRelationship relation : relationship.getTargetRelationships()) {
            if(!ArchimateModelUtils.isValidRelationship(relation.getSource().eClass(), eClass, relation.eClass())) {
                return false;
            }
        }
        
        return true;
    }

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

    /**
     * @return the correct folder to put the new concept
     */
    private static IFolder getFolderForNewConcept(IArchimateConcept oldConcept, IArchimateConcept newConcept) {
        IFolder existingFolder = (IFolder)oldConcept.eContainer();
        IFolder defaultFolder = oldConcept.getArchimateModel().getDefaultFolderForObject(newConcept);
        
        // If the existing folder is the default folder, or is a sub-folder of the default folder, use that
        EObject e = oldConcept;
        while((e = e.eContainer()) instanceof IFolder parent) {
            if(parent == defaultFolder) {
                return existingFolder;
            }
        }
        
        // Else use default folder
        return defaultFolder;
    }
}
