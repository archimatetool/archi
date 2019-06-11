/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.archimatetool.editor.diagram.commands.CreateDiagramObjectCommand;
import com.archimatetool.editor.diagram.commands.SetConstraintObjectCommand;
import com.archimatetool.editor.diagram.editparts.IConstrainedSizeEditPart;
import com.archimatetool.editor.diagram.editparts.INonResizableEditPart;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Policy for General Diagrams
 * 
 * @author Phillip Beauvoir
 */
public class DiagramLayoutPolicy
extends XYLayoutEditPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Rectangle bounds = getConstraintFor(request);
        return new CreateDiagramObjectCommand(getHost(), request, bounds);
    }
    
    /*
     * Over-ride this to get any extra constraints for an object
     */
    @Override
    protected Rectangle getConstraintFor(CreateRequest request) {
        Rectangle bounds = (Rectangle)super.getConstraintFor(request);
        
        Dimension d = getMaximumSizeFor(request.getNewObjectType());
        bounds.width = Math.min(d.width, bounds.width);
        bounds.height = Math.min(d.height, bounds.height);
        
        return bounds;
    }
    
    /**
     * @param object
     * @return The Maximum size constraint for an object
     */
    protected Dimension getMaximumSizeFor(Object object) {
        return IFigure.MAX_DIMENSION;
    }
    
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        if(isChildEditPartLocked(child)) {
            return new LockedResizableEditPolicy();
        }
        if(child instanceof INonResizableEditPart) {
            return new NonResizableEditPolicy();
        }
        if(child instanceof IConstrainedSizeEditPart) {
            return new ConstrainedResizableEditPolicy();
        }
        return new ResizableEditPolicy();
    }
    
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        // If child part is locked, limit movement
        if(isChildEditPartLocked(child)) {
            return null;
        }

        // Return a command that can move and/or resize a child
        if(constraint instanceof Rectangle) {
            return new SetConstraintObjectCommand(request, (IDiagramModelObject)child.getModel(), (Rectangle)constraint);
        }

        return null;
    }
    
    /**
     * @param child
     * @return True id child EditPart is locked
     */
    protected boolean isChildEditPartLocked(EditPart child) {
        return child.getModel() instanceof ILockable && ((ILockable)child.getModel()).isLocked();
    }
    
    /*
     * This allows us to drag parts from a parent container to another.
     * This is the "add" counterpart to the "remove" Command created in BasicContainerEditPolicy.getOrphanChildrenCommand(GroupRequest);
     * 
     * If you don't want a part to be added, return null here.
     */
    @Override
    protected AddObjectCommand createAddCommand(ChangeBoundsRequest request, EditPart childEditPart, Object constraint) {
        IDiagramModelContainer parent = (IDiagramModelContainer)getHost().getModel();
        IDiagramModelObject child = (IDiagramModelObject)childEditPart.getModel();
        
        Rectangle bounds = (Rectangle)constraint;

        // Keep within the parent box
        // Fixed 2019-06-11 to check that the parent is not a diagram model (which can have negative space)
        if(!(parent instanceof IDiagramModel)) {
            if(bounds.x < 0) {
                bounds.x = 0;
            }
            if(bounds.y < 0) {
                bounds.y = 0;
            }
        }
        
        return new AddObjectCommand(parent, child, bounds);
    }
    
    /**
     * AddObjectCommand
     */
    public static class AddObjectCommand extends Command {
        IDiagramModelContainer parent;
        IDiagramModelObject child;
        Rectangle bounds;

        public AddObjectCommand(IDiagramModelContainer parent, IDiagramModelObject child, Rectangle bounds) {
            this.parent = parent;
            this.child = child;
            this.bounds = bounds.getCopy();
        }
        
        @Override
        public void execute() {
            child.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
            parent.getChildren().add(child);
        }

        @Override
        public void undo() {
            parent.getChildren().remove(child);
        }
        
        @Override
        public void dispose() {
            parent = null;
            child = null;
        }
    }
}

