/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

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
            
            if(((GraphicalEditPart)getHost()).getFigure() instanceof ITargetFeedbackFigure figure) {
                figure.showTargetFeedback(true);
            }
        }
    }
    
    @Override
    public void eraseTargetFeedback(Request request) {
        if(((GraphicalEditPart)getHost()).getFigure() instanceof ITargetFeedbackFigure figure) {
            figure.showTargetFeedback(false);
        }
    }
}
