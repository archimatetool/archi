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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draws a Rectangle whose corners are rounded in appearance. The size of the
 * rectangle is determined by the bounds set to it.
 */
public class RoundedRectangle extends Shape {

    /**
     * The width and height radii applied to each corner.
     * 
     * @deprecated Use {@link #getCornerDimensions()} instead.
     */
    protected Dimension corner = new Dimension(8, 8);

    /**
     * Constructs a round cornered rectangle.
     */
    public RoundedRectangle() {
    }

    /**
     * @see Shape#fillShape(Graphics)
     */
    @Override
    protected void fillShape(Graphics graphics) {
        graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
    }

    /**
     * @see Shape#outlineShape(Graphics)
     */
    @Override
    protected void outlineShape(Graphics graphics) {
        float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
        int inset1 = (int) Math.floor(lineInset);
        int inset2 = (int) Math.ceil(lineInset);

        Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
        r.x += inset1;
        r.y += inset1;
        r.width -= inset1 + inset2;
        r.height -= inset1 + inset2;

        graphics.drawRoundRectangle(r,
                Math.max(0, corner.width - (int) lineInset),
                Math.max(0, corner.height - (int) lineInset));
    }

    /**
     * Sets the dimensions of each corner. This will form the radii of the arcs
     * which form the corners.
     * 
     * @param d
     *            the dimensions of the corner
     * @since 2.0
     */
    public void setCornerDimensions(Dimension d) {
        corner.width = d.width;
        corner.height = d.height;
    }

    /**
     * Returns the dimensions used for each corner.
     * 
     * @return the dimensions of the corner.
     * @since 3.7
     */
    public Dimension getCornerDimensions() {
        return corner.getCopy();
    }
}
