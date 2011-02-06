/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import uk.ac.bolton.archimate.editor.diagram.directedit.LabelCellEditorLocator;
import uk.ac.bolton.archimate.editor.diagram.directedit.LabelDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IEditableLabelFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartDirectEditTitlePolicy;

/**
 * Edit Part with connections and editable label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateEditableLabelEditPart
extends AbstractArchimateEditPart implements IColoredEditPart, ITextEditPart {

    protected DirectEditManager fDirectEditManager;
    
    protected ConnectionAnchor fAnchor;
    
    @Override
    protected ConnectionAnchor getConnectionAnchor() {
        if(fAnchor == null) {
            fAnchor = new ChopboxAnchor(getFigure());
        }
        return fAnchor;
    }
    
    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        if(getFigure() instanceof IDiagramModelObjectFigure) {
            ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
        }
    }
    
    @Override
    protected void createEditPolicies() {
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }
    
    /** 
     * Edit Requests are handled here
     */
    @Override
    public void performRequest(Request request) {
        if(request.getType() == RequestConstants.REQ_OPEN) {
            // Edit the label if we clicked on it
            if(((IEditableLabelFigure)getFigure()).didClickLabel(((LocationRequest)request).getLocation().getCopy())) {
                if(fDirectEditManager == null) {
                    Label label = ((IEditableLabelFigure)getFigure()).getLabel();
                    fDirectEditManager = new LabelDirectEditManager(this, new LabelCellEditorLocator(label), label);
                }
                fDirectEditManager.show();
            }
            else {
                handleEditRequest(request); 
            }
        }
    }

    protected abstract void handleEditRequest(Request request);
}