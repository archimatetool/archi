/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.LabelDirectEditManager;
import com.archimatetool.editor.diagram.figures.IContainerFigure;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.policies.ArchimateContainerEditPolicy;
import com.archimatetool.editor.diagram.policies.ArchimateContainerLayoutPolicy;
import com.archimatetool.editor.diagram.policies.ArchimateDNDEditPolicy;
import com.archimatetool.editor.diagram.policies.ContainerHighlightEditPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.policies.PartDirectEditTitlePolicy;


/**
 * Edit Part with connections and editable text flow control
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateEditableTextFlowEditPart
extends AbstractArchimateEditPart implements IColoredEditPart, ITextAlignedEditPart, ILinedEditPart {
    
    @Override
    protected void refreshFigure() {
        getFigure().refreshVisuals();
    }
    
    @Override
    public IFigure getContentPane() {
        return ((IContainerFigure)getFigure()).getContentPane();
    }
    
    @Override
    public IDiagramModelObjectFigure getFigure() {
        return (IDiagramModelObjectFigure)super.getFigure();
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle deletion and orphaning
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
        
        // Add a policy for Native DND
        installEditPolicy("DND", new ArchimateDNDEditPolicy()); //$NON-NLS-1$
        
        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ArchimateContainerLayoutPolicy());
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ArchimateContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$

        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
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
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        return super.getAdapter(adapter);
    }

}