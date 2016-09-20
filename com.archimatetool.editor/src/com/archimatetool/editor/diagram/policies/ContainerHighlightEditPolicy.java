/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

import com.archimatetool.editor.diagram.dnd.DiagramDropRequest;
import com.archimatetool.editor.diagram.figures.ITargetFeedbackFigure;


/**
 * 
 * Highlights a parent Edit Part when a child edit part can be added to it
 * 
 * @author Phillip Beauvoir
 */
public class ContainerHighlightEditPolicy extends GraphicalEditPolicy {

    @Override
    public void eraseTargetFeedback(Request request) {
        if(getFeedbackFigure() != null) {
            getFeedbackFigure().eraseTargetFeedback();
        }
    }

    @Override
    public EditPart getTargetEditPart(Request request) {
        if(request == null || request.getType() == null) {
            return null;
        }
        
        return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost() : null;
    }

    @Override
    public void showTargetFeedback(Request request) {
        if(request.getType().equals(RequestConstants.REQ_MOVE) 
                || request.getType().equals(RequestConstants.REQ_ADD)
                || request.getType().equals(RequestConstants.REQ_CREATE)
                || request.getType().equals(DiagramDropRequest.REQ_DIAGRAM_DROP)
                ) {
            
            if(getFeedbackFigure() != null) {
                getFeedbackFigure().showTargetFeedback();
            }
        }
    }

    private ITargetFeedbackFigure getFeedbackFigure() {
        IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
        return (figure instanceof ITargetFeedbackFigure) ? (ITargetFeedbackFigure)figure : null;
    }
}
