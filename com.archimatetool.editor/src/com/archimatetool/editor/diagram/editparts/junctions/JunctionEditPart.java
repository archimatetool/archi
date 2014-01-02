/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.junctions;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditPart;
import com.archimatetool.editor.diagram.editparts.INonResizableEditPart;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.junctions.JunctionFigure;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;


/**
 * Junction Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class JunctionEditPart
extends AbstractArchimateEditPart
implements INonResizableEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new JunctionFigure(getModel());
    }

    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        return new EllipseAnchor(getFigure());
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
    
    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
    }

}