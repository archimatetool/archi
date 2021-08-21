/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Geometry;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Renders a {@link PointList} as a series of line segments. A Polyline figure
 * should be positioned by manipulating its points, <EM>NOT</EM> by calling
 * {@link Figure#setBounds(Rectangle)}.
 * <P>
 * A polyline's bounds will be calculated automatically based on its PointList.
 * The bounds will be the smallest Rectangle large enough to render the line
 * properly. Children should not be added to a Polyline and will not affect the
 * bounds calculation.
 */
public class Polyline extends AbstractPointListShape {

    private int tolerance = 2;
    private static final Rectangle LINEBOUNDS = Rectangle.SINGLETON;

    {
        setFill(false);
        bounds = null;
    }

    /**
     * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        int tolerance = (int) Math.max(getLineWidthFloat() / 2.0f,
                this.tolerance);
        LINEBOUNDS.setBounds(getBounds());
        LINEBOUNDS.expand(tolerance, tolerance);
        if (!LINEBOUNDS.contains(x, y))
            return false;
        return shapeContainsPoint(x, y) || childrenContainsPoint(x, y);
    }

    @Override
    protected boolean shapeContainsPoint(int x, int y) {
        return Geometry.polylineContainsPoint(points, x, y, tolerance);
    }

    /**
     * Null implementation for a line.
     * 
     * @see org.eclipse.draw2d.Shape#fillShape(Graphics)
     */
    @Override
    protected void fillShape(Graphics g) {
    }

    /**
     * @see org.eclipse.draw2d.IFigure#getBounds()
     */
    @Override
    public Rectangle getBounds() {
        if (bounds == null) {
            int expand = (int) (getLineWidthFloat() / 2.0f);
            bounds = getPoints().getBounds().getExpanded(expand, expand);
        }
        return bounds;
    }

    /**
     * @return <code>false</code> because Polyline's aren't filled
     */
    @Override
    public boolean isOpaque() {
        return false;
    }

    /**
     * @see Shape#outlineShape(Graphics)
     */
    @Override
    protected void outlineShape(Graphics g) {
        g.drawPolyline(points);
    }

    /**
     * @see Figure#primTranslate(int, int)
     */
    @Override
    public void primTranslate(int x, int y) {
    }

    /**
     * Erases the Polyline and removes all of its {@link Point Points}.
     * 
     * @since 2.0
     */
    @Override
    public void removeAllPoints() {
        super.removeAllPoints();
        bounds = null;
    }

    /**
     * @see org.eclipse.draw2d.Shape#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        if (getLineWidthFloat() == w) {
            return;
        }
        if (w < getLineWidthFloat()) { // The bounds will become smaller, so
                                        // erase must occur first.
            erase();
        }
        bounds = null;
        super.setLineWidthFloat(w);
    }

    /**
     * Sets the list of points to be used by this polyline connection. Removes
     * any previously existing points. The polyline will hold onto the given
     * list by reference.
     * 
     * @param points
     *            new set of points
     * @since 2.0
     */
    @Override
    public void setPoints(PointList points) {
        super.setPoints(points);
        firePropertyChange(Connection.PROPERTY_POINTS, null, points);
    }

    /**
     * Sets the tolerance
     * 
     * @param tolerance
     *            the new tolerance value of the Polyline
     */
    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public void repaint() {
        bounds = null;
        super.repaint();
    }
}