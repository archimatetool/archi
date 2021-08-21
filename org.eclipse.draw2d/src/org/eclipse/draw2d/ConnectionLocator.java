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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * Repositions a {@link Figure} attached to a {@link Connection} when the
 * Connection is moved. Provides for alignment at the start (source), middle, or
 * end (target) of the Connection.
 */
public class ConnectionLocator extends AbstractLocator {

    /** @deprecated Use {@link #SOURCE} */
    public static final int START = 2;
    /** The start (or source) of the Connection */
    public static final int SOURCE = 2;

    /** @deprecated Use {@link #TARGET} */
    public static final int END = 3;
    /** The end (or target) of the Connection */
    public static final int TARGET = 3;

    /**
     * @deprecated Use {@link #MIDDLE} instead, since the location is not the
     *             midpoint of a line-segment, but the middle of a polyline.
     */
    public static final int MIDPOINT = 4;
    /** The middle of the Connection */
    public static final int MIDDLE = 4;

    private Connection connection;
    private int alignment;

    /**
     * Constructs a ConnectionLocator with the passed connection and
     * {@link #MIDDLE} alignment.
     * 
     * @param connection
     *            The Connection
     * @since 2.0
     */
    public ConnectionLocator(Connection connection) {
        this(connection, MIDDLE);
    }

    /**
     * Constructs a ConnectionLocator with the passed Connection and alignment.
     * Valid values for the alignment are integer constants {@link #SOURCE},
     * {@link #MIDDLE}, and {@link #TARGET}.
     * 
     * @param connection
     *            The Connection
     * @param align
     *            The alignment
     * 
     * @since 2.0
     */
    public ConnectionLocator(Connection connection, int align) {
        setConnection(connection);
        setAlignment(align);
    }

    /**
     * Returns the alignment of ConnectionLocator.
     * 
     * @return The alignment
     * @since 2.0
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Returns connection associated with ConnectionLocator.
     * 
     * @return The Connection
     * @since 2.0
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Returns ConnectionLocator's reference point in absolute coordinates.
     * 
     * @return The reference point
     * @since 2.0
     */
    @Override
    protected Point getReferencePoint() {
        Point p = getLocation(getConnection().getPoints());
        getConnection().translateToAbsolute(p);
        return p;
    }

    /**
     * Returns a point from the passed PointList, dependent on
     * ConnectionLocator's alignment. If the alignment is {@link #SOURCE}, it
     * returns the first point in <i>points</i>. If {@link #TARGET}, it returns
     * the last point in <i>points</i>. If {@link #MIDDLE}, it returns the
     * middle of line represented by <i>points</i>.
     * 
     * @param points
     *            The points in the Connection
     * @return The location
     * @since 2.0
     */
    protected Point getLocation(PointList points) {
        switch (getAlignment()) {
        case SOURCE:
            return points.getPoint(Point.SINGLETON, 0);
        case TARGET:
            return points.getPoint(Point.SINGLETON, points.size() - 1);
        case MIDDLE:
            if (points.size() % 2 == 0) {
                int i = points.size() / 2;
                int j = i - 1;
                Point p1 = points.getPoint(j);
                Point p2 = points.getPoint(i);
                Dimension d = p2.getDifference(p1);
                return Point.SINGLETON.setLocation(p1.x + d.width / 2, p1.y
                        + d.height / 2);
            }
            int i = (points.size() - 1) / 2;
            return points.getPoint(Point.SINGLETON, i);
        default:
            return new Point();
        }
    }

    /**
     * Sets the alignment. Possible values are {@link #SOURCE}, {@link #MIDDLE},
     * and {@link #TARGET}.
     * 
     * @param align
     *            The alignment
     * @since 2.0
     */
    protected void setAlignment(int align) {
        alignment = align;
    }

    /**
     * Sets the Connection to be associated with this ConnectionLocator.
     * 
     * @param connection
     *            The Connection
     * @since 2.0
     */
    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

}
