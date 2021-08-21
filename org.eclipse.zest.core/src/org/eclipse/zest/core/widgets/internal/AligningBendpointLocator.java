/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.AbstractLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * A locator that finds the middle of a connection based on the bendpoints.
 * @author Del Myers
 *
 */
//@tag zest(bug(154391-ArcEnds(fix))) : use this locator to ensure that labels will be in the middle of connections.
//@tag zest.bug.160368-ConnectionAlign : replaces MidBenpointLocator in order to have finer control over alignment.
public class AligningBendpointLocator extends AbstractLocator {
    /**
     * "Vertical" alignment contstant. Figures should be placed in the middle
     * of the line.
     */
    public static final int MIDDLE = 0;
    /**
     * "Vertical" alignment constant. Figures should be placed above the line.
     */
    public static final int ABOVE = 1;
    /**
     * "Vertical" alignment constant. Figures should be placed below the line.
     */
    public static final int BELOW = 2;

    /**
     * "Horizontal" alignment constant. Figures should be placed in the center
     * of the points on the line.
     */
    public static final int CENTER = 0;

    /**
     * "Horizontal" alignment constant. Figures should be placed at the beginning
     * of the line. Figures will be anchored so that they have one end at the
     * beginning of the connection, not so that they are centered at the start
     * point. Which end of the figure is placed at that point will depend
     * on the direction of the first two points.
     */
    public static final int BEGINNING = 1;

    /**
     * "Horizontal" alignment constant. Figures should be placed at the end of
     * the line. Figures will be anchored so that they have one end at the
     * beginning of the connection, not so that they are centered at the end
     * point. Which end of the figure is placed at that point will depend
     * on the direction of the last two points.
     */
    public static final int END = 2;

    /**
     * "Horizontal" alignment constant. Figures should be centered between the
     * first two points on the connection. For connections with only two points, 
     * this will behave the same as CENTER.
     */
    public static final int CENTER_BEGINNING = 3;

    /**
     * "Horizontal" alignment constant. Figures should be centered between the
     * last two points on the connection. For connections with only two points,
     * this will behave the same as CENTER.
     */
    public static final int CENTER_END = 4;
    private int horizontal;
    private int vertical;
    private Connection connection;

    /**
     * @param connection
     */
    public AligningBendpointLocator(Connection connection) {
        this(connection, CENTER, MIDDLE);
    }

    public AligningBendpointLocator(Connection connection, int horizontal, int vertical) {
        this.connection = connection;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /* (non-Javadoc)
     * @see org.eclipse.draw2d.ConnectionLocator#getReferencePoint()
     */
    @Override
    protected Point getReferencePoint() {
        PointList points = getConnection().getPoints();
        Point p = points.getMidpoint().getCopy();
        PointList tempPoints = new PointList();
        switch (horizontal) {
        case BEGINNING:
            p = points.getFirstPoint().getCopy();
            break;
        case END:
            p = points.getLastPoint().getCopy();
            break;
        case CENTER_BEGINNING:
            tempPoints.addPoint(points.getFirstPoint().getCopy());
            tempPoints.addPoint(points.getPoint(1).getCopy());
            p = tempPoints.getMidpoint().getCopy();
            break;
        case CENTER_END:
            tempPoints = new PointList();
            int s = points.size();
            tempPoints.addPoint(points.getLastPoint().getCopy());
            tempPoints.addPoint(points.getPoint(s - 2).getCopy());
            p = tempPoints.getMidpoint().getCopy();
        case CENTER:
        default:
            p = points.getMidpoint().getCopy();
        }
        return p;
    }

    /**
     * Recalculates the position of the figure and returns the updated bounds.
     * @param target The figure to relocate
     */
    @Override
    public void relocate(IFigure target) {
        Dimension prefSize = target.getPreferredSize();
        Point center = getReferencePoint();
        calculatePosition();
        //@tag zest(bug(GEFProblem)) : there seems to be a bug in GEF that if the following is done, then labels get printed in the wrong location
        //target.translateToRelative(center);
        target.setBounds(getNewBounds(prefSize, center));
    }

    /**
     * Translates the center point depending on the horizontal and veritical
     * alignments based on the given bounds.
     * @param center
     */
    private void calculatePosition() {
        PointList points = getConnection().getPoints();
        int position = 0;
        switch (horizontal) {
        case BEGINNING:
            Point first = points.getFirstPoint();
            Point next = points.getPoint(1);
            if (first.x <= next.x)
                position |= PositionConstants.EAST;
            else
                position |= PositionConstants.WEST;
            break;
        case END:
            Point last = points.getLastPoint();
            int s = points.size();
            Point before = points.getPoint(s - 1);
            if (last.x <= before.x)
                position |= PositionConstants.EAST;
            else
                position |= PositionConstants.WEST;
            break;
        }
        if (position == 0)
            position = PositionConstants.CENTER;
        switch (vertical) {
        case ABOVE:
            position |= PositionConstants.NORTH;
            break;
        case BELOW:
            position |= PositionConstants.SOUTH;
            break;
        case MIDDLE:
            position |= PositionConstants.MIDDLE;
        }
        setRelativePosition(position);

    }

    /**
     * @return the horizontal alignment.
     */
    public int getHorizontalAlignment() {
        return horizontal;
    }

    /**
     * @param horizontal the horizontal alignment to set. One of CENTER,
     * BEGINNING, END, CENTER_BEGINNING, or CENTER_END.
     */
    public void setHorizontalAlignment(int horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * @param vertical the vertical alignment to set. One of ABOVE, MIDDLE, or
     * BELOW.
     */
    public void setVerticalAlginment(int vertical) {
        this.vertical = vertical;
    }

    /**
     * @return the vertical alginment.
     */
    public int getVerticalAlignment() {
        return vertical;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

}
