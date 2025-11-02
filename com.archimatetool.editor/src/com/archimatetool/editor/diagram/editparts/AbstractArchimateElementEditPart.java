/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.util.LightweightEContentAdapter;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Abstract Archimate Edit Part with connections
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementEditPart extends AbstractConnectedEditPart {
    
    /**
     * Listen to the models' Profile changes so we can update the image
     */
    private Adapter adapter = new LightweightEContentAdapter(this::eCoreChanged, IProfile.class);
    
    /**
     * Model that we are listening to changes on
     * Store this model in case the selected object is deleted
     */
    private IArchimateModel fModel;
    
    protected AbstractArchimateElementEditPart() {
    }
    
    protected AbstractArchimateElementEditPart(Class<? extends IDiagramModelObjectFigure> figureClass) {
        super(figureClass);
    }

    @Override
    public IDiagramModelArchimateObject getModel() {
        return (IDiagramModelArchimateObject)super.getModel();
    }
    
    @Override
    protected void addECoreAdapter() {
        super.addECoreAdapter();
        
        // Listen to changes in Archimate Element
        getModel().getArchimateElement().eAdapters().add(getECoreAdapter());
        
        // Listen to changes in Archimate Model for Profile changes
        fModel = getModel().getArchimateModel();
        fModel.eAdapters().add(adapter);
    }
    
    @Override
    protected void removeECoreAdapter() {
        super.removeECoreAdapter();
        
        // Unlisten to changes in Archimate Element
        getModel().getArchimateElement().eAdapters().remove(getECoreAdapter());
        
        // Unlisten to changes in Archimate Model
        if(fModel != null) {
            fModel.eAdapters().remove(adapter);
            fModel = null;
        }
    }
    
    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // Junction connection arrow/blob heads visible or hidden
        if(IFeatures.isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS)) {
            for(Object o : getTargetConnections()) {
                ((EditPart)o).refresh();
            }
            for(Object o : getSourceConnections()) {
                ((EditPart)o).refresh();
            }
            return;
        }

        // Archi Features and Profiles change, just refresh the figure
        if(IFeatures.isFeatureNotification(msg)
                || feature == IArchimatePackage.Literals.PROFILES__PROFILES
                || feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PROFILES) {
            refreshFigure();
            return;
        }

        // Update Connection Anchors and refresh figure if figure Type changes
        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE && msg.getNotifier() == getModel()) {
            refreshConnectionAnchors();
            refreshFigure();
            return;
        }

        switch(msg.getEventType()) {
            // Children added or removed or moved - need to refresh connections
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
            case Notification.MOVE:
                // This object's connections have to refreshed now in case we delete this
                refreshSourceConnections();
                refreshTargetConnections();
                // Update any related connecions as well
                refreshRelatedConnections();
                refreshChildren();
                break;

            default:
                super.eCoreChanged(msg);
        }
    }
    
    /**
     * Refresh all connections and their connections in case we have nested connection->connections
     */
    protected void refreshRelatedConnections() {
        // Get the EditPartRegistry 
        Map<?, ?> editPartRegistry = getRoot().getViewer().getEditPartRegistry();
        
        // Get the model objects that might need updating
        for(IDiagramModelObject dmo : getObjectsToUpdateConnections(getModel())) {
            // If we have the EditPart then update its connections
            if(editPartRegistry.get(dmo) instanceof AbstractConnectedEditPart editPart) {
                editPart.refreshSourceConnections();
                editPart.refreshTargetConnections();
            }
        }
    }
    
    /**
     * Get all connected objects that might need to to be refreshed when nesting when there are connection to connections.
     * However, this gets objects that don't need updating when there are no connection to connections so could do with some more work.
     */
    protected Set<IDiagramModelObject> getObjectsToUpdateConnections(IConnectable connectable) {
        Set<IDiagramModelObject> set = new HashSet<>();
        
        // All source and target connections of the connectable
        Set<IDiagramModelConnection> connections = new HashSet<>(connectable.getSourceConnections());
        connections.addAll(connectable.getTargetConnections());
        
        // Add source and target objects if not this object
        for(IDiagramModelConnection connection : connections) {
            if(connection.getSource() instanceof IDiagramModelObject dmo && dmo != getModel()) {
                set.add(dmo);
            }
            if(connection.getTarget() instanceof IDiagramModelObject dmo && dmo != getModel()) {
                set.add(dmo);
            }
            set.addAll(getObjectsToUpdateConnections(connection));
        }
        
        return set;
    }
    
    @Override
    protected void refreshFigure() {
        // Set Enabled according to current Viewpoint
        if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
            getFigure().setEnabled(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(getModel()));
        }
        else {
            getFigure().setEnabled(true);
        }

        getFigure().refreshVisuals();
    }
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        if(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS.equals(event.getProperty())) {
            refreshFigure();
        }
        else {
            super.applicationPreferencesChanged(event);
        }
    }
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ArchimateDiagramConnectionPolicy());
    }
    
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        if(getModel() != null && getModel().getArchimateElement() != null && adapter.isInstance(getModel().getArchimateElement())) {
            return adapter.cast(getModel().getArchimateElement());
        }

        return super.getAdapter(adapter);
    }
}