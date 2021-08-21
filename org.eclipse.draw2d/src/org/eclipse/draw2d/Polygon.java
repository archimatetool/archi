/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alex Selkov - Fix for Bug# 22701
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Geometry;

/**
 * Renders a {@link org.eclipse.draw2d.geometry.PointList} as a polygonal shape.
 * This class is similar to Polyline, except the PointList is closed and can be
 * filled in as a solid shape.
 * 
 * @see Polyline
 */
public class Polygon extends Polyline {

    /**
     * Returns whether the point (x,y) is contained inside this polygon.
     * 
     * @param x
     *            the X coordinate
     * @param y
     *            the Y coordinate
     * @return whether the point (x,y) is contained in this polygon
     */
    @Override
    public boolean containsPoint(int x, int y) {
        if (!getBounds().contains(x, y))
            return false;
        return shapeContainsPoint(x, y) || childrenContainsPoint(x, y);
    }

    @Override
    protected boolean shapeContainsPoint(int x, int y) {
        return Geometry.polygonContainsPoint(points, x, y);
    }

    /**
     * Fill the Polygon with the background color set by <i>g</i>.
     * 
     * @param g
     *            the Graphics object
     * @since 2.0
     */
    @Override
    protected void fillShape(Graphics g) {
        g.fillPolygon(getPoints());
    }

    /**
     * Draw the outline of the Polygon.
     * 
     * @param g
     *            the Graphics object
     * @since 2.0
     */
    @Override
    protected void outlineShape(Graphics g) {
        g.drawPolygon(getPoints());
    }

}
