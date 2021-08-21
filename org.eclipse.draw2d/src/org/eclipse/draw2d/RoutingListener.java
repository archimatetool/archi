/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

/**
 * Classes which implement this interface provide callback hooks for various
 * routing related events.
 * <P>
 * Instances can be hooked to a {@link PolylineConnection} by calling
 * {@link PolylineConnection#addRoutingListener(RoutingListener)}.
 * 
 * @since 3.2
 */
public interface RoutingListener {

    /**
     * Called when the connection has been invalidated.
     * 
     * @param connection
     *            the connection
     * @since 3.2
     */
    void invalidate(Connection connection);

    /**
     * Called after normal routing has completed.
     * 
     * @param connection
     *            the routed connection
     * @since 3.2
     */
    void postRoute(Connection connection);

    /**
     * Called when a connection has been removed from its router.
     * 
     * @param connection
     *            the connection
     * @since 3.2
     */
    void remove(Connection connection);

    /**
     * Called prior to routing occurring. A listener may intercept routing by
     * returning <code>true</code>. If intercepted, the connection's
     * <code>ConnectionRouter</code> will not perform routing.
     * 
     * @param connection
     *            the connection being routed
     * @return <code>true</code> if routing has been performed by the listener
     * @since 3.2
     */
    boolean route(Connection connection);

    /**
     * Called when the connection's routing constraint has been set or
     * initialized.
     * 
     * @param connection
     *            the connection
     * @param constraint
     *            the new constraint
     * @since 3.2
     */
    void setConstraint(Connection connection, Object constraint);

    /**
     * A stub implementation which implements all required methods.
     * 
     * @since 3.2
     */
    class Stub implements RoutingListener {
        @Override
        public void invalidate(Connection connection) {
        }

        @Override
        public void postRoute(Connection connection) {
        }

        @Override
        public void remove(Connection connection) {
        }

        @Override
        public boolean route(Connection connection) {
            return false;
        }

        @Override
        public void setConstraint(Connection connection, Object constraint) {
        }
    }

}
