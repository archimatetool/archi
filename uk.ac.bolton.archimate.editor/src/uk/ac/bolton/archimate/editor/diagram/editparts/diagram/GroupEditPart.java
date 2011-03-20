/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.diagram;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import uk.ac.bolton.archimate.editor.diagram.directedit.LabelCellEditorLocator;
import uk.ac.bolton.archimate.editor.diagram.directedit.LabelDirectEditManager;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractBaseEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.SnapEditPartAdapter;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IEditableLabelFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.diagram.GroupFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ContainerHighlightEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.DiagramLayoutPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.GroupContainerComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartDirectEditTitlePolicy;
import uk.ac.bolton.archimate.editor.ui.ViewManager;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Group Edit Part
 * 
 * This is not a Connected EditPart because:
 * 1. A connection anchor point looks ugly if it enters at the top-right missing section of the figure
 * 2. The Magic Connector snaps to the edge of the figure rather than clicking in space
 * 
 * @author Phillip Beauvoir
 */
public class GroupEditPart extends AbstractBaseEditPart
implements IColoredEditPart, ITextEditPart {
    
    private DirectEditManager fDirectEditManager;

    private Adapter adapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            switch(msg.getEventType()) {
                // Children added or removed
                case Notification.ADD:
                case Notification.ADD_MANY:
                case Notification.REMOVE:
                case Notification.REMOVE_MANY:
                // Move notification sent from Z-Order changes in model
                case Notification.MOVE: 
                    refreshChildren();
                    break;

                case Notification.SET:
                    Object feature = msg.getFeature();
                    if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__BOUNDS) {
                        refreshBounds();
                    }
                    else {
                        refreshFigure();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    
    @Override
    protected List<?> getModelChildren() {
        return ((IDiagramModelContainer)getModel()).getChildren();
    }

    @Override
    protected Adapter getECoreAdapter() {
        return adapter;
    }

    @Override
    protected void createEditPolicies() {
        // Add a policy to handle directly editing the Parts (for example, directly renaming a part)
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new GroupContainerComponentEditPolicy());
        
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
    protected IFigure createFigure() {
        GroupFigure figure = new GroupFigure((IDiagramModelObject)getModel());
        return figure;
    }
    
    @Override
    public IEditableLabelFigure getFigure() {
        return (IEditableLabelFigure)super.getFigure();
    }
    
    @Override
    public IFigure getContentPane() {
        return ((IContainerFigure)getFigure()).getContentPane();
    }

    @Override
    protected void refreshFigure() {
        getFigure().refreshVisuals();
    }

    @Override
    public void performRequest(Request request) {
        // REQ_DIRECT_EDIT is Single-click when already selected or a Rename command
        // REQ_OPEN is Double-click
        if(request.getType() == RequestConstants.REQ_DIRECT_EDIT || request.getType() == RequestConstants.REQ_OPEN) {
            if(request instanceof LocationRequest) {
                // Edit the text control if we clicked on it
                if(getFigure().didClickLabel(((LocationRequest)request).getLocation().getCopy())) {
                    getDirectEditManager().show();
                }
                // Else open Properties View on double-click
                else if(request.getType() == RequestConstants.REQ_OPEN){
                    ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
                }
            }
            else {
                getDirectEditManager().show();
            }
        }
    }
    
    protected DirectEditManager getDirectEditManager() {
        if(fDirectEditManager == null) {
            fDirectEditManager = new LabelDirectEditManager(this, new LabelCellEditorLocator(getFigure().getLabel()),
                    getFigure().getLabel());
        }
        return fDirectEditManager;
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
