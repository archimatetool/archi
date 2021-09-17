/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.window.Window;

import com.archimatetool.editor.diagram.dialog.NestedConnectionInfo;
import com.archimatetool.editor.diagram.dialog.NewNestedRelationDialog;
import com.archimatetool.editor.diagram.dialog.NewNestedRelationsDialog;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Compound Command to create new connections/relations between parent and childObjects in nested objects.
 * This is used when the user drags objects into a parent Archimate object and nested connections/relations to child are required.
 * 
 * Cases are:
 * 1. User drags newly created child object(s) into the parent where there is _no_ relationship.
 *    Ask via a dialog what user wants. If yes, create relationship & connection.
 *    This is when creating new child objects from palette or from DnD from model tree.
 * 2. User drags child object(s) into parent where there is a relationship - check if there is a connection, if not make one.
 *    This is when user drags child objects on diagram or from tree into parent and a relationship exists but no connection.
 * 3. Cases 1 & 2 can happen together if more than one child is dragged into parent.
 *    
 * @author Phillip Beauvoir
 */
public class CreateNestedArchimateConnectionsWithDialogCommand extends CompoundCommand {
    
    IDiagramModelArchimateObject fParentObject;
    List<IDiagramModelArchimateObject> fChildObjects;

    public CreateNestedArchimateConnectionsWithDialogCommand(IDiagramModelArchimateObject parentObject, IDiagramModelArchimateObject childObject) {
        fParentObject = parentObject;
        fChildObjects = new ArrayList<IDiagramModelArchimateObject>();
        fChildObjects.add(childObject);
    }

    public CreateNestedArchimateConnectionsWithDialogCommand(IDiagramModelArchimateObject parentObject, List<IDiagramModelArchimateObject> childObjects) {
        fParentObject = parentObject;
        fChildObjects = childObjects;
    }
    
    @Override
    public void execute() {
        // Add new relationships/connection if user wants them
        createConnectionDialogCommands();
        
        // Add missing connections (may have been deleted by user)
        createNewConnectionCommands();

        super.execute();
    }
    
    // These should return true always because sub-commands are only created on execute()
    
    @Override
    public boolean canExecute() {
        return true;
    }
    
    @Override
    public boolean canUndo() {
        return true;
    }
    
    @Override
    public boolean canRedo() {
        return true;
    }
    
    /** 
     * Child Objects that don't have a relationship set with parent - ask user if they want one
     */
    private void createConnectionDialogCommands() {
        // Gather suitable child objects
        List<IDiagramModelArchimateObject> childObjectsForDialog = new ArrayList<IDiagramModelArchimateObject>();
        for(IDiagramModelArchimateObject childObject : fChildObjects) {
            if(!(childObject.getArchimateConcept() instanceof IJunction)) { // Don't apply to Junctions
                if(canAddNewRelationship(fParentObject, childObject)) {
                    childObjectsForDialog.add(childObject);
                }
            }
        }
        
        // One child object
        if(childObjectsForDialog.size() == 1) {
            NewNestedRelationDialog dialog = new NewNestedRelationDialog(fParentObject, childObjectsForDialog.get(0));
            
            if(dialog.open() == Window.OK) {
                NestedConnectionInfo selected = dialog.getSelected();
                if(selected != null) {
                    Command cmd = new CreateRelationshipAndDiagramArchimateConnectionCommand(selected.getSourceObject(),
                            selected.getTargetObject(), selected.getRelationshipType());
                    add(cmd);
                }
            }
        }
        
        // More than one child object
        else if(childObjectsForDialog.size() > 1) {
            NewNestedRelationsDialog dialog = new NewNestedRelationsDialog(fParentObject, childObjectsForDialog);
            
            if(dialog.open() == Window.OK) {
                List<NestedConnectionInfo> selected = dialog.getSelected();
                for(NestedConnectionInfo sel : selected) {
                    Command cmd = new CreateRelationshipAndDiagramArchimateConnectionCommand(sel.getSourceObject(),
                            sel.getTargetObject(), sel.getRelationshipType());
                    add(cmd);
                }
            }
        }
    }
    
