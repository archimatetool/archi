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

import org.eclipse.draw2d.geometry.Geometry;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Renders a {@link PointList} as a series of line segments. All points from the
 * {@link PointList} are recognized as a relative points, so you can move/resize
 * this figure normally by calling {@link Figure#setBounds(Rectangle)}.
 * 
 * @since 3.5
 */
public class PolylineShape extends AbstractPointListShape {

    private int tolerance = 2;

    /**
     * @return true if the distance between specified point and closest segment
     *         of this PolyLine is less then {@link PolylineShape#tolerance}
     */
    @Override
    protected boolean shapeContainsPoint(int x, int y) {
        Point location = getLocation();
        return Geometry.polylineContainsPoint(points, x - location.x, y
                - location.y, tolerance);
    }

    @Override
    protected void fillShape(Graphics graphics) {
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.pushState();
        graphics.translate(getLocation());
        graphics.drawPolyline(points);
        graphics.popState();
    }

    /**
     * Setting tolerance parameter. This parameter will be used in
     * {@link PolylineShape#shapeContainsPoint(int, int)}
     */
    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

}
