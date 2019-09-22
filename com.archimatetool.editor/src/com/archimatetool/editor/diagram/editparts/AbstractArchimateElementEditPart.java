/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Abstract Archimate Edit Part with connections
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementEditPart extends AbstractConnectedEditPart {
    
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
        // Listen to changes in Archimate Model
        getModel().getArchimateElement().eAdapters().add(getECoreAdapter());
    }
    
    @Override
    protected void removeECoreAdapter() {
        super.removeECoreAdapter();
        // Unlisten to changes in Archimate Model
        getModel().getArchimateElement().eAdapters().remove(getECoreAdapter());
    }
    
    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();

        // Archi Features
        if(feature == IArchimatePackage.Literals.FEATURES__FEATURES || msg.getNotifier() instanceof IFeature) {
            refreshFigure();
            return;
        }

        // Update Connection Anchors if Figure Type changes
        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE && msg.getNotifier() == getModel()) {
            refreshConnectionAnchors();
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
        if(Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
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