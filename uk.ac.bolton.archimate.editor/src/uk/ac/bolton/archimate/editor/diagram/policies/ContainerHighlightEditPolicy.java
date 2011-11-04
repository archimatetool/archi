/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

import uk.ac.bolton.archimate.editor.diagram.dnd.DiagramDropRequest;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;

/**
 * 
 * Highlights an Edit Part when you hover a mouse over it
 * 
 * @author Phillip Beauvoir
 */
public class ContainerHighlightEditPolicy extends GraphicalEditPolicy {

    @Override
    public void eraseTargetFeedback(Request request) {
        getContainerFigure().eraseTargetFeedback();
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
                || request.getType().equals(RequestConstants.REQ_RECONNECT_SOURCE)
                || request.getType().equals(RequestConstants.REQ_RECONNECT_TARGET)
                || request.getType().equals(RequestConstants.REQ_CONNECTION_START)
                || request.getType().equals(RequestConstants.REQ_CONNECTION_END)
                || request.getType().equals(RequestConstants.REQ_CREATE)
                || request.getType().equals(DiagramDropRequest.REQ_DIAGRAM_DROP)
                ) {
            
            getContainerFigure().showTargetFeedback();
        }
    }

    private IContainerFigure getContainerFigure() {
        return (IContainerFigure)((GraphicalEditPart)getHost()).getFigure();
    }
}
