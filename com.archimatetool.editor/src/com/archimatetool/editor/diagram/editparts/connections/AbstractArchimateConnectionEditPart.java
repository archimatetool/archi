/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ReconnectRequest;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.util.DerivedRelationsUtils;


/**
 * Abstract class for all implementations of Archimate Connection parts
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionEditPart extends AbstractDiagramConnectionEditPart 
implements IArchimateConnectionEditPart, NodeEditPart {
    
    private IArchimateModel fArchimateModel;
    
    /**
     * Add an additional adapter to listen to *all* model changes to refresh Structural color.
     */
    private Adapter fModelAdapter = new EContentAdapter() {
        @Override
        public void notifyChanged(Notification msg) {
            super.notifyChanged(msg);
            
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.ARCHIMATE_RELATIONSHIP__SOURCE
                                                    || feature == IArchimatePackage.Literals.ARCHIMATE_RELATIONSHIP__TARGET
                                                    || feature == IArchimatePackage.Literals.FOLDER__ELEMENTS) {
                showStructural();
            }
        }
    };
    
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
    
    /**
     * Listen to user toggling on/off show structural chains
     */
    private PropertyChangeListener propertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName() == IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN) {
                registerStructural();
            }
        }
    };
    
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
			// Store this
            fArchimateModel = getModel().getDiagramModel().getArchimateModel();
			// Listen to Archimate Relationship changes
			getModel().getArchimateRelationship().eAdapters().add(getECoreAdapter());
			// Register to listen to overall model changes that affect the structural relationship chains
			if(isShowStructural()) {
	            fArchimateModel.eAdapters().add(fModelAdapter);
	        }
			// Listen to Viewer Property changes for "Show Structural Chains"
			getViewer().addPropertyChangeListener(propertyListener);
		}
	}
	
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            getModel().getArchimateRelationship().eAdapters().remove(getECoreAdapter());
            fArchimateModel.eAdapters().remove(fModelAdapter);
            getViewer().removePropertyChangeListener(propertyListener);
        }
    }
    
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        if(isShowStructural()) {
            showStructural();
        }
    }
    
    /**
     * Register Model Listener to update Structural Chains
     */
    protected void registerStructural() {
        if(isShowStructural()) {
            fArchimateModel.eAdapters().add(fModelAdapter);
            showStructural();
        }
        else {
            fArchimateModel.eAdapters().remove(fModelAdapter);
            clearStructural();
        }
    }
    
    protected boolean isShowStructural() {
        return Boolean.TRUE.equals(getViewer().getProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN));
    }
    
    protected void showStructural() {
        IArchimateRelationship relation = getModel().getArchimateRelationship();
        boolean doHighlight = DerivedRelationsUtils.isInDerivedChain(relation);
        getFigure().highlight(doHighlight);
    }
    
    protected void clearStructural() {
        getFigure().highlight(false);
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
        return getFilteredModelSourceConnections();
    }

    @Override
    protected List<IDiagramModelConnection> getModelTargetConnections() {
        return getFilteredModelTargetConnections();
    }

    public List<IDiagramModelConnection> getFilteredModelSourceConnections() {
        return getModel().getSourceConnections();
    }
    
    public List<IDiagramModelConnection> getFilteredModelTargetConnections() {
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
