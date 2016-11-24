/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Core EditPart for Archimate Relationship Connections
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateRelationshipEditPart extends DiagramConnectionEditPart 
implements NodeEditPart {
    
    public ArchimateRelationshipEditPart(Class<?> figureClass) {
        super(figureClass);
    }

    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        switch(msg.getEventType()) {
            // Children added or removed or moved
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
            case Notification.MOVE:
                if(feature == IArchimatePackage.Literals.CONNECTABLE__SOURCE_CONNECTIONS) {
                    refreshSourceConnections();
                }
                else if(feature == IArchimatePackage.Literals.CONNECTABLE__TARGET_CONNECTIONS) {
                    refreshTargetConnections();
                }
                break;
        }
        
        super.eCoreChanged(msg);
    }
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Hidden connections
        if(IPreferenceConstants.HIDDEN_RELATIONS_TYPES.equals(event.getProperty()) ||
                IPreferenceConstants.USE_NESTED_CONNECTIONS.equals(event.getProperty())) {
            refreshSourceConnections();
            refreshTargetConnections();
        }
        else {
            super.applicationPreferencesChanged(event);
        }
    }

    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    
    @Override
    public IDiagramModelArchimateConnection getModel() {
        return (IDiagramModelArchimateConnection)super.getModel();
    }

    @Override
    public void activate() {
		if(!isActive()) {
			super.activate();
			// Listen to Archimate Relationship changes
			getModel().getArchimateRelationship().eAdapters().add(getECoreAdapter());
		}
	}
	
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            getModel().getArchimateRelationship().eAdapters().remove(getECoreAdapter());
        }
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(getModel() != null && getModel().getArchimateRelationship() != null && adapter.isInstance(getModel().getArchimateRelationship())) {
            return getModel().getArchimateRelationship();
        }

        return super.getAdapter(adapter);
    }
    
    
    @Override
    public void createEditPolicies() {
        super.createEditPolicies();
        
        // Connection to connection
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ArchimateDiagramConnectionPolicy());
    }
    
    @Override
    protected List<IDiagramModelConnection> getModelSourceConnections() {
        return getModel().getSourceConnections();
    }

    @Override
    protected List<IDiagramModelConnection> getModelTargetConnections() {
        return getModel().getTargetConnections();
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return new LineConnectionAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return new LineConnectionAnchor(getFigure());
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        if(request instanceof ReconnectRequest) {
            return null;
        }
        return new LineConnectionAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        if(request instanceof ReconnectRequest) {
            return null;
        }
        return new LineConnectionAnchor(getFigure());
    }

}
