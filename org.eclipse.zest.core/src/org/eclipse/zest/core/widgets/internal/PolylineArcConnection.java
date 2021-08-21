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

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * A connection that draws an arc between nodes, based on a given depth for the
 * arc. This connection is drawn as an arc, defined as the circular arc with the
 * chord (ax, ay) - (bx, by) (where a and b are the anchors) and a depth d
 * defined as the maximum distance from any point on the chord (i.e. a vector
 * normal to the chord with magnitude d).
 * 
 * @author Del Myers
 */
// @tag zest(bug(154391-ArcEnds(fix))) : force the endpoints to match by using a
// polyline connection.
// This will be more accurate than the regular ArcConnection, but it may be
// slower.
public class PolylineArcConnection extends PolylineConnection {
    private int depth;
    private boolean inverse = false;
    private static final float PI = (float) 3.14159;
    private RectangleFigure center;

    {
        this.depth = 0;
        center = new RectangleFigure();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.draw2d.Polyline#setPoints(org.eclipse.draw2d.geometry.PointList
     * )
     */
    @Override
    public void setPoints(PointList points) {
        updateArc(points);
    }

    /**
     * This method is not supported by this kind of connection. Points are
     * calculated based on the arc definition.
     */
    @Override
    public void addPoint(Point pt) {
    }

    /**
     * @param depth
     *            the depth to set
     */
    public void setDepth(int depth) {
        this.inverse = depth < 0 ? true : false;
        this.depth = depth;
        updateArc(getPoints());
    }

    protected void updateArc(PointList pointList) {
        if (pointList.size() < 2) {
            return;
        }
        if (center.getParent() == this) {
            remove(center);
        }
        Point start = pointList.getFirstPoint();
        Point end = pointList.getLastPoint();
        if (depth == 0) {
            super.setPoints(pointList);
            return;
        }

        PointList points = new PointList();

        float arcStart = 0;
        float arcEnd = 0;
        float arcLength = 0;
        float cartCenterX = 0;
        float cartCenterY = 0;
        float r = 0;

        float x1 = start.x;
        float y1 = -start.y;
        float x2 = end.x;
        float y2 = -end.y;
        float depth = this.depth;

        if (start.equals(end)) {
            // do a circle
            arcStart = -PI / 2;
            arcLength = PI * 2;
            // @tag drawing(arcs) : try making the center on a line from the
            // center of the parent figure.

            cartCenterX = x1;
            cartCenterY = y1 + depth / 2;
            r = depth / 2;

        } else {
            if (x1 >= x2) {
                depth = -depth;
            }
            // the center of the chord
            float cartChordX = (x2 + x1) / 2;
            float cartChordY = (y2 + y1) / 2;
            float chordLength = (float) Math.sqrt((x1 - x2) * (x1 - x2)
                    + (y1 - y2) * (y1 - y2));
            if (Math.abs(depth) >= chordLength / 2) {
                depth = (chordLength / 3) * (depth / Math.abs(depth));
            }
            r = ((((chordLength / 2) * (chordLength / 2)) + (depth * depth)) / (2 * depth));

            // Find a vector normal to the chord. This will be used for
            // translating the
            // circle back to screen coordinates.
            float chordNormal = 0;

            if (Math.abs(x1 - x2) <= .000001) {
                // slope of 0. NaN is easier to detect than 0.
                chordNormal = Float.NaN;
            } else if (Math.abs(y1 - y2) <= 0.000001) {
                // infinite slope.
                chordNormal = Float.POSITIVE_INFINITY;
            } else {
                chordNormal = -1 * (y2 - y1) / (x2 - x1);
            }

            float th1;
            if (Float.isNaN(chordNormal)) {
                cartCenterX = (y1 > y2) ? (cartChordX - r + (depth))
                        : (cartChordX + r - (depth));
                cartCenterY = cartChordY;
                th1 = PI / 2;
            } else if (Float.isInfinite(chordNormal)) {
                cartCenterX = cartChordX;
                cartCenterY = cartChordY + r - (depth);
                th1 = 0;
            } else {
                // assume that the center of the chord is on the origin.
                th1 = (float) Math.atan(chordNormal);
                cartCenterX = (r - (depth)) * (float) Math.sin(th1)
                        + cartChordX;// cartChordX+r
                // -depth;
                cartCenterY = (r - (depth)) * (float) Math.cos(th1)
                        + cartChordY;// cartChordY+r-depth;

            }
            // figure out the new angles
            // translate the points to the center of the circle
            float cartArcX1 = x1 - cartCenterX;
            float cartArcY1 = y1 - cartCenterY;
            float cartArcX2 = x2 - cartCenterX;
            float cartArcY2 = y2 - cartCenterY;

            // calculate the length of the arc
            arcStart = angleRadians(cartArcX1, cartArcY1);
            arcEnd = angleRadians(cartArcX2, cartArcY2);

            if (arcEnd < arcStart) {
                arcEnd = arcEnd + PI + PI;
            }

            // make sure that we are between the two nodes.
            arcLength = arcEnd - arcStart;
            float pad = PI / Math.abs(r);
            arcStart += pad;
            arcEnd -= pad;
            arcLength = (arcEnd) - (arcStart);
            if (inverse) {
                arcLength = (2 * PI - arcLength);
            }
        }
        // calculate the points
        r = Math.abs(r);
        float x = 0, y = 0;
        Point p = null;
        points.addPoint(start);
        float length = arcLength * r;

        int steps = (int) length / 16;
        if (steps < 10 && length > 10) {
            steps = 10;
        }
        if (arcLength < PI / 4 && steps > 6) {
            steps = 6;
        }
        if (steps < 4 && length > 4) {
            steps = 4;
        }
        float stepSize = arcLength / steps;
        if (inverse) {
            float step = arcStart - stepSize;
            for (int i = 1; i < steps; i++, step -= stepSize) {
                x = (r) * (float) Math.cos(step) + cartCenterX;
                y = (r) * (float) Math.sin(step) + cartCenterY;
                p = new Point(Math.round(x), Math.round(-y));
                points.addPoint(p);
            }
        } else {
            float step = stepSize + arcStart;
            for (int i = 1; i < steps; i++, step += stepSize) {
                x = (r) * (float) Math.cos(step) + cartCenterX;
                y = (r) * (float) Math.sin(step) + cartCenterY;
                p = new Point(Math.round(x), Math.round(-y));
                points.addPoint(p);
            }
        }
        points.addPoint(end);

        super.setPoints(points);
    }

    /*
     * Gets an angle in radians for the x, y coordinates. The angle will be
     * between 0 and 2PI.
     */
    float angleRadians(float x, float y) {
        float theta = (float) Math.atan(y / x);
        switch (findQuadrant(x, y)) {
        case 1:
            return theta;
        case 2:
            return (theta + PI);
        case 4:
            theta = (theta + PI);
        case 3:
            return (theta + PI);
        default:
            return theta;
        }

    }

    // find the quadrant, assume points are centered at 0,0
    protected int findQuadrant(float x, float y) {
        if (y > 0) {
            if (x > 0) {
                return 1;
            } else {
                return 2;
            }
        } else {
            if (x > 0) {
                return 4;
            } else {
                return 3;
            }
        }
    }
}
