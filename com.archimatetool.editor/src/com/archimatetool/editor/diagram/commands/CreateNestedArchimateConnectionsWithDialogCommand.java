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
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Compound Command to create new relations between parent and childObjects in nested objects.
 * This is used when the user drags objects into a parent Archimate object and nested relations to child are required.
 * 
 * 1. User drags newly created child object(s) into the parent where there is _no_ relationship.
 *    Ask via a dialog what user wants. If yes, create relationship.
 *    This is when creating new child objects from palette or from DnD from model tree.
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
        // Add new relationships if user wants them
        createRelationshipDialogCommands();

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
    void createRelationshipDialogCommands() {
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
                    Command cmd = new AddRelationshipCommand(fParentObject.getArchimateElement(),
                            childObjectsForDialog.get(0).getArchimateElement(), eClass);
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
                    Command cmd = new AddRelationshipCommand(fParentObject.getArchimateElement(),
                            selType.childObject.getArchimateElement(), selType.relationshipType);
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
        
        // Not if there is already a relationship of an allowed type between the two
        for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
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

    static class AddRelationshipCommand extends Command {
        private IArchimateElement source;
        private IArchimateElement target;
        private IArchimateRelationship relationship;
        private IFolder folder;
        
        public AddRelationshipCommand(IArchimateElement source, IArchimateElement target, EClass relationshipType) {
            this.source = source;
            this.target = target;
            this.relationship = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(relationshipType);
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
}
