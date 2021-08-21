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
 * Renders a {@link org.eclipse.draw2d.geometry.PointList} as a polygonal shape
 * scaled in accordance with bounds to fill whole figure. This class is similar
 * to {@link PolygonShape}, except the polygon should be scaled
 * expanded/compressed to fit in current bounds.
 * 
 * @since 3.5
 */
@SuppressWarnings("deprecation")
public class ScalablePolygonShape extends AbstractPointListShape {

    private static final Rectangle TEMPLATEBOUNDS = Rectangle.SINGLETON;

    private PointList scaledPoints;

    @Override
    protected boolean shapeContainsPoint(int x, int y) {
        Point location = getLocation();
        return Geometry.polygonContainsPoint(getScaledPoints(), x - location.x,
                y - location.y);
    }

    @Override
    protected void fillShape(Graphics graphics) {
        graphics.pushState();
        graphics.translate(getLocation());
        graphics.fillPolygon(getScaledPoints());
        graphics.popState();
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.pushState();
        graphics.translate(getLocation());
        graphics.drawPolygon(getScaledPoints());
        graphics.popState();
    }

    private Rectangle getTemplateBounds() {
        TEMPLATEBOUNDS.setLocation(0, 0);
        TEMPLATEBOUNDS.setSize(0, 0);
        int[] intArray = points.toIntArray();
        for (int i = 0; i < intArray.length;) {
            int x = intArray[i++];
            if (x > TEMPLATEBOUNDS.width) {
                TEMPLATEBOUNDS.width = x;
            }
            int y = intArray[i++];
            if (y > TEMPLATEBOUNDS.height) {
                TEMPLATEBOUNDS.height = y;
            }
        }
        return TEMPLATEBOUNDS;
    }

    public PointList getScaledPoints() {
        if (scaledPoints != null) {
            return scaledPoints;
        }
        Rectangle pointsBounds = getTemplateBounds();
        Rectangle actualBounds = getBounds();
        double xScale = actualBounds.width > lineWidth ? ((double) actualBounds.width - lineWidth)
                / pointsBounds.width
                : 0;
        double yScale = actualBounds.height > lineWidth ? ((double) actualBounds.height - lineWidth)
                / pointsBounds.height
                : 0;
        double halfLineWidth = ((double) lineWidth) / 2;

        int[] pointsArray = points.getCopy().toIntArray();
        for (int i = 0; i < pointsArray.length; i = i + 2) {
            pointsArray[i] = (int) (Math.floor(pointsArray[i] * xScale) + halfLineWidth);
            pointsArray[i + 1] = (int) (Math.floor(pointsArray[i + 1] * yScale) + halfLineWidth);
        }
        return scaledPoints = new PointList(pointsArray);
    }

    @Override
    public void addPoint(Point pt) {
        scaledPoints = null;
        super.addPoint(pt);
    }

    @Override
    public void insertPoint(Point pt, int index) {
        scaledPoints = null;
        super.insertPoint(pt, index);
    }

    @Override
    public void removeAllPoints() {
        scaledPoints = null;
        super.removeAllPoints();
    }

    @Override
    public void removePoint(int index) {
        scaledPoints = null;
        super.removePoint(index);
    }

    @Override
    public void setStart(Point start) {
        scaledPoints = null;
        super.setStart(start);
    }

    @Override
    public void setEnd(Point end) {
        scaledPoints = null;
        super.setEnd(end);
    }

    @Override
    public void setPoint(Point pt, int index) {
        scaledPoints = null;
        super.setPoint(pt, index);
    }

    @Override
    public void setPoints(PointList points) {
        scaledPoints = null;
        super.setPoints(points);
    }

    @Override
    public void setBounds(Rectangle rect) {
        scaledPoints = null;
        super.setBounds(rect);
    }

    @Override
    public void setLineWidth(int w) {
        scaledPoints = null;
        super.setLineWidth(w);
    }

}
