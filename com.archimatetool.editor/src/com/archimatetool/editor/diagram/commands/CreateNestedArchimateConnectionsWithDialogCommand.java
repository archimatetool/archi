/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.window.Window;

import com.archimatetool.editor.diagram.dialog.NewNestedRelationDialog;
import com.archimatetool.editor.diagram.dialog.NewNestedRelationsDialog;
import com.archimatetool.editor.diagram.dialog.NewNestedRelationsDialog.SelectedRelationshipType;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;
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
    void createConnectionDialogCommands() {
        // Gather suitable child objects
        List<IDiagramModelArchimateObject> childObjectsForDialog = new ArrayList<IDiagramModelArchimateObject>();
        for(IDiagramModelArchimateObject childObject : fChildObjects) {
            if(canAddNewRelationship(fParentObject, childObject)) {
                childObjectsForDialog.add(childObject);
            }
        }
        
        // One
        if(childObjectsForDialog.size() == 1) {
            NewNestedRelationDialog dialog = new NewNestedRelationDialog(fParentObject, childObjectsForDialog.get(0));
            
            if(dialog.open() == Window.OK) {
                EClass eClass = dialog.getSelectedType();
                if(eClass != null) {
                    Command cmd = new CreateRelationshioAndDiagramArchimateConnectionCommand(fParentObject, childObjectsForDialog.get(0), eClass);
                    add(cmd);
                }
            }
        }
        
        // More than one
        else if(childObjectsForDialog.size() > 1) {
            NewNestedRelationsDialog dialog = new NewNestedRelationsDialog(fParentObject, childObjectsForDialog);
            
            if(dialog.open() == Window.OK) {
                List<SelectedRelationshipType> selectedTypes = dialog.getSelectedTypes();
                
                for(SelectedRelationshipType selType : selectedTypes) {
                    Command cmd = new CreateRelationshioAndDiagramArchimateConnectionCommand(fParentObject, selType.childObject, selType.relationshipType);
                    add(cmd);
                }
            }
        }
    }
    
    /**
     * @return true if a new relation can/should be added between parent and child when adding an element to a View
     */
    boolean canAddNewRelationship(IDiagramModelArchimateObject parentObject, IDiagramModelArchimateObject childObject) {
        IArchimateElement parentElement = parentObject.getArchimateElement();
        IArchimateElement childElement = childObject.getArchimateElement();
        
        // Disallow certain types
        if(!(DiagramModelUtils.isNestedConnectionTypeComponent(parentElement) && DiagramModelUtils.isNestedConnectionTypeComponent(childElement))) {
            return false;
        }
        
        // Not if there is already a relationship of an allowed type between the two
        for(IRelationship relation : parentElement.getSourceRelationships()) {
            if(relation.getTarget() == childElement) {
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
                    if(relation.eClass() == eClass) {
                        return false;
                    }
                }
            }
        }
        
        // Check this is a valid relationship
        for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
            if(ArchimateModelUtils.isValidRelationship(parentElement, childElement, eClass)) {
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
    void createNewConnectionCommands() {
        IArchimateElement parentElement = fParentObject.getArchimateElement();
        
        for(IDiagramModelArchimateObject childObject : fChildObjects) {
            IArchimateElement childElement = childObject.getArchimateElement();
            
            // Disallow certain types
            if(!(DiagramModelUtils.isNestedConnectionTypeComponent(parentElement) && DiagramModelUtils.isNestedConnectionTypeComponent(childElement))) {
                continue;
            }

            for(IRelationship relation : parentElement.getSourceRelationships()) {
                if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                    // And there's not one already there...
                    if(!DiagramModelUtils.hasDiagramModelArchimateConnection(fParentObject, childObject, relation)) {
                        add(new CreateDiagramArchimateConnectionCommand(fParentObject, childObject, relation));
                    }
                }
            }
        }
    }
    
    static class CreateRelationshioAndDiagramArchimateConnectionCommand extends CompoundCommand {
        
        CreateRelationshioAndDiagramArchimateConnectionCommand(IDiagramModelArchimateObject sourceObject,
                IDiagramModelArchimateObject targetObject, EClass relationshipType) {
            
            IRelationship relationship = (IRelationship)IArchimateFactory.eINSTANCE.create(relationshipType);
            
            Command cmd = new AddRelationshipCommand(sourceObject.getArchimateElement(), targetObject.getArchimateElement(), relationship);
            add(cmd);
            
            cmd = new CreateDiagramArchimateConnectionCommand(sourceObject, targetObject, relationship);
            add(cmd);
        }
    }
    
    static class AddRelationshipCommand extends Command {
        private IArchimateElement source;
        private IArchimateElement target;
        private IRelationship relationship;
        private IFolder folder;
        
        public AddRelationshipCommand(IArchimateElement source, IArchimateElement target, IRelationship relationship) {
            this.source = source;
            this.target = target;
            this.relationship = relationship;
        }
        
        @Override
        public void execute() {
            relationship.connect(source, target);
            folder = target.getArchimateModel().getDefaultFolderForElement(relationship);
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
    static class CreateDiagramArchimateConnectionCommand extends Command {
        IDiagramModelArchimateConnection connection;
        IDiagramModelArchimateObject source;
        IDiagramModelArchimateObject target;
        IRelationship relationship;
        
        CreateDiagramArchimateConnectionCommand(IDiagramModelArchimateObject source, IDiagramModelArchimateObject target, IRelationship relationship) {
            this.source = source;
            this.target = target;
            this.relationship = relationship;
        }
        
        @Override
        public void execute() {
            connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            connection.setRelationship(relationship);
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
