/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Abstract Edit Part with connections
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractConnectedEditPart
extends AbstractBaseEditPart
implements NodeEditPart {
    
    private ConnectionAnchor fAnchor;
    
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
                if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__BOUNDS) {
                    refreshBounds();
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
    protected Adapter getECoreAdapter() {
        return adapter;
    }
    
    @Override
    protected List<IDiagramModelConnection> getModelSourceConnections() {    	
    	return ((IDiagramModelObject)getModel()).getSourceConnections();
    }

    @Override
    protected List<IDiagramModelConnection> getModelTargetConnections() {
    	return ((IDiagramModelObject)getModel()).getTargetConnections();
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
    	return getDefaultConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
    	return getDefaultConnectionAnchor();
    }
    
    /**
     * @return The connection anchor to use for source and target connections
     * Default is a Chopbox connection anchor
     */
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        if(fAnchor == null) {
            fAnchor = new ChopboxAnchor(getFigure());
        }
        return fAnchor;
    }
}