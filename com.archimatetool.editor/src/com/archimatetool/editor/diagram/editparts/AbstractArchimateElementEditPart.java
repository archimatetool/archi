/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
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
    
    protected AbstractArchimateElementEditPart(Class<?> figureClass) {
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
                refreshSourceConnections();
                refreshTargetConnections();
                refreshChildren();
                break;

            default:
                super.eCoreChanged(msg);
        }
    }
    
    @Override
    protected void refreshFigure() {
        // Set Enabled according to current Viewpoint
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
            getFigure().setEnabled(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(getModel()));
        }
        else {
            getFigure().setEnabled(true);
        }

        getFigure().refreshVisuals();
    }
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Hidden connections
        if(IPreferenceConstants.HIDDEN_RELATIONS_TYPES.equals(event.getProperty()) ||
                IPreferenceConstants.USE_NESTED_CONNECTIONS.equals(event.getProperty())) {
            refreshSourceConnections();
            refreshTargetConnections();
        }
        else if(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS.equals(event.getProperty())) {
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
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(getModel() != null && getModel().getArchimateElement() != null && adapter.isInstance(getModel().getArchimateElement())) {
            return getModel().getArchimateElement();
        }

        return super.getAdapter(adapter);
    }
}