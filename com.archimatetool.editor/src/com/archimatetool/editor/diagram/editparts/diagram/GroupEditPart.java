/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.MultiLineTextDirectEditManager;
import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.editparts.SnapEditPartAdapter;
import com.archimatetool.editor.diagram.figures.IContainerFigure;
import com.archimatetool.editor.diagram.figures.diagram.GroupFigure;
import com.archimatetool.editor.diagram.policies.ArchimateDNDEditPolicy;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramLayoutPolicy;
import com.archimatetool.editor.diagram.policies.BasicContainerEditPolicy;
import com.archimatetool.editor.diagram.policies.ContainerHighlightEditPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.policies.PartDirectEditTitlePolicy;



/**
 * Group Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class GroupEditPart extends AbstractConnectedEditPart {
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ArchimateDiagramConnectionPolicy());

        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (Deleting and Orphaning)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
        
        // Add a policy for Native DND
        installEditPolicy("DND", new ArchimateDNDEditPolicy()); //$NON-NLS-1$
        
        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ArchimateDiagramLayoutPolicy());
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$

        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        GroupFigure figure = new GroupFigure(getModel());
        return figure;
    }
    
    @Override
    public IFigure getContentPane() {
        return ((IContainerFigure)getFigure()).getContentPane();
    }

    @Override
    protected void refreshFigure() {
        // Refresh the figure if necessary
        getFigure().refreshVisuals();
    }

    /** 
     * Edit Requests are handled here
     */
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
        return new MultiLineTextDirectEditManager(this, true);
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
