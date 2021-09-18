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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.util.LightweightEContentAdapter;




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
    
    private Adapter adapter = new LightweightEContentAdapter(this::eCoreChanged, IFeature.class);
    
    /**
     * Message from the ECore Adapter
     * @param msg
     */
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // Archi Features
        if(IFeatures.isFeatureNotification(msg)) {
            refreshVisuals();
            return;
        }

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
                // Connection Router Type
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE) {
                    setConnectionRouter(true);
                }
                break;

            default:
                break;
        }
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return adapter;
    }
    
    /**
     * Application User Preferences were changed
     */
    @Override
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
    public void deactivate() {
        if(!isActive()) {
            return;
        }
        
        super.deactivate();
        
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
        // Causes snap to grid problems when in negative space
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
        setConnectionRouter(false);
    }
    
    /**
     * Set Connection Router type for the whole diagram
     */
    protected void setConnectionRouter(boolean withAnimation) {
        // Animation
        if(withAnimation && AnimationUtil.doAnimate()) {
            Animation.markBegin();
        }

        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);

        switch(getModel().getConnectionRouterType()) {
            case IDiagramModel.CONNECTION_ROUTER_MANHATTAN:
                cLayer.setConnectionRouter(new ManhattanConnectionRouter());
                break;
            
            case IDiagramModel.CONNECTION_ROUTER_BENDPOINT:
            default:
                AutomaticRouter router = new FanRouter();
                router.setNextRouter(new BendpointConnectionRouter());
                cLayer.setConnectionRouter(router);
                break;
        }

        if(withAnimation && AnimationUtil.doAnimate()) {
            Animation.run(AnimationUtil.animationSpeed());
        }
    }
    
    protected void setAntiAlias() {
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        
        // Anti-aliasing
        cLayer.setAntialias(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.ANTI_ALIAS) ? SWT.ON : SWT.DEFAULT);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == SnapToHelper.class) {
            return new SnapEditPartAdapter(this).getSnapToHelper();
        }
        
        if(getModel() != null && adapter.isInstance(getModel())) {
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
