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
package org.eclipse.gef.handles;

import org.eclipse.draw2d.ConnectionLocator;

import org.eclipse.gef.ConnectionEditPart;

/**
 * A handle used at the start of the {@link org.eclipse.draw2d.Connection}. This
 * is treated differently than the end of the Connection.
 * 
 * @deprecated use {@link ConnectionEndpointHandle}
 */
public final class ConnectionStartHandle extends ConnectionEndpointHandle {

    /**
     * Creates a new ConnectionStartHandle, sets its owner to <code>owner</code>
     * , and sets its locator to a {@link ConnectionLocator}.
     * 
     * @param owner
     *            the ConnectionEditPart owner
     */
    public ConnectionStartHandle(ConnectionEditPart owner) {
        super(owner, ConnectionLocator.TARGET);
    }

    /**
     * Creates a new ConnectionStartHandle and sets its owner to
     * <code>owner</code>. If the handle is fixed, it cannot be dragged.
     * 
     * @param owner
     *            the ConnectionEditPart owner
     * @param fixed
     *            if true, handle cannot be dragged.
     */
    public ConnectionStartHandle(ConnectionEditPart owner, boolean fixed) {
        super(owner, fixed, ConnectionLocator.SOURCE);
    }

    /**
     * Creates a new ConnectionStartHandle.
     */
    public ConnectionStartHandle() {
        super(ConnectionLocator.TARGET);
    }

}
