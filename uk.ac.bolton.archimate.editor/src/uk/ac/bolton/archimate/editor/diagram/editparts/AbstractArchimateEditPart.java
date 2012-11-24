/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.PropertyChangeEvent;

import uk.ac.bolton.archimate.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IProperties;

/**
 * Abstract Archimate Edit Part with connections
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateEditPart
extends AbstractConnectedEditPart
implements IArchimateEditPart {
    
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
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Hidden connections
        if(IPreferenceConstants.HIDDEN_RELATIONS_TYPES.equals(event.getProperty()) ||
                IPreferenceConstants.USE_NESTED_CONNECTIONS.equals(event.getProperty())) {
            refreshSourceConnections();
            refreshTargetConnections();
        }
        // Default fill colour preference changed
        else if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX)) {
            getFigure().repaint();
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
        if(adapter == IArchimateElement.class || adapter == IProperties.class) {
            return getModel().getArchimateElement();
        }
        return super.getAdapter(adapter);
    }
}