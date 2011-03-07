/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

import org.eclipse.gef.EditPolicy;

import uk.ac.bolton.archimate.editor.diagram.editparts.DiagramPart;
import uk.ac.bolton.archimate.editor.diagram.sketch.policies.SketchContainerComponentEditPolicy;



/**
 * Sketch Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchDiagramPart extends DiagramPart {
    
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Install a Custom Sketch policy for DND support
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new SketchContainerComponentEditPolicy());
    }
}