    /**
     * @return true if any new relation can/should be added between parent and child when adding a child element to a parent object
     */
    private boolean canAddNewRelationship(IDiagramModelArchimateObject parentObject, IDiagramModelArchimateObject childObject) {
        IArchimateElement parentElement = parentObject.getArchimateElement();
        IArchimateElement childElement = childObject.getArchimateElement();
        
        Set<EClass> relationsClassesForNewRelations = ConnectionPreferences.getRelationsClassesForNewRelations();
        Set<EClass> relationsClassesForNewReverseRelations = ConnectionPreferences.getRelationsClassesForNewReverseRelations();
        
        // Not if there is already a relationship of an allowed type between parent and child elements...

        // Normal direction
        for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
            if(relation.getTarget() == childElement) {
                if(relationsClassesForNewRelations.contains(relation.eClass())) {
                    return false;
                }
            }
        }
        
        // Reverse direction
        for(IArchimateRelationship relation : parentElement.getTargetRelationships()) {
            if(relation.getSource() == childElement) {
                // Specialization is reversed normal direction
                if(relation instanceof ISpecializationRelationship && relationsClassesForNewRelations.contains(relation.eClass())) {
                    return false;
                }
                
                if(relationsClassesForNewReverseRelations.contains(relation.eClass())) {
                    return false;
                }
            }
        }

        // Check there is at least one valid relationship...

        // Normal direction
        for(EClass eClass : relationsClassesForNewRelations) {
            if(ArchimateModelUtils.isValidRelationship(parentElement, childElement, eClass)) {
                return true;
            }
        }
        
        // Reverse direction
        for(EClass eClass : relationsClassesForNewReverseRelations) {
            if(ArchimateModelUtils.isValidRelationship(childElement, parentElement, eClass)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Create Commands for child objects that don't have connections and need new ones
     * 
     * TODO A3: If O1--C1--O2. C1 is also connected to parent.
     *          O1 or O2 is added to parent - should add connection from C1 to parent?
     *          Or should it be only when O1 AND O2 are added to parent?
     *          
     */
    private void createNewConnectionCommands() {
        IArchimateElement parentElement = fParentObject.getArchimateElement();
        
        // Check connections between parent and child objects that are being dragged in
        for(IDiagramModelArchimateObject childObject : fChildObjects) {
            IArchimateElement childElement = childObject.getArchimateElement();
            boolean aConnectionExists = false;
            
            // If there is already a connection between parent and child, then don't create another one from relationships
            
            for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
                if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                    if(DiagramModelUtils.hasDiagramModelArchimateConnection(fParentObject, childObject, relation)) {
                       aConnectionExists = true;
                       break;
                    }
                }
            }
            
            // Create connections from relationships if none already exists
            if(!aConnectionExists) {
	            for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
	                if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                        add(new CreateDiagramArchimateConnectionCommand(fParentObject, childObject, relation));
	                }
	            }
            }
            
            // Now do the reverse direction...
            
            aConnectionExists = false;
            
            for(IArchimateRelationship relation : parentElement.getTargetRelationships()) {
                if(relation.getSource() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                    if(DiagramModelUtils.hasDiagramModelArchimateConnection(childObject, fParentObject, relation)) {
                       aConnectionExists = true;
                       break;
                    }
                }
            }
            
