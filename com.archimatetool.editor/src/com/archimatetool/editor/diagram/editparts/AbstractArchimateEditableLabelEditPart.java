/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.LabelDirectEditManager;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.policies.PartDirectEditTitlePolicy;


/**
 * Edit Part with connections and editable label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateEditableLabelEditPart
extends AbstractArchimateEditPart implements IColoredEditPart, ITextEditPart, ILinedEditPart {

    @Override
    protected void refreshFigure() {
        getFigure().refreshVisuals();
    }
    
    @Override
    public IDiagramModelObjectFigure getFigure() {
        return (IDiagramModelObjectFigure)super.getFigure();
    }
    
    @Override
    protected void createEditPolicies() {
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
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
        return new LabelDirectEditManager(this, getFigure().getTextControl());
    }

}