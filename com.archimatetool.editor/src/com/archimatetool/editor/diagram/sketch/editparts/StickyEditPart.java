/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.MultiLineTextDirectEditManager;
import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.editparts.SnapEditPartAdapter;
import com.archimatetool.editor.diagram.figures.IContainerFigure;
import com.archimatetool.editor.diagram.policies.BasicContainerEditPolicy;
import com.archimatetool.editor.diagram.policies.ContainerHighlightEditPolicy;
import com.archimatetool.editor.diagram.policies.DiagramLayoutPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.sketch.figures.StickyFigure;
import com.archimatetool.editor.diagram.sketch.policies.SketchConnectionPolicy;
import com.archimatetool.editor.diagram.sketch.policies.SketchDNDEditPolicy;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.ISketchModelSticky;



/**
 * Sticky Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class StickyEditPart extends AbstractConnectedEditPart {
    
    @Override
    protected List<?> getModelChildren() {
        return ((IDiagramModelContainer)getModel()).getChildren();
    }

    @Override
    protected IFigure createFigure() {
        StickyFigure figure = new StickyFigure((ISketchModelSticky)getModel());
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SketchConnectionPolicy());
        
        // Add a policy to handle directly editing the Part
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new StickyDirectEditTitlePolicy());

        // Add a policy to handle deletion and orphaning
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
        
        // Add a DND Policy
        installEditPolicy("DND", new SketchDNDEditPolicy()); //$NON-NLS-1$
        
        // Install a custom layout policy that handles dragging things around and creating new objects
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
        
        // Orphaning
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$

        // Selection Feedback
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ContainerHighlightEditPolicy());
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

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        return super.getAdapter(adapter);
    }
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            createDirectEditManager().show();
        }
        else if(req.getType() == RequestConstants.REQ_OPEN) {
            // Show Properties view
            showPropertiesView();
        }
    }

    protected DirectEditManager createDirectEditManager() {
        return new MultiLineTextDirectEditManager(this);
    }
    
    /**
     * DirectEditPolicy
     */
    private class StickyDirectEditTitlePolicy extends DirectEditPolicy {
        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            String content = (String)request.getCellEditor().getValue();
            return new EObjectFeatureCommand(Messages.StickyEditPart_0, getModel(),
                    IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, content);
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
        }
    }
}
