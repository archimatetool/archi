/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.util.PropertyChangeEvent;

import uk.ac.bolton.archimate.editor.diagram.policies.DiagramConnectionPolicy;
import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;

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
        if(IPreferenceConstants.HIDDEN_RELATIONS_TYPES.equals(event.getProperty())) {
            refreshSourceConnections();
            refreshTargetConnections();
        }
        else {
            super.applicationPreferencesChanged(event);
        }
    }
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new DiagramConnectionPolicy());
    }
    
    /* 
     * Don't include connections where the child (target of connection) is inside the parent (source of connection)
     */
    @Override
    protected List<IDiagramModelConnection> getModelSourceConnections() {
        List<IDiagramModelConnection> originalList = super.getModelSourceConnections();
        
        // optimise
        if(originalList.isEmpty()) {
            return originalList;
        }
        
        if(ConnectionPreferences.useNestedConnections()) {
            // Make a copy of the original connections
            List<IDiagramModelConnection> newList = new ArrayList<IDiagramModelConnection>(originalList);
            
            // If a connection is an archimate type and its target element is an archimate type
            // and this box contains that box and that box qualifies, remove the connection
            for(IDiagramModelConnection connection : originalList) {
                if(connection instanceof IDiagramModelArchimateConnection) {
                    if(ConnectionPreferences.shouldBeHiddenConnection((IDiagramModelArchimateConnection)connection)) {
                        newList.remove(connection);
                    }
                }
            }
            
            return newList;
        }
        else {
            return originalList;
        }
    }

    /* 
     * Don't include connections where the child (target of connection) is inside the parent (source of connection)
     */
    @Override
    protected List<IDiagramModelConnection> getModelTargetConnections() {
        List<IDiagramModelConnection> originalList = super.getModelTargetConnections();
        
        // optimise
        if(originalList.isEmpty()) {
            return originalList;
        }
        
        // Parent has to be an archimtate type edit part
        if(ConnectionPreferences.useNestedConnections() && getParent() instanceof IArchimateEditPart) {
            // Make a copy of the original connections
            List<IDiagramModelConnection> newList = new ArrayList<IDiagramModelConnection>(originalList);
            
            // If a connection is an archimate type and its target element is an archimate type
            // and the parent of this is the source of the connection
            for(IDiagramModelConnection connection : originalList) {
                if(connection instanceof IDiagramModelArchimateConnection) {
                    if(ConnectionPreferences.shouldBeHiddenConnection((IDiagramModelArchimateConnection)connection)) {
                        newList.remove(connection);
                    }
                }
            }

            return newList;
        }
        else {
            return originalList;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == IArchimateElement.class) {
            return getModel().getArchimateElement();
        }
        return super.getAdapter(adapter);
    }
}