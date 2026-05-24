/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
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
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.MarqueeBlockFigure;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IAdapter;
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
    
    private boolean useCreationFigure = false;
    private boolean useResizeFigure = false;

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Rectangle bounds = (Rectangle)getConstraintFor(request);
        return new CreateDiagramObjectCommand(getHost(), request, bounds);
    }
    
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        if(isChildEditPartLocked(child)) {
            return new LockedResizableEditPolicy();
        }
        
        if(child instanceof INonResizableEditPart) {
            return new NonResizableEditPolicy() {
                @Override
                protected IFigure createDragSourceFeedbackFigure() {
                    return createChildDragSourceFeedbackFigure(child);
                }
            };
        }
        
        if(child instanceof IConstrainedSizeEditPart) {
            return new ConstrainedResizableEditPolicy();
        }
        
        return new ResizableEditPolicy() {
            @Override
            protected IFigure createDragSourceFeedbackFigure() {
                return createChildDragSourceFeedbackFigure(child);
            }
        };
    }
    
    @Override
    protected IFigure createSizeOnDropFeedback(CreateRequest createRequest) {
        IFigure figure = null;
        
        if(useCreationFigure && createRequest.getNewObject() instanceof IDiagramModelObject dmo) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(dmo);
            if(provider != null) {
                GraphicalEditPart editPart = (GraphicalEditPart)provider.createEditPart();
                editPart.setModel(dmo);
                dmo.setAdapter(IArchiveManager.class, ((IAdapter)getHost().getModel()).getAdapter(IArchiveManager.class));
                figure = editPart.getFigure();
                ((IDiagramModelObjectFigure)figure).refreshVisuals();
            }
        }
        
        if(figure == null) {
            figure = new MarqueeBlockFigure();
        }
        
        addFeedback(figure);
        return figure;
    }
    
    protected IFigure createChildDragSourceFeedbackFigure(EditPart child) {
        IFigure figure = null;
        
        if(useResizeFigure && child.getModel() instanceof IDiagramModelObject dmo) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(dmo);
            if(provider != null) {
                IDiagramModelObject dmoCopy = (IDiagramModelObject)dmo.getCopy();
                dmoCopy.setAdapter(IArchiveManager.class, dmo.getAdapter(IArchiveManager.class));
                dmoCopy.setAlpha(Math.min(120, dmo.getAlpha()));
                dmoCopy.setLineAlpha(Math.min(120, dmo.getLineAlpha()));
                dmoCopy.setFontColor("#bbbbbb"); //$NON-NLS-1$
                dmoCopy.setIconColor("#bbbbbb"); //$NON-NLS-1$
                //dmoCopy.setName("");
                
                GraphicalEditPart editPart = (GraphicalEditPart)provider.createEditPart();
                editPart.setModel(dmoCopy);
                figure = editPart.getFigure();
                ((IDiagramModelObjectFigure)figure).refreshVisuals();
            }
        }
        
        if(figure == null) {
            figure = new MarqueeBlockFigure();
        }
        
        addFeedback(figure);
        return figure;
    }
    
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        // If child part is locked, limit movement
        if(isChildEditPartLocked(child)) {
            return null;
        }

        // Return a command that can move and/or resize a child
        if(constraint instanceof Rectangle rect) {
            return new SetConstraintObjectCommand(request, (IDiagramModelObject)child.getModel(), rect);
        }

        return null;
    }
    
    /**
     * @param child
     * @return True id child EditPart is locked
     */
    protected boolean isChildEditPartLocked(EditPart child) {
        return child.getModel() instanceof ILockable lockable && lockable.isLocked();
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
    public class AddObjectCommand extends Command {
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
            doExecute();
            // Add to selected parts
            selectEditPart();
        }

        @Override
        public void undo() {
            parent.getChildren().remove(child);
        }
        
        @Override
        public void redo() {
            doExecute();
            // Don't add to selected parts because user might have selected other objects since execute and undo
        }
        
        protected void doExecute() {
            child.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
            parent.getChildren().add(child);
        }
        
        protected void selectEditPart() {
            if(getHost().getViewer().getEditPartRegistry().get(child) instanceof EditPart editPart) {
                getHost().getViewer().appendSelection(editPart);
            }
        }
        
        @Override
        public void dispose() {
            parent = null;
            child = null;
        }
    }
}

