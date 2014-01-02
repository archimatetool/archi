/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import org.eclipse.gef.EditPolicy;

import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.diagram.sketch.policies.SketchConnectionPolicy;
import com.archimatetool.editor.diagram.sketch.policies.SketchDNDEditPolicy;



/**
 * Sketch Group Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchGroupEditPart extends GroupEditPart {
    
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Install our own connection policy
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SketchConnectionPolicy());

        // Install our own DND policy
        installEditPolicy("DND", new SketchDNDEditPolicy()); //$NON-NLS-1$
    }

}
