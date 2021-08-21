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
package org.eclipse.gef;

/**
 * The set of constants used to identify <code>Requests</code> by their
 * {@link Request#getType() type}. Applications can extend this set of constants
 * with their own.
 */
public interface RequestConstants {

    /**
     * Indicates the creation of a new connection. Creating a connection
     * involves both the source node and target node EditParts. This is the
     * constant that is used with the first node on which the user clicks.
     */
    String REQ_CONNECTION_START = "connection start";//$NON-NLS-1$

    /**
     * Indicates the end of creation of a new connection. Creating a connection
     * involves both the source node and target node EditParts. This is the
     * constant that is used with the second node on which the user clicks.
     */
    String REQ_CONNECTION_END = "connection end";//$NON-NLS-1$

    /**
     * Constant used to indicate that the <i>source</i> end of an existing
     * connection is being reconnected to a new source node EditPart. The new
     * source node is the receiver of such Requests.
     */
    String REQ_RECONNECT_SOURCE = "Reconnection source";//$NON-NLS-1$

    /**
     * Constant used to indicate that the <i>target</i> end of an existing
     * connection is being reconnected to a new target node EditPart. The new
     * target node is the receiver of such Requests.
     */
    String REQ_RECONNECT_TARGET = "Reconnection target";//$NON-NLS-1$

    /**
     * Indicates that an existing bendpoint is being moved or dragged by the
     * User.
     */
    String REQ_MOVE_BENDPOINT = "move bendpoint";//$NON-NLS-1$

    /**
     * Indicates that a bendpoint is being inserted by the User.
     */
    String REQ_CREATE_BENDPOINT = "create bendpoint";//$NON-NLS-1$

    /**
     * Indicates that a part (or a group of parts) is being resized.
     */
    String REQ_RESIZE = "resize"; //$NON-NLS-1$

    /**
     * Indicates that a group of children are to be resized.
     */
    String REQ_RESIZE_CHILDREN = "resize children"; //$NON-NLS-1$

    /**
     * Indicates that a part (or a group of parts) is being moved within its
     * current parent.
     */
    String REQ_MOVE = "move"; //$NON-NLS-1$

    /**
     * Indicates that a group of children are being moved.
     */
    String REQ_MOVE_CHILDREN = "move children"; //$NON-NLS-1$

    /**
     * Indicates that the user has double-clicked on the receiver. "Open" means
     * different things for different applications. Sometimes it means open a
     * popup dialog of properties, or the Workbench's properties view. Sometimes
     * it means open a sub-diagram.
     */
    String REQ_OPEN = "open"; //$NON-NLS-1$

    /**
     * Indicates that the receiver is being removed from its current parent, to
     * be inserted into a new parent.
     */
    String REQ_ORPHAN = "orphan"; //$NON-NLS-1$

    /**
     * Indicates that a group of children are being removed from the receiver of
     * the Request.
     */
    String REQ_ORPHAN_CHILDREN = "orphan children"; //$NON-NLS-1$

    /**
     * Indicates that an object is to be created by the receiver of the Request.
     */
    String REQ_CREATE = "create child"; //$NON-NLS-1$

    /**
     * Constant used to indicate that a group of existing children are being
     * added to the receiver of the Request.
     */
    String REQ_ADD = "add children"; //$NON-NLS-1$

    /**
     * Indicates that the reciever of the request should be cloned.
     */
    String REQ_CLONE = "clone"; //$NON-NLS-1$

    /**
     * Constant used to indicate that the receiver of the Request is being
     * deleted.
     */
    String REQ_DELETE = "delete"; //$NON-NLS-1$

    /**
     * Constant used to indicate that a child of the receiver the Request is to
     * be deleted.
     */
    String REQ_DELETE_DEPENDANT = "delete dependant"; //$NON-NLS-1$

    /**
     * Constant used to indicate that alignment is being performed.
     */
    String REQ_ALIGN = "align"; //$NON-NLS-1$

    /**
     * Constant used to indicate that a group of children are being aligned.
     */
    String REQ_ALIGN_CHILDREN = "align children"; //$NON-NLS-1$

    /**
     * Indicates that a direct edit should be performed.
     */
    String REQ_DIRECT_EDIT = "direct edit"; //$NON-NLS-1$

    /**
     * Indicates selection Requests.
     */
    String REQ_SELECTION = "selection"; //$NON-NLS-1$

    /**
     * Indicates selection hover Requests.
     */
    String REQ_SELECTION_HOVER = "selection hover"; //$NON-NLS-1$

}
