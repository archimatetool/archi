/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IProperties;




/**
 * Abstract Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramPart extends AbstractFilteredEditPart
implements IEditPartFilterProvider {
    
    /**
     * EditPart Filters
     */
    private List<IEditPartFilter> fEditPartFilters;
    
    private Adapter adapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            eCoreChanged(msg);
        }
    };
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            applicationPreferencesChanged(event);
        }
    };
    
    /**
     * Message from the ECore Adapter
     * @param msg
     */
    protected void eCoreChanged(Notification msg) {
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
                // Viewpoint changed
                else if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                    refreshChildrenFigures();
                }
                break;

            default:
                break;
        }
    }
    
    /**
     * Application User Preferences were changed
     */
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if(event.getProperty() == IPreferenceConstants.ANTI_ALIAS) {
            setAntiAlias();
            refresh();
        }
    }

    /**
     * Refresh all child figures
     */
    protected void refreshChildrenFigures() {
        for(Object editPart : getChildren()) {
            if(editPart instanceof AbstractBaseEditPart) {
                ((AbstractBaseEditPart)editPart).refreshChildrenFigures();
            }
        }
    }

    @Override
    public IDiagramModel getModel() {
        return (IDiagramModel)super.getModel();
    }

    @Override
    public void activate() {
        if(isActive()) {
            return;
        }
        
        super.activate();
        
        // Listen to Model
        getModel().eAdapters().add(adapter);
        
        // Listen to Prefs changes
        Preferences.STORE.addPropertyChangeListener(prefsListener);
    }

    @Override
    public void deactivate() {
        if(!isActive()) {
            return;
        }
        
        super.deactivate();
        
        // Remove Model listener
        getModel().eAdapters().remove(adapter);
        
        // Remove Prefs listener
        Preferences.STORE.removePropertyChangeListener(prefsListener);
        
        // Clear Filters
        if(fEditPartFilters != null) {
            fEditPartFilters.clear();
            fEditPartFilters = null;
        }
    }

    /**
     * Update any Edit Parts that may need changing as a result of for example locking an Edit Part
     */
    public void updateEditPolicies() {
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
        
        switch(getModel().getConnectionRouterType()) {
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
    
    protected void setAntiAlias() {
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
        
        if(adapter == IDiagramModel.class || adapter == IProperties.class) {
            return getModel();
        }
        
        return super.getAdapter(adapter);
    }
    
    // -------------------------------- Filters -----------------------------------------
    
    @Override
    public void addEditPartFilter(IEditPartFilter filter) {
        if(fEditPartFilters == null) {
            fEditPartFilters = new ArrayList<IEditPartFilter>();
        }
        fEditPartFilters.add(filter);
    }
    
    @Override
    public void removeEditPartFilter(IEditPartFilter filter) {
        if(fEditPartFilters != null && filter != null) {
            fEditPartFilters.remove(filter);
            if(fEditPartFilters.isEmpty()) {
                fEditPartFilters = null;
            }
        }
    }
    
    @Override
    public IEditPartFilter[] getEditPartFilters() {
        if(fEditPartFilters == null) {
            return null;
        }
        IEditPartFilter[] result = new IEditPartFilter[fEditPartFilters.size()];
        fEditPartFilters.toArray(result);
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] getEditPartFilters(Class<T> T) {
        if(fEditPartFilters == null) {
            return null;
        }
        
        List<T> filteredList = new ArrayList<T>();
        
        for(Object filter : fEditPartFilters) {
            if(T.isInstance(filter)) {
                filteredList.add((T)filter);
            }
        }
        
        if(filteredList.isEmpty()) {
            return null;
        }
        
        T[] result = (T[])Array.newInstance(T, filteredList.size());
        return filteredList.toArray(result);
    }
}
