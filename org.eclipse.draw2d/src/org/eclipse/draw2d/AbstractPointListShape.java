/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Shatalin (Borland) - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * Base superclass for all polylines/polygons
 * 
 * @since 3.5
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractPointListShape extends Shape {

    PointList points = new PointList();

    /**
     * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        if (!super.containsPoint(x, y)) {
            return false;
        }
        return shapeContainsPoint(x, y) || childrenContainsPoint(x, y);
    }

    /**
     * Returns <code>true</code> if the point <code>(x, y)</code> is contained
     * within one of the child figures.
     * 
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @return <code>true</code> if the point (x,y) is contained in one of the
     *         child figures
     */
    protected boolean childrenContainsPoint(int x, int y) {
        for (Iterator it = getChildren().iterator(); it.hasNext();) {
            IFigure nextChild = (IFigure) it.next();
            if (nextChild.containsPoint(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if the point <code>(x, y)</code> is contained
     * within this figure.
     * 
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @return <code>true</code> if the point (x,y) is contained in this figure
     */
    abstract protected boolean shapeContainsPoint(int x, int y);

    /**
     * Adds the passed point to this figure.
     * 
     * @param pt
     *            the Point to be added to this figure
     */
    public void addPoint(Point pt) {
        points.addPoint(pt);
        repaint();
    }

    /**
     * @return the first point in this figure
     */
    public Point getStart() {
        return points.getFirstPoint();
    }

    /**
     * Returns the last point in this Figure.
     * 
     * @return the last point
     */
    public Point getEnd() {
        return points.getLastPoint();
    }

    /**
     * Returns the points in this figure <B>by reference</B>. If the returned
     * list is modified, this figure must be informed by calling
     * {@link #setPoints(PointList)}. Failure to do so will result in layout and
     * paint problems.
     * 
     * @return this Polyline's points
     */
    public PointList getPoints() {
        return points;
    }

    /**
     * Inserts a given point at a specified index in this figure.
     * 
     * @param pt
     *            the point to be added
     * @param index
     *            the position in this figure where the point is to be added
     */
    public void insertPoint(Point pt, int index) {
        points.insertPoint(pt, index);
        repaint();
    }

    /**
     * Erases this figure and removes all of its {@link Point Points}.
     */
    public void removeAllPoints() {
        erase();
        points.removeAllPoints();
    }

    /**
     * Removes a point from this figure.
     * 
     * @param index
     *            the position of the point to be removed
     */
    public void removePoint(int index) {
        erase();
        points.removePoint(index);
        repaint();
    }

    /**
     * Sets the start point of this figure
     * 
     * @param start
     *            the point that will become the first point in this figure
     */
    public void setStart(Point start) {
        if (points.size() == 0) {
            addPoint(start);
        } else {
            setPoint(start, 0);
        }
    }

    /**
     * Sets the end point of this figure
     * 
     * @param end
     *            the point that will become the last point in this figure
     */
    public void setEnd(Point end) {
        if (points.size() < 2) {
            addPoint(end);
        } else {
            setPoint(end, points.size() - 1);
        }
    }

    /**
     * Sets the points at both extremes of this figure
     * 
     * @param start
     *            the point to become the first point in this figure
     * @param end
     *            the point to become the last point in this figure
     */
    public void setEndpoints(Point start, Point end) {
        setStart(start);
        setEnd(end);
    }

    /**
     * Sets the point at <code>index</code> to the Point <code>pt</code>. If
     * you're going to set multiple Points, use {@link #setPoints(PointList)}.
     * 
     * @param pt
     *            the point
     * @param index
     *            the index
     */
    public void setPoint(Point pt, int index) {
        erase();
        points.setPoint(pt, index);
        repaint();
    }

    /**
     * Sets the list of points to be used by this figure. Removes any previously
     * existing points. This figure will hold onto the given list by reference.
     * 
     * @param points
     *            new set of points
     */
    public void setPoints(PointList points) {
        erase();
        this.points = points;
        repaint();
    }

}
