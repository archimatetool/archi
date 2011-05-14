/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.junctions;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.INonResizableEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.junctions.JunctionFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.ui.services.ViewManager;

/**
 * Junction Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class JunctionEditPart
extends AbstractArchimateEditPart
implements INonResizableEditPart {            
    
    protected ConnectionAnchor fAnchor;

    @Override
    protected IFigure createFigure() {
        return new JunctionFigure(getModel());
    }

    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        if(fAnchor == null) {
            fAnchor = new EllipseAnchor(getFigure());
        }
        return fAnchor;
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Add a policy to handle editing the Part (for example, deleting)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }
 
    @Override
    public void performRequest(Request request) {
        if(request.getType() == RequestConstants.REQ_OPEN) {
            // Show Properties View
            ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
        }
    }
    
    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
    }

}