            // Create connections from relationships if none already exists
            if(!aConnectionExists) {
                for(IArchimateRelationship relation : parentElement.getTargetRelationships()) {
                    if(relation.getSource() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                        add(new CreateDiagramArchimateConnectionCommand(childObject, fParentObject, relation));
                    }
                }
            }
        }
        
        // Check connections between parent and child connections of objects
        for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
            if(relation.getTarget() instanceof IArchimateRelationship) {
                IDiagramModelArchimateConnection dmc = findConnection((IArchimateRelationship)relation.getTarget());
                if(dmc != null) {
                    if(!DiagramModelUtils.hasDiagramModelArchimateConnection(fParentObject, dmc, relation)) {
                        add(new CreateDiagramArchimateConnectionCommand(fParentObject, dmc, relation));
                    }
                }
            }
        }
        
        for(IArchimateRelationship relation : parentElement.getTargetRelationships()) {
            if(relation.getSource() instanceof IArchimateRelationship) {
                IDiagramModelArchimateConnection dmc = findConnection((IArchimateRelationship)relation.getSource());
                if(dmc != null) {
                    if(!DiagramModelUtils.hasDiagramModelArchimateConnection(fParentObject, dmc, relation)) {
                        add(new CreateDiagramArchimateConnectionCommand(fParentObject, dmc, relation));
                    }
                }
            }
        }
    }
    
    private IDiagramModelArchimateConnection findConnection(IArchimateRelationship relation) {
        for(IDiagramModelObject dmo : fParentObject.getChildren()) {
            for(IDiagramModelConnection dmc : dmo.getSourceConnections()) {
                if(dmc instanceof IDiagramModelArchimateConnection) {
                    if(((IDiagramModelArchimateConnection)dmc).getArchimateRelationship() == relation) {
                        return (IDiagramModelArchimateConnection)dmc;
                    }
                }
            }
            for(IDiagramModelConnection dmc : dmo.getTargetConnections()) {
                if(dmc instanceof IDiagramModelArchimateConnection) {
                    if(((IDiagramModelArchimateConnection)dmc).getArchimateRelationship() == relation) {
                        return (IDiagramModelArchimateConnection)dmc;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Command to create Relationship and Connection
     */
    private static class CreateRelationshipAndDiagramArchimateConnectionCommand extends CompoundCommand {
        
        CreateRelationshipAndDiagramArchimateConnectionCommand(IDiagramModelArchimateObject sourceObject,
                IDiagramModelArchimateObject targetObject, EClass relationshipType) {
            
            IArchimateRelationship relationship = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(relationshipType);
            
            Command cmd = new AddRelationshipCommand(sourceObject.getArchimateElement(), targetObject.getArchimateElement(), relationship);
            add(cmd);
            
            cmd = new CreateDiagramArchimateConnectionCommand(sourceObject, targetObject, relationship);
            add(cmd);
        }
    }
    
    private static class AddRelationshipCommand extends Command {
        private IArchimateElement source;
        private IArchimateElement target;
        private IArchimateRelationship relationship;
        private IFolder folder;
        
        public AddRelationshipCommand(IArchimateElement source, IArchimateElement target, IArchimateRelationship relationship) {
            this.source = source;
            this.target = target;
            this.relationship = relationship;
        }
        
        @Override
        public void execute() {
            relationship.connect(source, target);
            folder = target.getArchimateModel().getDefaultFolderForObject(relationship);
            folder.getElements().add(relationship);
        }

        @Override
        public void undo() {
            folder.getElements().remove(relationship);
            relationship.disconnect();
        }
        
        @Override
        public void redo() {
            folder.getElements().add(relationship);
            relationship.reconnect();
        }
        
        @Override
        public void dispose() {
            source = null;
            target = null;
            folder = null;
            relationship = null;
        }
    }
    
    /**
     * Create New Connection Command based on existing relation
     */
    private static class CreateDiagramArchimateConnectionCommand extends Command {
        IDiagramModelArchimateConnection connection;
        IDiagramModelArchimateComponent source;
        IDiagramModelArchimateComponent target;
        IArchimateRelationship relationship;
        
        CreateDiagramArchimateConnectionCommand(IDiagramModelArchimateComponent source, IDiagramModelArchimateComponent target, IArchimateRelationship relationship) {
            this.source = source;
            this.target = target;
            this.relationship = relationship;
        }
        
        @Override
        public void execute() {
            connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            connection.setArchimateRelationship(relationship);
            connection.connect(source, target);
        }
        
        @Override
        public void redo() {
            connection.reconnect();
        }
        
        @Override
        public void undo() {
            connection.disconnect();
        }

        @Override
        public void dispose() {
            connection = null;
            source = null;
            target = null;
            relationship = null;
        }
    }
}
