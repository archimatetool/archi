/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * A Utilities class for geometry operations.
 * 
 * @author Pratik Shah
 * @author Alexander Nyssen
 * @since 3.1
 */
public class Geometry {

    /**
     * Determines whether the two line segments p1->p2 and p3->p4, given by
     * p1=(x1, y1), p2=(x2,y2), p3=(x3,y3), p4=(x4,y4) intersect. Two line
     * segments are regarded to be intersecting in case they share at least one
     * common point, i.e if one of the two line segments starts or ends on the
     * other line segment or the line segments are collinear and overlapping,
     * then they are as well considered to be intersecting.
     * 
     * @param x1
     *            x coordinate of starting point of line segment 1
     * @param y1
     *            y coordinate of starting point of line segment 1
     * @param x2
     *            x coordinate of ending point of line segment 1
     * @param y2
     *            y coordinate of ending point of line segment 1
     * @param x3
     *            x coordinate of the starting point of line segment 2
     * @param y3
     *            y coordinate of the starting point of line segment 2
     * @param x4
     *            x coordinate of the ending point of line segment 2
     * @param y4
     *            y coordinate of the ending point of line segment 2
     * 
     * @return <code>true</code> if the two line segments formed by the given
     *         coordinates share at least one common point.
     * 
     * @since 3.1
     */
    public static boolean linesIntersect(int x1, int y1, int x2, int y2,
            int x3, int y3, int x4, int y4) {

        // calculate bounding box of segment p1->p2
        int bb1_x = Math.min(x1, x2);
        int bb1_y = Math.min(y1, y2);
        int bb2_x = Math.max(x1, x2);
        int bb2_y = Math.max(y1, y2);

        // calculate bounding box of segment p3->p4
        int bb3_x = Math.min(x3, x4);
        int bb3_y = Math.min(y3, y4);
        int bb4_x = Math.max(x3, x4);
        int bb4_y = Math.max(y3, y4);

        // check if bounding boxes intersect
        if (!(bb2_x >= bb3_x && bb4_x >= bb1_x && bb2_y >= bb3_y && bb4_y >= bb1_y)) {
            // if bounding boxes do not intersect, line segments cannot
            // intersect either
            return false;
        }

        // If p3->p4 is inside the triangle p1-p2-p3, then check whether the
        // line p1->p2 crosses the line p3->p4.
        long p1p3_x = (long) x1 - x3;
        long p1p3_y = (long) y1 - y3;
        long p2p3_x = (long) x2 - x3;
        long p2p3_y = (long) y2 - y3;
        long p3p4_x = (long) x3 - x4;
        long p3p4_y = (long) y3 - y4;
        if (productSign(crossProduct(p2p3_x, p2p3_y, p3p4_x, p3p4_y),
                crossProduct(p3p4_x, p3p4_y, p1p3_x, p1p3_y)) >= 0) {
            long p2p1_x = (long) x2 - x1;
            long p2p1_y = (long) y2 - y1;
            long p1p4_x = (long) x1 - x4;
            long p1p4_y = (long) y1 - y4;
            return productSign(crossProduct(-p1p3_x, -p1p3_y, p2p1_x, p2p1_y),
                    crossProduct(p2p1_x, p2p1_y, p1p4_x, p1p4_y)) <= 0;
        }
        return false;
    }

    private static int productSign(long x, long y) {
        if (x == 0 || y == 0) {
            return 0;
        } else if (x < 0 ^ y < 0) {
            return -1;
        }
        return 1;
    }

    private static long crossProduct(long x1, long y1, long x2, long y2) {
        return x1 * y2 - x2 * y1;
    }

