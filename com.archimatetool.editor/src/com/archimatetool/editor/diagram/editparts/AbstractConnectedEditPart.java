/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Abstract Edit Part with connections
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractConnectedEditPart
extends AbstractBaseEditPart
implements NodeEditPart {
    
    private Adapter adapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            eCoreChanged(msg);
        }
    };
    
    /**
     * Message from the ECore Adapter
     * @param msg
     */
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();

        switch(msg.getEventType()) {
            // Children added or removed or moved
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
            case Notification.MOVE:
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS) {
                    refreshSourceConnections();
                }
                else if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__TARGET_CONNECTIONS) {
                    refreshTargetConnections();
                }
                else {
                    refreshChildren();
                }
                break;

            case Notification.SET:
                // Bounds
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__BOUNDS) {
                    refreshBounds();
                }
                // Locked
                else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                    updateEditPolicies(); // Update Edit Policies of this and parent
                    if(getParent() instanceof AbstractDiagramPart) {
                        ((AbstractDiagramPart)getParent()).updateEditPolicies();
                    }
                    else if(getParent() instanceof AbstractBaseEditPart) {
                        ((AbstractBaseEditPart)getParent()).updateEditPolicies();
                    }
                }
                else {
                    refreshFigure();
                }
                break;

            default:
                break;
        }
    }
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR.equals(event.getProperty())) {
            refreshConnectionAnchors();
        }
        
        super.applicationPreferencesChanged(event);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return adapter;
    }
    
    @Override
    public IDiagramModelObject getModel() {
        return (IDiagramModelObject)super.getModel();
    }

    @Override
    protected List<IDiagramModelConnection> getModelSourceConnections() {
        return getFilteredModelSourceConnections();
    }

    @Override
    protected List<IDiagramModelConnection> getModelTargetConnections() {
        return getFilteredModelTargetConnections();
    }
    
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
    	if(Preferences.STORE.getBoolean(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR)) {
    	    return new OrthogonalAnchor(getFigure(), connection, true);
    	}
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
    	if(Preferences.STORE.getBoolean(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR)) {
    		return new OrthogonalAnchor(getFigure(), connection, false);
    	}
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
    	if(Preferences.STORE.getBoolean(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR)) {
    	    return new OrthogonalAnchor(getFigure(), request, true);
    	}
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
    	if(Preferences.STORE.getBoolean(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR)) {
    	    return new OrthogonalAnchor(getFigure(), request, false);
    	}
    	return getDefaultConnectionAnchor();
    }
    
    /**
     * @return The default connection anchor to use for source and target connections
     *         Default is a Chopbox connection anchor
     */
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        return new ChopboxAnchor(getFigure());
    }
    
    /**
     * Refresh the connection anchors to return updated ones
     */
    protected void refreshConnectionAnchors() {
        for(Object editPart : getSourceConnections()) {
            ((EditPart)editPart).refresh();
        }
        for(Object editPart : getTargetConnections()) {
            ((EditPart)editPart).refresh();
        }
    }
    
    // =================================== Filtering ====================================================
    
    public List<IDiagramModelConnection> getFilteredModelSourceConnections() {
        return getFilteredConnections(getModel().getSourceConnections());
    }
    
    public List<IDiagramModelConnection> getFilteredModelTargetConnections() {
        return getFilteredConnections(getModel().getTargetConnections());
    }
    
    /**
     * See if any connections are filtered out
     * @param originalList
     * @return A list of filtered connections
     */
    private List<IDiagramModelConnection> getFilteredConnections(List<IDiagramModelConnection> originalList) {
        IConnectionEditPartFilter[] filters = getEditPartFilterProvider().getEditPartFilters(IConnectionEditPartFilter.class);
        if(filters != null) {
            List<IDiagramModelConnection> filteredList = new ArrayList<IDiagramModelConnection>();
            
            for(IDiagramModelConnection connection : originalList) {
                boolean add = true;
                
                for(IConnectionEditPartFilter filter : filters) {
                    add = filter.isConnectionVisible(this, connection);
                    
                    if(!add) { // no point in trying the next filter
                        break;
                    }
                }
                
                if(add) {
                    filteredList.add(connection);
                }
            }
            
            return filteredList;
        }
        
        return originalList;
    }
}