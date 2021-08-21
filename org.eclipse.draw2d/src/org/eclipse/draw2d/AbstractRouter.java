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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;

/**
 * Base class for implementing a connection router. This class provides stubs
 * for constraint usage, and some utility methods.
 */
public abstract class AbstractRouter implements ConnectionRouter {

    private static final Point START = new Point();
    private static final Point END = new Point();

    /**
     * Returns the constraint for the given Connection.
     * 
     * @param connection
     *            The connection
     * @return The constraint
     * @since 2.0
     */
    @Override
    public Object getConstraint(Connection connection) {
        return null;
    }

    /**
     * A convenience method for obtaining a connection's endpoint. The
     * connection's endpoint is a point in absolute coordinates obtained by
     * using its source and target {@link ConnectionAnchor}. The returned Point
     * is a static singleton that is reused to reduce garbage collection. The
     * caller may modify this point in any way. However, the point will be
     * reused and its values overwritten during the next call to this method.
     * 
     * @param connection
     *            The connection
     * @return The endpoint
     * @since 2.0
     */
    protected Point getEndPoint(Connection connection) {
        Point ref = connection.getSourceAnchor().getReferencePoint();
        return END.setLocation(connection.getTargetAnchor().getLocation(ref));
    }

    /**
     * A convenience method for obtaining a connection's start point. The
     * connection's startpoint is a point in absolute coordinates obtained by
     * using its source and target {@link ConnectionAnchor}. The returned Point
     * is a static singleton that is reused to reduce garbage collection. The
     * caller may modify this point in any way. However, the point will be
     * reused and its values overwritten during the next call to this method.
     * 
     * @param conn
     *            The connection
     * @return The start point
     * @since 2.0
     */
    protected Point getStartPoint(Connection conn) {
        Point ref = conn.getTargetAnchor().getReferencePoint();
        return START.setLocation(conn.getSourceAnchor().getLocation(ref));
    }

    /**
     * Causes the router to discard any cached information about the given
     * Connection.
     * 
     * @param connection
     *            The connection to invalidate
     * @since 2.0
     */
    @Override
    public void invalidate(Connection connection) {
    }

    /**
     * Removes the given Connection from this routers list of Connections it is
     * responsible for.
     * 
     * @param connection
     *            The connection to remove
     * @since 2.0
     */
    @Override
    public void remove(Connection connection) {
    }

    /**
     * Sets the constraint for the given Connection.
     * 
     * @param connection
     *            The connection
     * @param constraint
     *            The constraint
     * @since 2.0
     */
    @Override
    public void setConstraint(Connection connection, Object constraint) {
    }

}
