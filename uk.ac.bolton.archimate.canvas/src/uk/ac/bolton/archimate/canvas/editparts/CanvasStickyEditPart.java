/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.editparts;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;

import uk.ac.bolton.archimate.canvas.figures.CanvasStickyFigure;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelSticky;
import uk.ac.bolton.archimate.canvas.model.ICanvasPackage;
import uk.ac.bolton.archimate.canvas.policies.CanvasConnectionPolicy;
import uk.ac.bolton.archimate.editor.diagram.directedit.MultiLineTextDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractConnectedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextPositionedEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Canvas Sticky Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class CanvasStickyEditPart extends AbstractConnectedEditPart
implements IColoredEditPart, ITextAlignedEditPart, ITextPositionedEditPart {
    
    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        switch(msg.getEventType()) {
            case Notification.SET:
                // Refresh Icon
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH
                            || feature == ICanvasPackage.Literals.ICONIC__IMAGE_POSITION) {
                    ((CanvasStickyFigure)getFigure()).updateImage();
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
    protected IFigure createFigure() {
        return new CanvasStickyFigure(getModel());
    }

    @Override
    public ICanvasModelSticky getModel() {
        return (ICanvasModelSticky)super.getModel();
    }

    @Override
    protected List<?> getModelChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CanvasConnectionPolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());

        updateEditPolicies();
    }
    
    @Override
    public void updateEditPolicies() {
        // Add a policy to handle directly editing the Part
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, isLocked() ? null : new CanvasStickyDirectEditTitlePolicy());
    }

    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_DIRECT_EDIT && !isLocked()) {
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

    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
    }

    /**
     * DirectEditPolicy
     */
    private class CanvasStickyDirectEditTitlePolicy extends DirectEditPolicy {
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
