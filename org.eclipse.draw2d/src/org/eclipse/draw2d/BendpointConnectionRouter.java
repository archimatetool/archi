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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;

/**
 * Routes {@link Connection}s through a <code>List</code> of {@link Bendpoint
 * Bendpoints}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BendpointConnectionRouter extends AbstractRouter {

    private Map constraints = new HashMap(11);

    private static final PrecisionPoint A_POINT = new PrecisionPoint();

    /**
     * Gets the constraint for the given {@link Connection}.
     * 
     * @param connection
     *            The connection whose constraint we are retrieving
     * @return The constraint
     */
    @Override
    public Object getConstraint(Connection connection) {
        return constraints.get(connection);
    }

    /**
     * Removes the given connection from the map of constraints.
     * 
     * @param connection
     *            The connection to remove
     */
    @Override
    public void remove(Connection connection) {
        constraints.remove(connection);
    }

    /**
     * Routes the {@link Connection}. Expects the constraint to be a List of
     * {@link org.eclipse.draw2d.Bendpoint Bendpoints}.
     * 
     * @param conn
     *            The connection to route
     */
    @Override
    public void route(Connection conn) {
        PointList points = conn.getPoints();
        points.removeAllPoints();

        List bendpoints = (List) getConstraint(conn);
        if (bendpoints == null)
            bendpoints = Collections.EMPTY_LIST;

        Point ref1, ref2;

        if (bendpoints.isEmpty()) {
            ref1 = conn.getTargetAnchor().getReferencePoint();
            ref2 = conn.getSourceAnchor().getReferencePoint();
        } else {
            ref1 = new Point(((Bendpoint) bendpoints.get(0)).getLocation());
            conn.translateToAbsolute(ref1);
            ref2 = new Point(
                    ((Bendpoint) bendpoints.get(bendpoints.size() - 1))
                            .getLocation());
            conn.translateToAbsolute(ref2);
        }

        A_POINT.setLocation(conn.getSourceAnchor().getLocation(ref1));
        conn.translateToRelative(A_POINT);
        points.addPoint(A_POINT);

        for (int i = 0; i < bendpoints.size(); i++) {
            Bendpoint bp = (Bendpoint) bendpoints.get(i);
            points.addPoint(bp.getLocation());
        }

        A_POINT.setLocation(conn.getTargetAnchor().getLocation(ref2));
        conn.translateToRelative(A_POINT);
        points.addPoint(A_POINT);
        conn.setPoints(points);
    }

    /**
     * Sets the constraint for the given {@link Connection}.
     * 
     * @param connection
     *            The connection whose constraint we are setting
     * @param constraint
     *            The constraint
     */
    @Override
    public void setConstraint(Connection connection, Object constraint) {
        constraints.put(connection, constraint);
    }

}
