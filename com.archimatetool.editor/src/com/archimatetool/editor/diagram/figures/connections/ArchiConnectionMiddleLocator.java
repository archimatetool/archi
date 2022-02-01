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
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * Repositions a {@link Figure} attached to a {@link Connection} when the
 * Connection is moved. Provides for alignment at the start (source), middle, or
 * end (target) of the Connection.
 */
public class ArchiConnectionMiddleLocator extends ConnectionLocator {
    
    private int vDistance;

    /**
     * Constructs a ConnectionLocator with the passed connection and
     * {@link #MIDDLE} alignment.
     * 
     * @param connection
     *            The Connection
     * @since 2.0
     */
    public ArchiConnectionMiddleLocator(AbstractDiagramConnectionFigure connection) {
        this(connection, MIDDLE, 0);
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
    public ArchiConnectionMiddleLocator(AbstractDiagramConnectionFigure connection, int align, int vDistance) {
        super(connection, align);
        this.vDistance = vDistance;
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
    @Override
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
                return Point.SINGLETON.setLocation(p1.x + d.width / 2, 
                                                   p1.y + d.height / 2 + this.vDistance);
            }
            
            int i = (points.size() - 1) / 2;
            Point p = points.getPoint(Point.SINGLETON, i);
            return Point.SINGLETON.setLocation(p.x, 
                                               p.y + this.vDistance);
        default:
            return new Point();
        }
    }

}
