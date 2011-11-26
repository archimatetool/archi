/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import uk.ac.bolton.archimate.editor.model.commands.NonNotifyingCompoundCommand;
import uk.ac.bolton.archimate.model.IBounds;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Basic Container EditPolicy
 * 
 * @author Phillip Beauvoir
 */
public class BasicContainerEditPolicy extends ContainerEditPolicy {

	@Override
    protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

    /*
     * This allows us to drag parts from a parent container to another.
     * This is the "remove" counterpart to the "add" Command created in DiagramLayoutPolicy.createAddCommand(EditPart, Object);
     * 
     * If you don't want a part to be removed, return null here.
     */
	@Override
    public Command getOrphanChildrenCommand(GroupRequest request) {
        CompoundCommand result = new NonNotifyingCompoundCommand("Move elements");

        IDiagramModelContainer parent = (IDiagramModelContainer)getHost().getModel();

        for(Object o : request.getEditParts()) {
            EditPart editPart = (EditPart)o;
            IDiagramModelObject child = (IDiagramModelObject)editPart.getModel();
            result.add(new RemoveObjectCommand(parent, child));
        }
        
        return result;
    }
	
	/**
	 * Remove Object Command
	 */
	static class RemoveObjectCommand extends Command {
	    IDiagramModelContainer fParent;
	    IDiagramModelObject fChild;
	    IBounds fOldBounds;
	    int fIndex;

	    public RemoveObjectCommand(IDiagramModelContainer parent, IDiagramModelObject child) {
	        fParent = parent;
	        fChild = child;
	    }
	    
	    @Override
	    public void execute() {
	        fOldBounds = fChild.getBounds();
	        fIndex = fParent.getChildren().indexOf(fChild); // Ensure this is stored just before execute
	        fParent.getChildren().remove(fChild);
	    }

	    @Override
	    public void redo() {
	        fParent.getChildren().remove(fChild);
	    }

	    @Override
	    public void undo() {
	        fChild.setBounds(fOldBounds);
	        fParent.getChildren().add(fIndex, fChild);
	    }
	    
	    @Override
	    public void dispose() {
	        fParent = null;
	        fChild = null;
	    }
	}
}