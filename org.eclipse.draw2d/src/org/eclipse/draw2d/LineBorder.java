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

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.geometry.Insets;

/**
 * Provides for a line border with sides of equal widths.
 */
public class LineBorder extends AbstractBorder {

    private int width = 1;
    private Color color;
    private int style = Graphics.LINE_SOLID;

    /**
     * Constructs a LineBorder with the specified color and of the specified
     * width and style.
     * 
     * @param color
     *            The color of the border.
     * @param width
     *            The width of the border in pixels.
     * @param style
     *            The style of the border. For the list of valid values, see
     *            {@link org.eclipse.draw2d.Graphics}
     * @since 3.5
     */
    public LineBorder(Color color, int width, int style) {
        setColor(color);
        setWidth(width);
        setStyle(style);
    }

    /**
     * Constructs a LineBorder with the specified color and of the specified
     * width.
     * 
     * @param color
     *            The color of the border.
     * @param width
     *            The width of the border in pixels.
     * @since 2.0
     */
    public LineBorder(Color color, int width) {
        this(color, width, Graphics.LINE_SOLID);
    }

    /**
     * Constructs a LineBorder with the specified color and a width of 1 pixel.
     * 
     * @param color
     *            The color of the border.
     * @since 2.0
     */
    public LineBorder(Color color) {
        this(color, 1);
    }

    /**
     * Constructs a black LineBorder with the specified width.
     * 
     * @param width
     *            The width of the border in pixels.
     * @since 2.0
     */
    public LineBorder(int width) {
        this(null, width);
    }

    /**
     * Constructs a default black LineBorder with a width of one pixel.
     * 
     * @since 2.0
     */
    public LineBorder() {
    }

    /**
     * Returns the line color of this border.
     * 
     * @return The line color of this border
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the space used by the border for the figure provided as input. In
     * this border all sides always have equal width.
     * 
     * @param figure
     *            The figure this border belongs to
     * @return This border's insets
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return new Insets(getWidth());
    }

    /**
     * Returns the line width of this border.
     * 
     * @return The line width of this border
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns <code>true</code> since this border is opaque. Being opaque it is
     * responsible to fill in the area within its boundaries.
     * 
     * @return <code>true</code> since this border is opaque
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        tempRect.setBounds(getPaintRectangle(figure, insets));
        if (getWidth() % 2 == 1) {
            tempRect.width--;
            tempRect.height--;
        }
        tempRect.shrink(getWidth() / 2, getWidth() / 2);
        graphics.setLineWidth(getWidth());
        graphics.setLineStyle(getStyle());
        if (getColor() != null)
            graphics.setForegroundColor(getColor());
        graphics.drawRectangle(tempRect);
    }

    /**
     * Sets the line color for this border.
     * 
     * @param color
     *            The line color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the line width for this border.
     * 
     * @param width
     *            The line width
     */
    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    /**
     * Returns the line style for this border.
     * 
     * @return The line style for this border
     * @since 3.5
     */
    public int getStyle() {
        return style;
    }

    /**
     * Sets the line type of this border.
     * 
     * @param style
     *            For the list of valid values, see
     *            {@link org.eclipse.draw2d.Graphics}
     * @since 3.5
     */
    public void setStyle(int style) {
        this.style = style;
    }

}