    /**
     * @see PointList#polylineContainsPoint(int, int, int)
     * @since 3.5
     */
    public static boolean polylineContainsPoint(PointList points, int x, int y,
            int tolerance) {
        int coordinates[] = points.toIntArray();
        /*
         * For each segment of PolyLine calling isSegmentPoint
         */
        for (int index = 0; index < coordinates.length - 3; index += 2) {
            if (segmentContainsPoint(coordinates[index],
                    coordinates[index + 1], coordinates[index + 2],
                    coordinates[index + 3], x, y, tolerance)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if the least distance between point (px,py) and segment
     *         (x1,y1) - (x2,y2) is less then specified tolerance
     */
    private static boolean segmentContainsPoint(int x1, int y1, int x2, int y2,
            int px, int py, int tolerance) {
        /*
         * Point should be located inside Rectangle(x1 -+ tolerance, y1 -+
         * tolerance, x2 +- tolerance, y2 +- tolerance)
         */
        Rectangle lineBounds = Rectangle.SINGLETON;
        lineBounds.setSize(0, 0);
        lineBounds.setLocation(x1, y1);
        lineBounds.union(x2, y2);
        lineBounds.expand(tolerance, tolerance);
        if (!lineBounds.contains(px, py)) {
            return false;
        }

        /*
         * If this is horizontal, vertical line or dot then the distance between
         * specified point and segment is not more then tolerance (due to the
         * lineBounds check above)
         */
        if (x1 == x2 || y1 == y2) {
            return true;
        }

        /*
         * Calculating square distance from specified point to this segment
         * using formula for Dot product of two vectors.
         */
        long v1x = x2 - x1;
        long v1y = y2 - y1;
        long v2x = px - x1;
        long v2y = py - y1;
        long numerator = v2x * v1y - v1x * v2y;
        long denominator = v1x * v1x + v1y * v1y;
        long squareDistance = numerator * numerator / denominator;
        return squareDistance <= tolerance * tolerance;
    }

    /**
     * One simple way of finding whether the point is inside or outside a simple
     * polygon is to test how many times a ray starting from the point
     * intersects the edges of the polygon. If the point in question is not on
     * the boundary of the polygon, the number of intersections is an even
     * number if the point is outside, and it is odd if inside.
     * 
     * @see PointList#polygonContainsPoint(int, int)
     * @since 3.5
     */
    public static boolean polygonContainsPoint(PointList points, int x, int y) {
        boolean isOdd = false;
        int coordinates[] = points.toIntArray();
        int n = coordinates.length;
        if (n > 3) { // If there are at least 2 Points (4 ints)
            int x1, y1;
            int x0 = coordinates[n - 2];
            int y0 = coordinates[n - 1];

            for (int i = 0; i < n; x0 = x1, y0 = y1) {
                x1 = coordinates[i++];
                y1 = coordinates[i++];
                if (!segmentContaintPoint(y0, y1, y)) {
                    // Current edge has no intersection with the point by Y
                    // coordinates
                    continue;
                }
                int crossProduct = crossProduct(x1, y1, x0, y0, x, y);
                if (crossProduct == 0) {
                    // cross product == 0 only if this point is on the line
                    // containing selected edge
                    if (segmentContaintPoint(x0, x1, x)) {
                        // This point is on the edge
                        return true;
                    }
                    // This point is outside the edge - simply skipping possible
                    // intersection (no parity changes)
                } else if ((y0 <= y && y < y1 && crossProduct > 0)
                        || (y1 <= y && y < y0 && crossProduct < 0)) {
                    // has intersection
                    isOdd = !isOdd;
                }
            }
            return isOdd;
        }
        return false;
    }

    /**
     * @return true if segment with two ends x0, x1 contains point x
     */
    private static boolean segmentContaintPoint(int x0, int x1, int x) {
        return !((x < x0 && x < x1) || (x > x0 && x > x1));
    }

    /**
     * Calculating cross product of two vectors: 1. [ax - cx, ay - cx] 2. [bx -
     * cx, by - cy]
     */
    private static int crossProduct(int ax, int ay, int bx, int by, int cx,
            int cy) {
        return (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
    }

}