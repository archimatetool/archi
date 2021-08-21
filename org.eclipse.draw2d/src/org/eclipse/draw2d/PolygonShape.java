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

/**
 * Renders a {@link org.eclipse.draw2d.geometry.PointList} as a polygonal shape.
 * This class is similar to {@link PolylineShape}, except the
 * {@link org.eclipse.draw2d.geometry.PointList} is closed and can be filled in
 * as a solid shape.
 * 
 * @see PolylineShape
 * @since 3.5
 */
public class PolygonShape extends AbstractPointListShape {

    @Override
    protected boolean shapeContainsPoint(int x, int y) {
        Point location = getLocation();
        return Geometry.polygonContainsPoint(points, x - location.x, y
                - location.y);
    }

    @Override
    protected void fillShape(Graphics graphics) {
        graphics.pushState();
        graphics.translate(getLocation());
        graphics.fillPolygon(getPoints());
        graphics.popState();
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.pushState();
        graphics.translate(getLocation());
        graphics.drawPolygon(getPoints());
        graphics.popState();
    }

}
