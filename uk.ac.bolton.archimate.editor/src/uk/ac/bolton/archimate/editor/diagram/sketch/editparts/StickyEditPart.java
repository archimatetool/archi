/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

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

import uk.ac.bolton.archimate.editor.diagram.directedit.MultiLineTextDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractConnectedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.SnapEditPartAdapter;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ContainerHighlightEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.DiagramLayoutPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.sketch.figures.StickyFigure;
import uk.ac.bolton.archimate.editor.diagram.sketch.policies.SketchConnectionPolicy;
import uk.ac.bolton.archimate.editor.diagram.sketch.policies.SketchDNDEditPolicy;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.ISketchModelSticky;


/**
 * Sticky Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class StickyEditPart extends AbstractConnectedEditPart
implements IColoredEditPart, ITextAlignedEditPart  {
    
    private DirectEditManager fDirectEditManager;

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
        installEditPolicy("DND", new SketchDNDEditPolicy());
        
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
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
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
            getDirectEditManager().show();
        }
        else if(req.getType() == RequestConstants.REQ_OPEN) {
            // Show Properties view
            showPropertiesView();
        }
    }

    protected DirectEditManager getDirectEditManager() {
        if(fDirectEditManager == null) {
            fDirectEditManager = new MultiLineTextDirectEditManager(this);
        }
        return fDirectEditManager;
    }
    
    /**
     * DirectEditPolicy
     */
    private class StickyDirectEditTitlePolicy extends DirectEditPolicy {
        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            String content = (String)request.getCellEditor().getValue();
            return new EObjectFeatureCommand("Content", getModel(),
                    IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, content);
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
        }
    }
}
