/*******************************************************************************
 * Copyright (c) 2010  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Anchor for rounded rectangles which is always on a line between the center
 * and the reference point.
 * 
 * @author Benjamin Schwertfeger (benjamin.schwertfeger@itemis.de)
 * @since 3.8
 */
public class RoundedRectangleAnchor extends ChopboxAnchor {

    private static final int LEFT = 1;

    private static final int MIDDLE = 2;

    private static final int RIGHT = 4;

    private static final int TOP = 8;

    private static final int CENTER = 16;

    private static final int BOTTOM = 32;

    private final Dimension dimension;

    /**
     * Rounded Rectangle getCornerDimension should be public #302836 then
     * Rounded Rectangle would be sufficient.
     */
    public RoundedRectangleAnchor(final RoundedRectangle figure) {
        super(figure);
        dimension = null;
    }

    /**
     * Rounded Rectangle getCornerDimension should be public #302836 then
     * Rounded Rectangle would be sufficient.
     */
    public RoundedRectangleAnchor(final Figure figure, final Dimension corners) {
        super(figure);
        dimension = corners;
    }

    /**
     * Calculates the position with ChopboxAnchor#getLocation() and if the
     * anchor is not at the rounded corners, the result is returned. If the
     * anchor point should be at a corner, the rectangle for the ellipse is
     * determined and ellipseAnchorGetLocation returns the two intersection
     * points between the line from calculated anchor point and the center of
     * the rounded rectangle.
     * 
     * @return The anchor location
     */
    @Override
    public Point getLocation(final Point ref) {
        Dimension corner = dimension;
        if (getOwner() instanceof RoundedRectangle) {
            corner = ((RoundedRectangle) getOwner()).getCornerDimensions();
        }
        final Point location = super.getLocation(ref);
        final Rectangle r = Rectangle.SINGLETON;
        r.setBounds(getOwner().getBounds());
        r.translate(-1, -1);
        r.resize(1, 1);
        getOwner().translateToAbsolute(r);
        final int yTop = r.y + corner.height / 2;
        final int yBottom = r.y + r.height - corner.height / 2;
        final int xLeft = r.x + corner.width / 2;
        final int xRight = r.x + r.width - corner.width / 2;
        int pos = 0;
        if (location.x < xLeft) {
            pos = LEFT;
        } else if (location.x > xRight) {
            pos = RIGHT;
        } else {
            pos = MIDDLE;
        }
        if (location.y < yTop) {
            pos |= TOP;
        } else if (location.y > yBottom) {
            pos |= BOTTOM;
        } else {
            pos += CENTER;
        }
        switch (pos) {
        case TOP | MIDDLE:
        case CENTER | LEFT:
        case CENTER | RIGHT:
        case BOTTOM | MIDDLE:
            return new Point(location.x, location.y);
        case TOP | LEFT:
            return ellipseAnchorGetLocation(location, new Rectangle(r.x, r.y,
                    corner.width, corner.height), getOwner().getBounds()
                    .getCenter())[0];
        case TOP | RIGHT:
            return ellipseAnchorGetLocation(location,
                    new Rectangle(r.x + r.width - corner.width, r.y,
                            corner.width, corner.height), getOwner()
                            .getBounds().getCenter())[1];
        case CENTER | MIDDLE:
            // default for reference inside Figure
            return new Point(r.x, r.y + r.height / 2);
        case BOTTOM | LEFT:
            return ellipseAnchorGetLocation(location, new Rectangle(r.x, r.y
                    + r.height - corner.height, corner.width, corner.height),
                    getOwner().getBounds().getCenter())[0];
        case BOTTOM | RIGHT:
            return ellipseAnchorGetLocation(location, new Rectangle(r.x
                    + r.width - corner.width, r.y + r.height - corner.height,
                    corner.width, corner.height), getOwner().getBounds()
                    .getCenter())[1];
        default:
            throw new IllegalStateException(
                    "Calculation of RoundedRectangleAnchor missed. Rect: " + r //$NON-NLS-1$
                            + " Point: " + location); //$NON-NLS-1$
        }
    }

    /**
     * Calculation of intersections points of one ellipse, represented by r, and
     * the line between ref and c.
     * 
     * @param reference
     *            reference point for line end (end of the line)
     * @param r
     *            the rectangle of the ellipse, where the intersection points
     *            are wanted for
     * @param center
     *            center of the figure (start of the line)
     * @return Two intersection points of circle with the line. They could be
     *         equal, if the line only tangents. Returns null, if no
     *         intersection was found.
     */
    private static Point[] ellipseAnchorGetLocation(final Point ref,
            final Rectangle r, Point c) {

        // Move the coordinates so that the center of ellipse is in the origin.
        final PrecisionPoint reference = new PrecisionPoint(r.getCenter()
                .negate().translate(ref));
        final PrecisionPoint center = new PrecisionPoint(r.getCenter().negate()
                .translate(c));
        // Transform the coordinate axis, to make the ellipse a circle with
        // radius 1.
        final double referenceX = reference.preciseX() * 2.0 / r.width;
        final double referenceY = reference.preciseY() * 2.0 / r.height;
        final double centerX = center.preciseX() * 2.0 / r.width;
        final double centerY = center.preciseY() * 2.0 / r.height;

        // the line is y=a*x+b detemine a and b
        final double a = (referenceY - centerY) / (referenceX - centerX);
        final double b = centerY - (centerX * a);

        // circle is x^2+y^2=1. With the line this leads to
        //
        // x_{1/2} = +-Sqrt( (1-b*b)/((a*a+1)^2) + (a*a*b*b)/(a*a+1) ) -
        // (a*b)/a*a+1
        //
        // y = a*x+b
        final double bSqr = Math.pow(b, 2);
        final double aSqr = Math.pow(a, 2);
        final double xSqrt = Math.sqrt((1 - bSqr) / (aSqr + 1) + (aSqr * bSqr)
                / (Math.pow(aSqr + 1, 2)));
        if (xSqrt == Double.NaN) {
            // no intersection found
            return null;
        }
        final double x1 = -xSqrt - (a * b) / (Math.pow(a, 2) + 1);
        final double x2 = +xSqrt - (a * b) / (Math.pow(a, 2) + 1);
        final double y1 = a * x1 + b;
        final double y2 = a * x2 + b;
        final Point p1 = new PrecisionPoint(x1 * r.width / 2.0, y1 * r.height
                / 2.0);
        final Point p2 = new PrecisionPoint(x2 * r.width / 2.0, y2 * r.height
                / 2.0);
        return new Point[] { r.getCenter().translate(p1),
                r.getCenter().translate(p2) };
    }
}