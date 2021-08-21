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
package org.eclipse.gef.requests;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;

/**
 * A Request to reconnect a connection.
 */
public class ReconnectRequest extends LocationRequest implements DropRequest,
        TargetRequest {

    private ConnectionEditPart connection;
    private EditPart target;

    /**
     * Default constructor.
     */
    public ReconnectRequest() {
    }

    /**
     * Creates a ReconnectRequest with the given type.
     * 
     * @param type
     *            the type
     */
    public ReconnectRequest(Object type) {
        setType(type);
    }

    /**
     * Returns the ConnectionEditPart to be reconnected.
     * 
     * @return The ConnectionEditPart to be reconnected.
     */
    public ConnectionEditPart getConnectionEditPart() {
        return connection;
    }

    /**
     * Returns the target edit part that the connection should be connected to.
     * 
     * @return the target edit part
     */
    public EditPart getTarget() {
        return target;
    }

    /**
     * Returns <code>true</code> if the start (source) anchor is the anchor
     * being moved.
     * 
     * @return whether the start anchor is being moved
     */
    public boolean isMovingStartAnchor() {
        return RequestConstants.REQ_RECONNECT_SOURCE.equals(getType());
    }

    /**
     * Sets the ConnectionEditPart to be reconnected.
     * 
     * @param conn
     *            The ConnectionEditPart.
     */
    public void setConnectionEditPart(ConnectionEditPart conn) {
        connection = conn;
    }

    /**
     * Sets the target edit part that the connection should be connected to.
     * 
     * @param ep
     *            the target edit part
     */
    @Override
    public void setTargetEditPart(EditPart ep) {
        target = ep;
    }

}
