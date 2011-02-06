/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import uk.ac.bolton.archimate.editor.diagram.directedit.LabelDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.directedit.TextFlowCellEditorLocator;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IEditableTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.ArchimateContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ArchimateContainerLayoutPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ContainerHighlightEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.GroupContainerComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartDirectEditTitlePolicy;
import uk.ac.bolton.archimate.editor.ui.ViewManager;

/**
 * Edit Part with connections and editable text flow control
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateEditableTextFlowEditPart
extends AbstractArchimateEditPart implements IColoredEditPart, ITextAlignedEditPart {
    
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
    protected List<?> getModelChildren() {
        return getModel().getChildren();
    }
    
    @Override
    public IFigure getContentPane() {
        return ((IContainerFigure)getFigure()).getContentPane();
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new GroupContainerComponentEditPolicy());
        
        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ArchimateContainerLayoutPolicy());
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ArchimateContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$

        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
    }
    
    /** 
     * Edit Requests are handled here
     */
    @Override
    public void performRequest(Request request) {
        if(request.getType() == RequestConstants.REQ_OPEN) {
            // Edit the textflow if we clicked on it
            IEditableTextFlowFigure figure = (IEditableTextFlowFigure)getFigure();
            if(figure.didClickTextControl(((LocationRequest)request).getLocation().getCopy())) {
                if(fDirectEditManager == null) {
                    fDirectEditManager = new LabelDirectEditManager(this, new TextFlowCellEditorLocator(figure.getTextControl()),
                            figure.getTextControl());
                }
                fDirectEditManager.show();
            }
            else {
                handleEditRequest(request); 
            }
        }
    }
    
    protected void handleEditRequest(Request request) {
        // Show Properties View
        ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        return super.getAdapter(adapter);
    }

}