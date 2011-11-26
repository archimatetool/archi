/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.GroupEditPart;
import uk.ac.bolton.archimate.editor.diagram.sketch.policies.SketchDNDEditPolicy;


/**
 * Sketch Group Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchGroupEditPart extends GroupEditPart {
    
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Install our own DND policy
        installEditPolicy("DND", new SketchDNDEditPolicy());
    }

}
