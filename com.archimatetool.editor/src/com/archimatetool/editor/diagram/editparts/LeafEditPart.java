/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;


/**
 * Edit Part that is not a container, cannot be resized, and doesn't use Orthogonal Anchor
 * 
 * @author Phillip Beauvoir
 */
public class LeafEditPart
extends AbstractArchimateElementEditPart
implements INonResizableEditPart {            
    
    public LeafEditPart(Class<?> figureClass) {
        super(figureClass);
    }
    
    @Override
    protected boolean canUseOrthogonalAnchor() {
        return false;
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
            showPropertiesView();
        }
    }

}