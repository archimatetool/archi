/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramArchimateObjectCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramObjectCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.SetConstraintObjectCommand;
import uk.ac.bolton.archimate.editor.diagram.editparts.INonResizableEditPart;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Policy for General Diagram
 * 
 * @author Phillip Beauvoir
 */
public class DiagramLayoutPolicy
extends XYLayoutEditPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Rectangle bounds = getConstraintFor(request);
        
        if(request.getNewObjectType() instanceof EClass) {
            EClass eClass = (EClass)request.getNewObjectType();
            
            // Archimate type object
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
                return new CreateDiagramArchimateObjectCommand((IDiagramModelContainer)getHost().getModel(), request, bounds);
            }
            // Other non-Archimate object (note, group, sticky)
            else {
                return new CreateDiagramObjectCommand((IDiagramModelContainer)getHost().getModel(), request, bounds);
            }
        }
        
        return null;
    }
    
    /*
     * Over-ride this to get any extra constraints for an object
     */
    @Override
    protected Rectangle getConstraintFor(CreateRequest request) {
        Rectangle bounds = (Rectangle)super.getConstraintFor(request);
        
        if(request.getNewObjectType() instanceof EClass) {
            Dimension d = getMaximumSizeFor((EClass)request.getNewObjectType());
            bounds.width = Math.min(d.width, bounds.width);
            bounds.height = Math.min(d.height, bounds.height);
        }
        
        return bounds;
    }
    
    /**
     * @param eClass
     * @return The Maximum size constraint for an object
     */
    protected Dimension getMaximumSizeFor(EClass eClass) {
        // Junctions should not be bigger than default size
        if(IArchimatePackage.eINSTANCE.getJunctionElement().isSuperTypeOf(eClass)) {
            return new Dimension(-1, -1);
        }
        return IFigure.MAX_DIMENSION;
    }
    
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        return (child instanceof INonResizableEditPart) ? new NonResizableEditPolicy() : new ResizableEditPolicy();
    }
    
    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        // Return a command that can move and/or resize a Shape
        if(constraint instanceof Rectangle) {
            return new SetConstraintObjectCommand((IDiagramModelObject)child.getModel(), (Rectangle)constraint);
        }

        return null;
    }
    
    /*
     * This allows us to drag parts from a parent container to another.
     * This is the "add" counterpart to the "remove" Command created in GroupContainerEditPolicy.getOrphanChildrenCommand(GroupRequest);
     * 
     * If you don't want a part to be added, return null here.
     */
    @Override
    protected Command createAddCommand(EditPart childEditPart, Object constraint) {
        IDiagramModelContainer parent = (IDiagramModelContainer)getHost().getModel();
        IDiagramModelObject child = (IDiagramModelObject)childEditPart.getModel();
        
        // Keep within box
        Rectangle bounds = (Rectangle)constraint;
        if(bounds.x < 0) {
            bounds.x = 0;
        }
        if(bounds.y < 0) {
            bounds.y = 0;
        }
        
        return new AddObjectCommand(parent, child, bounds);
    }
    
    /**
     * AddObjectCommand
     */
    static class AddObjectCommand extends Command {
        IDiagramModelContainer fParent;
        IDiagramModelObject fChild;
        Rectangle fBounds;

        public AddObjectCommand(IDiagramModelContainer parent, IDiagramModelObject child, Rectangle bounds) {
            fParent = parent;
            fChild = child;
            fBounds = bounds.getCopy();
        }
        
        @Override
        public void execute() {
            fChild.setBounds(fBounds.x, fBounds.y, fBounds.width, fBounds.height);
            fParent.getChildren().add(fChild);
        }

        @Override
        public void undo() {
            fParent.getChildren().remove(fChild);
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fChild = null;
        }
    }
}

