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

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.internal.MultiValueMap;

/**
 * An abstract router implementation which detects when multiple connections are
 * overlapping. Two connections overlap if the combination of source and target
 * anchors are equal. Subclasses must implement
 * {@link #handleCollision(PointList, int)} to determine how to avoid the
 * overlap.
 * <p>
 * This router can delegate to another connection router. The wrappered router
 * will route the connections first, after which overlapping will be determined.
 */
@SuppressWarnings("rawtypes")
public abstract class AutomaticRouter extends AbstractRouter {

    private ConnectionRouter nextRouter;
    private MultiValueMap connections = new MultiValueMap();

    private class HashKey {

        private ConnectionAnchor anchor1, anchor2;

        HashKey(Connection conn) {
            anchor1 = conn.getSourceAnchor();
            anchor2 = conn.getTargetAnchor();
        }

        @Override
        public boolean equals(Object object) {
            boolean isEqual = false;
            HashKey hashKey;

            if (object instanceof HashKey) {
                hashKey = (HashKey) object;
                ConnectionAnchor hkA1 = hashKey.getFirstAnchor();
                ConnectionAnchor hkA2 = hashKey.getSecondAnchor();

                isEqual = (hkA1.equals(anchor1) && hkA2.equals(anchor2))
                        || (hkA1.equals(anchor2) && hkA2.equals(anchor1));
            }
            return isEqual;
        }

        public ConnectionAnchor getFirstAnchor() {
            return anchor1;
        }

        public ConnectionAnchor getSecondAnchor() {
            return anchor2;
        }

        @Override
        public int hashCode() {
            return anchor1.hashCode() ^ anchor2.hashCode();
        }
    }

    /**
     * @see org.eclipse.draw2d.ConnectionRouter#getConstraint(Connection)
     */
    @Override
    public Object getConstraint(Connection connection) {
        if (next() != null)
            return next().getConstraint(connection);
        return null;
    }

    /**
     * Handles collisions between 2 or more Connections. Collisions are
     * currently defined as 2 connections with no bendpoints and whose start and
     * end points coincide. In other words, the 2 connections are the exact same
     * line.
     * 
     * @param list
     *            The PointList of a connection that collides with another
     *            connection
     * @param index
     *            The index of the current connection in the list of colliding
     *            connections
     */
    protected abstract void handleCollision(PointList list, int index);

    /**
     * @see org.eclipse.draw2d.ConnectionRouter#invalidate(Connection)
     */
    @Override
    public void invalidate(Connection conn) {
        if (next() != null)
            next().invalidate(conn);
        if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
            return;
        HashKey connectionKey = new HashKey(conn);
        ArrayList connectionList = connections.get(connectionKey);
        int affected = connections.remove(connectionKey, conn);
        if (affected != -1) {
            for (int i = affected; i < connectionList.size(); i++)
                ((Connection) connectionList.get(i)).revalidate();
        } else
            connections.removeValue(conn);

    }

    /**
     * Returns the next router in the chain.
     * 
     * @return The next router
     * @since 2.0
     */
    protected ConnectionRouter next() {
        return nextRouter;
    }

    /**
     * @see org.eclipse.draw2d.ConnectionRouter#remove(Connection)
     */
    @Override
    public void remove(Connection conn) {
        if (conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
            return;
        HashKey connectionKey = new HashKey(conn);
        ArrayList connectionList = connections.get(connectionKey);
        if (connectionList != null) {
            int index = connections.remove(connectionKey, conn);
            for (int i = index + 1; i < connectionList.size(); i++)
                ((Connection) connectionList.get(i)).revalidate();
        }
        if (next() != null)
            next().remove(conn);
    }

    /**
     * Routes the given connection. Calls the 'next' router first (if one
     * exists) and if no bendpoints were added by the next router, collisions
     * are dealt with by calling {@link #handleCollision(PointList, int)}.
     * 
     * @param conn
     *            The connection to route
     */
    @Override
    public void route(Connection conn) {
        if (next() != null)
            next().route(conn);
        else {
            conn.getPoints().removeAllPoints();
            setEndPoints(conn);
        }

        if (conn.getPoints().size() == 2) {
            PointList points = conn.getPoints();
            HashKey connectionKey = new HashKey(conn);
            ArrayList connectionList = connections.get(connectionKey);

            if (connectionList != null) {

                int index;

                if (connectionList.contains(conn)) {
                    index = connectionList.indexOf(conn) + 1;
                } else {
                    index = connectionList.size() + 1;
                    connections.put(connectionKey, conn);
                }

                handleCollision(points, index);
                conn.setPoints(points);
            } else {
                connections.put(connectionKey, conn);
            }
        }
    }

    /**
     * An AutomaticRouter needs no constraints for the connections it routes.
     * This method invalidates the connections and calls
     * {@link #setConstraint(Connection, Object)} on the {@link #next()} router.
     * 
     * @see org.eclipse.draw2d.ConnectionRouter#setConstraint(Connection,
     *      Object)
     */
    @Override
    public void setConstraint(Connection connection, Object constraint) {
        invalidate(connection);
        if (next() != null)
            next().setConstraint(connection, constraint);
    }

    /**
     * Sets the start and end points for the given connection.
     * 
     * @param conn
     *            The connection
     */
    protected void setEndPoints(Connection conn) {
        PointList points = conn.getPoints();
        points.removeAllPoints();
        Point start = getStartPoint(conn);
        Point end = getEndPoint(conn);
        conn.translateToRelative(start);
        conn.translateToRelative(end);
        points.addPoint(start);
        points.addPoint(end);
        conn.setPoints(points);
    }

    /**
     * Sets the next router.
     * 
     * @param router
     *            The ConnectionRouter
     * @since 2.0
     */
    public void setNextRouter(ConnectionRouter router) {
        nextRouter = router;
    }

}
