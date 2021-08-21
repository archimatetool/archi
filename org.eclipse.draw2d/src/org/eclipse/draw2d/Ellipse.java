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
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An figure that draws an ellipse filling its bounds.
 */
public class Ellipse extends Shape {
    /**
     * Constructs a new Ellipse with the default values of a Shape.
     * 
     * @since 2.0
     */
    public Ellipse() {
    }

    /**
     * Returns <code>true</code> if the given point (x,y) is contained within
     * this ellipse.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @return <code>true</code>if the given point is contained
     */
    @Override
    public boolean containsPoint(int x, int y) {
        if (!super.containsPoint(x, y)) {
            return false;
        } else {
            Rectangle r = getBounds();
            long ux = x - r.x - r.width / 2;
            long uy = y - r.y - r.height / 2;
            return ((ux * ux) << 10) / (r.width * r.width) + ((uy * uy) << 10)
                    / (r.height * r.height) <= 256;
        }
    }

    /**
     * Fills the ellipse.
     * 
     * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void fillShape(Graphics graphics) {
        graphics.fillOval(getOptimizedBounds());
    }

    /**
     * Outlines the ellipse.
     * 
     * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.drawOval(getOptimizedBounds());
    }

    private Rectangle getOptimizedBounds() {
        float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
        int inset1 = (int) Math.floor(lineInset);
        int inset2 = (int) Math.ceil(lineInset);

        Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
        r.x += inset1;
        r.y += inset1;
        r.width -= inset1 + inset2;
        r.height -= inset1 + inset2;
        return r;
    }
}
