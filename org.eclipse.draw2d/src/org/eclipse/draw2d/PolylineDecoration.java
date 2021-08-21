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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Transform;

/**
 * A decorative Figure intended to be placed on a {@link Polyline}. It has the
 * default shape of right-pointing triangle.
 */
public class PolylineDecoration extends Polyline implements RotatableDecoration {

    /** A triangle template */
    public static final PointList TRIANGLE_TIP = new PointList();

    static {
        TRIANGLE_TIP.addPoint(-1, 1);
        TRIANGLE_TIP.addPoint(0, 0);
        TRIANGLE_TIP.addPoint(-1, -1);
    }

    private Point location = new Point();
    private PointList template = TRIANGLE_TIP;
    private Transform transform = new Transform();

    /**
     * Constructs a PolylineDecoration. Defaults the PolylineDecoration to fill
     * its region with black.
     * 
     * @since 2.0
     */
    public PolylineDecoration() {
        setBackgroundColor(ColorConstants.black);
        setScale(7, 3);
    }

    /**
     * @see Polyline#getPoints()
     */
    @Override
    public PointList getPoints() {
        if (points == null) {
            points = new PointList();
            for (int i = 0; i < template.size(); i++)
                points.addPoint(transform.getTransformed(template.getPoint(i)));
        }
        return points;
    }

    /**
     * @see IFigure#setLocation(Point)
     */
    @Override
    public void setLocation(Point p) {
        points = null;
        bounds = null;
        location.setLocation(p);
        transform.setTranslation(p.x, p.y);
    }

    /**
     * Sets the PolylineDecoration's point template. This template is an outline
     * of the PolylineDecoration's region. (The default value is TRIANGLE_TIP
     * which is a triangle whose tip is pointing to the right).
     * 
     * @param pl
     *            the template
     * @since 2.0
     */
    public void setTemplate(PointList pl) {
        erase();
        template = pl;
        points = null;
        bounds = null;
        repaint();
    }

    /**
     * Sets the amount of scaling to be done along X and Y axes on the
     * PolylineDecoration's template.
     * 
     * @param x
     *            the x scale
     * @param y
     *            the y scale
     * @since 2.0
     */
    public void setScale(double x, double y) {
        points = null;
        bounds = null;
        transform.setScale(x, y);
    }

    /**
     * @see RotatableDecoration#setReferencePoint(Point)
     */
    @Override
    public void setReferencePoint(Point ref) {
        Point pt = Point.SINGLETON;
        pt.setLocation(ref);
        pt.negate().translate(location);
        setRotation(Math.atan2(pt.y, pt.x));
    }

    /**
     * Sets the angle by which rotation is to be done on the PolylineDecoration.
     * 
     * @param angle
     *            the angle of rotation
     * @since 2.0
     */
    public void setRotation(double angle) {
        points = null;
        bounds = null;
        transform.setRotation(angle);
    }

}
