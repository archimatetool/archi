/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ContainerComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.DiagramLayoutPolicy;
import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;



/**
 * Main Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class DiagramPart extends AbstractGraphicalEditPart {
    
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
                    // Connection Router Type
                    if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE) {
                        refreshVisuals();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty() == IPreferenceConstants.ANTI_ALIAS) {
                setAntiAlias();
                refresh();
            }
        }
    };
    
    @Override
    protected List<?> getModelChildren() {
        return ((IDiagramModel)getModel()).getChildren();
    }

    @Override
    public void activate() {
        if(isActive()) {
            return;
        }
        super.activate();
        
        ((IDiagramModel)getModel()).eAdapters().add(adapter);
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);
    }

    @Override
    public void deactivate() {
        if(!isActive()) {
            return;
        }
        super.deactivate();
        
        ((IDiagramModel)getModel()).eAdapters().remove(adapter);
        
        Preferences.STORE.removePropertyChangeListener(prefsListener);
    }

    @Override
    protected void createEditPolicies() {
        // Install a custom layout policy that handles dragging things around
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
        
        // Install a policy for DND support and other things
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ContainerComponentEditPolicy());
        
        // And we need to install this Group Container Policy here as well as in the GroupEditpart
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    }

    @Override
    protected IFigure createFigure() {
        FreeformLayer figure = new FreeformLayer();
        
        // Provide an edge when in negative space
        figure.setBorder(new MarginBorder(5));
        
        figure.setLayoutManager(new FreeformLayout());
        
        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(figure);
        
        // Anti-aliasing
        setAntiAlias();

        return figure;
    }
    
    @Override
    public void refreshVisuals() {
        // Set Connection Router type for the whole diagram
        
        // For animation to work on connections also set addRoutingListener(RoutingAnimator.getDefault());
        // on the connection figures - see AbstractConnectionFigure
        if(AnimationUtil.doAnimate()) {
            Animation.markBegin();
        }
        
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        
        switch(((IDiagramModel)getModel()).getConnectionRouterType()) {
            case IDiagramModel.CONNECTION_ROUTER_BENDPOINT:
                AutomaticRouter router = new FanRouter();
                router.setNextRouter(new BendpointConnectionRouter());
                cLayer.setConnectionRouter(router);
                break;
                
            case IDiagramModel.CONNECTION_ROUTER_SHORTEST_PATH:
                router = new FanRouter();
                router.setNextRouter(new ShortestPathConnectionRouter(getFigure()));
                cLayer.setConnectionRouter(router);
                break;
                
            case IDiagramModel.CONNECTION_ROUTER_MANHATTAN:
                cLayer.setConnectionRouter(new ManhattanConnectionRouter());
                break;
        }
        
        if(AnimationUtil.doAnimate()) {
            Animation.run(AnimationUtil.animationSpeed());
        }
    }
    
    private void setAntiAlias() {
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        
        // Anti-aliasing
        cLayer.setAntialias(Preferences.useAntiAliasing() ? SWT.ON : SWT.DEFAULT);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        if(adapter == IDiagramModel.class) {
            return getModel();
        }
        
        return super.getAdapter(adapter);
    }
    
}
