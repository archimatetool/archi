/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.editparts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import uk.ac.bolton.archimate.canvas.policies.CanvasDNDEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractDiagramPart;
import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.DiagramLayoutPolicy;



/**
 * Canvas Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class CanvasDiagramPart extends AbstractDiagramPart {
    
    @Override
    protected void createEditPolicies() {
        updateEditPolicies();
        
        // Install a policy for DND support
        installEditPolicy("DND", new CanvasDNDEditPolicy());
        
        // And we need to install this Group Container Policy here as well as in the GroupEditpart
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    }
    
    @Override
    public void updateEditPolicies() {
        // Install a custom layout policy that handles dragging things around
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
    }
}
