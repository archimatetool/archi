package uk.ac.bolton.archimate.editor.diagram.policies.snaptogrid;

/**
 * snap-to-grid patch  by Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * This Class has been fully copied/pasted to allow change on
 * protected method updateSourceRequest
 */

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.gef.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.tools.SimpleDragTracker;
// snap-to-grid patch
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;

/**
 * A tracker for creating new bendpoints or dragging existing ones. The
 * Connection bendpoint tracker is returned by connection bendpoint handles.
 * This tracker will send a {@link BendpointRequest} to the connection editpart
 * which originated the tracker. The bendpoint request may be either a request
 * to move an existing bendpoint, or a request to create a new bendpoint.
 * <P>
 * A ConnectionBendpointTracker operates on a single connection editpart.
 * 
 * @author hudsonr
 */
@SuppressWarnings("all")
public class ConnectionBendpointTracker extends SimpleDragTracker {

	private Object type;
	private ConnectionEditPart editpart;
	private int index;

	/**
	 * Null constructor.
	 */
	protected ConnectionBendpointTracker() {
	}

	/**
	 * Constructs a tracker for the given connection and index.
	 * 
	 * @param editpart
	 *            the connection
	 * @param i
	 *            the index of the bendpoint
	 */
	public ConnectionBendpointTracker(ConnectionEditPart editpart, int i) {
		setConnectionEditPart(editpart);
		setIndex(i);
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#createOperationSet()
	 */
	protected List createOperationSet() {
		List list = new ArrayList();
		list.add(getConnectionEditPart());
		return list;
	}

	/**
	 * Creates a BendpointRequest.
	 * 
	 * @see org.eclipse.gef.tools.SimpleDragTracker#createSourceRequest()
	 */
	protected Request createSourceRequest() {
		BendpointRequest request = new BendpointRequest();
		request.setType(getType());
		request.setIndex(getIndex());
		request.setSource(getConnectionEditPart());
		
		return request;
	}

	/**
	 * Obtains a new command from the connection.
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#getCommand()
	 */
	protected Command getCommand() {
		return getConnectionEditPart().getCommand(getSourceRequest());
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return getType().toString();
	}

	/**
	 * Convenience method to obtain the connection editpart's connection figure.
	 * 
	 * @return the connection figure
	 */
	protected Connection getConnection() {
		return (Connection) getConnectionEditPart().getFigure();
	}

	/**
	 * Returns the connection editpart on which the tracker operates.
	 * 
	 * @return the connection editpart
	 */
	protected ConnectionEditPart getConnectionEditPart() {
		return editpart;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	protected String getDebugName() {
		return "Bendpoint Handle Tracker " + getCommandName();//$NON-NLS-1$
	}

	/**
	 * Returns the index of the bendpoint being dragged or created.
	 * 
	 * @return the index
	 */
	protected int getIndex() {
		return index;
	}

	/**
	 * The type of tracker, either {@link RequestConstants#REQ_CREATE_BENDPOINT}
	 * or {@link RequestConstants#REQ_MOVE_BENDPOINT}.
	 * 
	 * @return the type of operation being performed (move or create bendpoint)
	 */
	protected Object getType() {
		return type;
	}

	/**
	 * Sets the connection editpart being operated on.
	 * 
	 * @param cep
	 *            the connection
	 */
	public void setConnectionEditPart(ConnectionEditPart cep) {
		editpart = cep;
	}

	/**
	 * Sets the index of the operation.
	 * 
	 * @param i
	 *            the index
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * Sets the type of bendpoint drag being performed.
	 * 
	 * @see #getType()
	 * @param type
	 *            the drag type
	 */
	public void setType(Object type) {
		this.type = type;
	}

	/**
	 * @see org.eclipse.gef.tools.SimpleDragTracker#updateSourceRequest()
	 */
	protected void updateSourceRequest() {
		BendpointRequest request = (BendpointRequest) getSourceRequest();
		
        // snap-to-grid patch
		Point p = getLocation();
        boolean s2g = Preferences.STORE.getBoolean(IPreferenceConstants.GRID_SNAP);
        int gs = Preferences.STORE.getInt(IPreferenceConstants.GRID_SIZE);
        if (s2g) {
        	p.setX(((int)(p.x/gs))*gs);
            p.setY(((int)(p.y/gs))*gs);
        }
        
		// request.setLocation(getLocation());
        request.setLocation(p);
	}

}
