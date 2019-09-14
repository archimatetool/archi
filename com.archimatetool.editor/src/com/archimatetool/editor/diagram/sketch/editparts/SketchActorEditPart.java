/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.MultiLineTextDirectEditManager;
import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.policies.PartDirectEditTitlePolicy;
import com.archimatetool.editor.diagram.sketch.figures.SketchActorFigure;
import com.archimatetool.editor.diagram.sketch.policies.SketchConnectionPolicy;
import com.archimatetool.model.ISketchModelActor;



/**
 * Sketch Actor Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchActorEditPart extends AbstractConnectedEditPart {
    
    @Override
    protected IFigure createFigure() {
        SketchActorFigure figure = new SketchActorFigure((ISketchModelActor)getModel());
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SketchConnectionPolicy());
        
        // Add a policy to handle directly editing the Part
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }

    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        getFigure().refreshVisuals();
    }
    
    @Override
    public void performRequest(Request request) {
        // REQ_DIRECT_EDIT is Single-click when already selected or a Rename command
        // REQ_OPEN is Double-click
        if(request.getType() == RequestConstants.REQ_DIRECT_EDIT || request.getType() == RequestConstants.REQ_OPEN) {
            if(request instanceof LocationRequest) {
                // Edit the text control if we clicked on it
                if(getFigure().didClickTextControl(((LocationRequest)request).getLocation().getCopy())) {
                    createDirectEditManager().show();
                }
                // Else open Properties View on double-click
                else if(request.getType() == RequestConstants.REQ_OPEN){
                    showPropertiesView();
                }
            }
            else {
                createDirectEditManager().show();
            }
        }
    }
    
    protected DirectEditManager createDirectEditManager() {
        //return new LabelDirectEditManager(this, getFigure().getTextControl(), getModel().getName());
        return new MultiLineTextDirectEditManager(this, true, getFigure().getTextControl());
    }
}
