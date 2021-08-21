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

import org.eclipse.draw2d.ConnectionAnchor;

/**
 * A specialized <code>GraphicalEditPart</code> that supports both <i>target</i>
 * and <i>source</i> <code>ConnectionEditParts</code>. This <em>optional</em>
 * interface is used by the default {@link ConnectionEditPart} implementation
 * and supporting classes to obtain the correct {@link ConnectionAnchor
 * ConnectionAnchors} for the {@link org.eclipse.draw2d.Connection} Figure. This
 * interface offers a single access point for obtaining ConnectionAnchors at
 * different times. The classes which rely on this interface are:
 * <UL>
 * <LI>{@link org.eclipse.gef.editparts.AbstractConnectionEditPart} - during
 * refresh(), this interface is used to obtain the appropriate ConnectionAnchors
 * for the figure.
 * <LI>{@link org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy} - during
 * creation of a new connection, there is no ConnectionEditPart. Therefore, the
 * source node EditPart is responsible for display <i>feedback</i>.
 * <LI>{@link org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy} - when
 * disconnecting the end of a connection and reattaching it to a new node,
 * ConnectionEndpointEditPolicy uses this interface to obtain the proper anchors
 * for diaplaying <i>feedback</i>.
 * </UL>
 */
public interface NodeEditPart extends GraphicalEditPart {

    /**
     * Returns the <code>ConnectionAnchor</code> for the specified <i>source</i>
     * connection. This NodeEditPart is the
     * {@link ConnectionEditPart#getSource() source} EditPart for the given
     * connection.
     * <P>
     * The anchor may be a function of the connection's model, the node's model,
     * a combination of both, or it may not depend on anything all.
     * 
     * @param connection
     *            the ConnectionEditPart
     * @return the ConnectionAnchor for the given ConnectionEditPart
     */
    ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection);

    /**
     * Returns the <code>ConnectionAnchor</code> for the specified <i>target</i>
     * connection. This NodeEditPart is the
     * {@link ConnectionEditPart#getTarget() target} EditPart for the given
     * connection.
     * <P>
     * The anchor may be a function of the connection's model, the node's model,
     * a combination of both, or it may not depend on anything all.
     * 
     * @param connection
     *            the ConnectionEditPart
     * @return the ConnectionAnchor for the given ConnectionEditPart
     */
    ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection);

    /**
     * Returns the <i>source</i> <code>ConnectionAnchor</code> for the specified
     * Request. The returned ConnectionAnchor is used only when displaying
     * <i>feedback</i>. The Request is usually a
     * {@link org.eclipse.gef.requests.LocationRequest}, which provides the
     * current mouse location.
     * 
     * @param request
     *            a Request describing the current interaction
     * @return the ConnectionAnchor to use during feedback
     */
    ConnectionAnchor getSourceConnectionAnchor(Request request);

    /**
     * Returns the <i>target</i> <code>ConnectionAnchor</code> for the specified
     * Request. The returned ConnectionAnchor is used only when displaying
     * <i>feedback</i>. The Request is usually a
     * {@link org.eclipse.gef.requests.LocationRequest}, which provides the
     * current mouse location.
     * 
     * @param request
     *            a Request describing the current interaction
     * @return the ConnectionAnchor to use during feedback
     */
    ConnectionAnchor getTargetConnectionAnchor(Request request);

}
