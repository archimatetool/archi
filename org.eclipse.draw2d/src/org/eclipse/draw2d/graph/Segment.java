/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Geometry;
import org.eclipse.draw2d.geometry.Point;

/**
 * A Segment representation for the ShortestPathRouting. A segment is a line
 * between two vertices.
 * 
 * This class is for internal use only
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
class Segment {

    Vertex start, end;

    /**
     * Creates a segment between the given start and end points.
     * 
     * @param start
     *            the start vertex
     * @param end
     *            the end vertex
     */
    Segment(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the cosine of the made between this segment and the given segment
     * 
     * @param otherSegment
     *            the other segment
     * @return cosine value (not arc-cos)
     */
    double cosine(Segment otherSegment) {
        double cos = (((start.x - end.x) * (otherSegment.end.x - otherSegment.start.x)) + ((start.y - end.y) * (otherSegment.end.y - otherSegment.start.y)))
                / (getLength() * otherSegment.getLength());
        double sin = (((start.x - end.x) * (otherSegment.end.y - otherSegment.start.y)) - ((start.y - end.y) * (otherSegment.end.x - otherSegment.start.x)));
        if (sin < 0.0)
            return (1 + cos);

        return -(1 + cos);
    }

    /**
     * Returns the cross product of this segment and the given segment
     * 
     * @param otherSegment
     *            the other segment
     * @return the cross product
     */
    long crossProduct(Segment otherSegment) {
        return (((start.x - end.x) * (otherSegment.end.y - end.y)) - ((start.y - end.y) * (otherSegment.end.x - end.x)));
    }

    private double getLength() {
        return (end.getDistance(start));
    }

    /**
     * Returns a number that represents the sign of the slope of this segment.
     * It does not return the actual slope.
     * 
     * @return number representing sign of the slope
     */
    double getSlope() {
        if (end.x - start.x >= 0)
            return (end.y - start.y);
        else
            return -(end.y - start.y);
    }

    /**
     * Returns true if the given segment intersects this segment.
     * 
     * @param sx
     *            start x
     * @param sy
     *            start y
     * @param tx
     *            end x
     * @param ty
     *            end y
     * @return true if the segments intersect
     */
    boolean intersects(int sx, int sy, int tx, int ty) {
        return Geometry.linesIntersect(start.x, start.y, end.x, end.y, sx, sy,
                tx, ty);
    }

    /**
     * Return true if the segment represented by the points intersects this
     * segment.
     * 
     * @param s
     *            start point
     * @param t
     *            end point
     * @return true if the segments intersect
     */
    boolean intersects(Point s, Point t) {
        return intersects(s.x, s.y, t.x, t.y);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return start + "---" + end; //$NON-NLS-1$
    }

}
