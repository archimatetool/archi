/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.editparts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;

import uk.ac.bolton.archimate.canvas.figures.CanvasBlockFigure;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelBlock;
import uk.ac.bolton.archimate.canvas.model.ICanvasPackage;
import uk.ac.bolton.archimate.canvas.policies.CanvasBlockLayoutPolicy;
import uk.ac.bolton.archimate.canvas.policies.CanvasConnectionPolicy;
import uk.ac.bolton.archimate.canvas.policies.CanvasDNDEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.directedit.MultiLineTextDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractConnectedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextPositionedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.SnapEditPartAdapter;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ContainerHighlightEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Canvas Block Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockEditPart extends AbstractConnectedEditPart
implements IColoredEditPart, ITextAlignedEditPart, ITextPositionedEditPart {
    
    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        switch(msg.getEventType()) {
            case Notification.SET:
                // Refresh Icon
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH
                            || feature == ICanvasPackage.Literals.ICONIC__IMAGE_POSITION) {
                    ((CanvasBlockFigure)getFigure()).updateImage();
                }
                else {
                    super.eCoreChanged(msg);
                }
                
                break;

            default:
                super.eCoreChanged(msg);
        }
    }

    @Override
    protected List<?> getModelChildren() {
        return getModel().getChildren();
    }

    @Override
    protected IFigure createFigure() {
        return new CanvasBlockFigure(getModel());
    }

    @Override
    public ICanvasModelBlock getModel() {
        return (ICanvasModelBlock)super.getModel();
    }
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CanvasConnectionPolicy());

        // Add a policy to handle deletion and orphaning
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
        
        // Install a policy for DND support
        installEditPolicy("DND", new CanvasDNDEditPolicy());

        // Add these
        updateEditPolicies();
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$

        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
    }
    
    @Override
    public void updateEditPolicies() {
        // Add a policy to handle directly editing the Part
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, isLocked() ? null : new CanvasBlockDirectEditTitlePolicy());

        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new CanvasBlockLayoutPolicy());
    }

    @Override
    public IFigure getContentPane() {
        return ((IContainerFigure)getFigure()).getContentPane();
    }
    
    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        return super.getAdapter(adapter);
    }
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_DIRECT_EDIT && !getModel().isLocked()) {
            createDirectEditManager().show();
        }
        else if(req.getType() == RequestConstants.REQ_OPEN) {
            // Show Properties view
            showPropertiesView();
        }
    }

    protected DirectEditManager createDirectEditManager() {
        return new MultiLineTextDirectEditManager(this);
    }
    
    /**
     * DirectEditPolicy
     */
    private class CanvasBlockDirectEditTitlePolicy extends DirectEditPolicy {
        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            String content = (String)request.getCellEditor().getValue();
            return new EObjectFeatureCommand("Content", getModel(),
                        IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, content);
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
        }
    }
}
