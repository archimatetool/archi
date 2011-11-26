/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.dnd;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;


/**
 * A policy to handle a Native DND commands on an IDiagramModelContainer or IDiagramModel 
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDNDEditPolicy extends AbstractEditPolicy {
    
    // Cache these values for speed
    private IDiagramModelContainer fTargetContainer;
    private IDiagramModel fTargetDiagramModel;
    
    @Override
    public EditPart getTargetEditPart(Request request) {
        // We support Native DND
        if(request.getType() == DiagramDropRequest.REQ_DIAGRAM_DROP) {
            return getHost();
        }
        
        return null;
    }
    
    @Override
    public Command getCommand(Request request) {
        // We support Native DND
        if(request.getType() == DiagramDropRequest.REQ_DIAGRAM_DROP) {
            return getDropCommand((DiagramDropRequest)request);
        }
        
        return null;
    }
    
    /**
     * @param request
     * @return A command for when a native drop event occurs on the Diagram
     */
    protected abstract Command getDropCommand(DiagramDropRequest request);
    
    /**
     * @return The target Container on which the drop occurred
     */
    protected IDiagramModelContainer getTargetContainer() {
        if(fTargetContainer == null) {
            fTargetContainer = (IDiagramModelContainer)getHost().getModel();
        }
        return fTargetContainer;
    }
    
    /**
     * @return The target parent Diagram Model
     */
    protected IDiagramModel getTargetDiagramModel() {
        if(fTargetDiagramModel == null) {
            fTargetDiagramModel = getTargetContainer().getDiagramModel();
        }
        return fTargetDiagramModel;
    }
    
    /**
     * Return the actual drop location
     * @param request
     * @return the actual drop location
     */
    protected Point getDropLocation(DiagramDropRequest request) {
        // XY drop point
        Point pt = request.getDropLocation();

        // Translate drop point
        IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
        if(figure instanceof IContainerFigure) {
            ((IContainerFigure)figure).translateMousePointToRelative(pt);
        }
        // Translate to relative content pane
        else {
            IFigure contentPane = ((GraphicalEditPart)getHost()).getContentPane();
            contentPane.translateToRelative(pt);
        }
        
        return pt;
    }
}
