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

import org.eclipse.swt.graphics.LineAttributes;

/**
 * Provides abstract support for a variety of shapes.
 * <p>
 * When customizing shapes, you shouldn't override paintFigure(). Override
 * fillShape() and outlineShape() methods instead.
 */
public abstract class Shape extends Figure {

    /**
     * The width of this shape's outline.
     * 
     * @deprecated Use {@link #setLineWidth(int)} or
     *             {@link #setLineWidthFloat(float)} instead.
     */
    protected int lineWidth;

    /**
     * Private copy of lineWidth field to track changes. We cannot compare to
     * the float line width because rounding may make them seem equal when they
     * have actually changed.
     * 
     * e.g. someone sets int line width to 5, when float line width was already
     * 5.4, float line width should change to 5.0, but comparing them as ints
     * would suggest there's no change to synchronize.
     */
    private int lastLineWidth;

    /**
     * The line style to be used for this shape's outline.
     * 
     * @deprecated Use {@link #setLineStyle(int)} instead.
     */
    protected int lineStyle;

    /**
     * Private copy of lineStyle field to track changes.
     */
    private int lastLineStyle;

    private LineAttributes lineAttributes;

    private boolean fill;
    private boolean outline;
    private boolean xorFill;
    private boolean xorOutline;
    private Integer antialias;
    private Integer alpha;

    /**
     * Default constructor.
     * 
     * @since 2.0
     */
    public Shape() {
        lineAttributes = new LineAttributes(1.0f);
        fill = true;
        outline = true;
        xorFill = false;
        xorOutline = false;
        antialias = null;
        alpha = null;

        // synchronize parameters
        lineWidth = (int) lineAttributes.width;
        lineStyle = lineAttributes.style;
        lastLineWidth = lineWidth;
        lastLineStyle = lineStyle;
    }

    /**
     * Fills the interior of the shape with the background color.
     * 
     * @param graphics
     *            the graphics object
     */
    protected abstract void fillShape(Graphics graphics);

    /**
     * Outlines this shape using the foreground color.
     * 
     * @param graphics
     *            the graphics object
     */
    protected abstract void outlineShape(Graphics graphics);

    /**
     * Paints the shape. Each shape has an outline to draw, and a region to fill
     * within that outline. Disabled shapes must visually depict the disabled
     * state.
     * 
     * @see Figure#paintFigure(Graphics)
     */
    @Override
    public void paintFigure(Graphics graphics) {
        if (antialias != null) {
            graphics.setAntialias(antialias.intValue());
        }
        if (alpha != null) {
            graphics.setAlpha(alpha.intValue());
        }
        // graphics.rotate(rotation);

        /*
         * see bug #267397: paintFigure was historically not called, disabling
         * setOpaque() behavior, and it was decided to defend the API's
         * consistency.
         */
        // paint background and border
        // super.paintFigure(graphics);

        if (!isEnabled()) {
            graphics.translate(1, 1);
            graphics.setBackgroundColor(ColorConstants.buttonLightest);
            graphics.setForegroundColor(ColorConstants.buttonLightest);

            if (fill) {
                paintFill(graphics);
            }

            if (outline) {
                paintOutline(graphics);
            }

            graphics.setBackgroundColor(ColorConstants.buttonDarker);
            graphics.setForegroundColor(ColorConstants.buttonDarker);
            graphics.translate(-1, -1);
        }

        if (fill) {
            paintFill(graphics);
        }

        if (outline) {
            paintOutline(graphics);
        }
    }

    private void paintOutline(Graphics graphics) {
        // synchronize the line width and style attributes to the
        // public fields which may have been assigned
        // to without our knowledge
        lineAttributes.width = getLineWidthFloat();
        lineAttributes.style = getLineStyle();

        graphics.setLineAttributes(lineAttributes);

        if (xorOutline) {
            /*
             * XORMode is a non-advanced only feature (GDI, not in GDI+ on
             * windows)
             * 
             * Also, XORMode is deprecated in SWT, so this should really be
             * removed completely at some point. XORMode isn't supported on Mac
             * OSX at all.
             */
            boolean oldAdv = graphics.getAdvanced();
            graphics.setAdvanced(false);
            graphics.setXORMode(true);
            outlineShape(graphics);
            graphics.setAdvanced(oldAdv);
        } else {
            outlineShape(graphics);
        }
    }

    private void paintFill(Graphics graphics) {
        if (xorFill) {
            /*
             * XORMode is a non-advanced only feature (GDI, not in GDI+ on
             * windows)
             * 
             * Also, XORMode is deprecated in SWT, so this should really be
             * removed completely at some point. XORMode isn't supported on Mac
             * OSX at all.
             */
            boolean oldAdv = graphics.getAdvanced();
            graphics.setAdvanced(false);
            graphics.setXORMode(true);
            fillShape(graphics);
            graphics.setAdvanced(oldAdv);
        } else {
            fillShape(graphics);
        }
    }

    /**
     * Sets whether this shape should fill its region or not. It repaints this
     * figure.
     * 
     * @param b
     *            fill state
     * @since 2.0
     */
    public void setFill(boolean b) {
        if (fill != b) {
            fill = b;
            repaint();
        }
    }

    /**
     * Sets whether XOR based fill should be used by the shape. It repaints this
     * figure.
     * 
     * @param b
     *            XOR fill state
     * @since 2.0
     */
    public void setFillXOR(boolean b) {
        if (xorFill != b) {
            xorFill = b;
            repaint();
        }
    }

    /**
     * Sets whether the outline should be drawn for this shape.
     * 
     * @param b
     *            <code>true</code> if the shape should be outlined
     * @since 2.0
     */
    public void setOutline(boolean b) {
        if (outline != b) {
            outline = b;
            repaint();
        }
    }

    /**
     * Sets whether XOR based outline should be used for this shape.
     * 
     * @param b
     *            <code>true</code> if the outline should be XOR'ed
     * @since 2.0
     */
    public void setOutlineXOR(boolean b) {
        if (xorOutline != b) {
            xorOutline = b;
            repaint();
        }
    }

    /**
     * Sets whether XOR based fill and XOR based outline should be used for this
     * shape.
     * 
     * @param b
     *            <code>true</code> if the outline and fill should be XOR'ed
     * @since 2.0
     */
    public void setXOR(boolean b) {
        xorOutline = xorFill = b;
        repaint();
    }

    /**
     * @since 3.5
     */
    public Integer getAlpha() {
        return alpha;
    }

    /**
     * @since 3.5
     */
    public Integer getAntialias() {
        return antialias;
    }

    /**
     * Returns line attributes used when drawing this shape.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes
     * 
     *      Performance note: creates and returns a clone.
     * 
     * @return current line attributes
     * @since 3.5
     */
    public LineAttributes getLineAttributes() {
        return SWTGraphics.clone(lineAttributes);
    }

    /**
     * Returns the line width of this shape's outline.
     * 
     * @return the line width
     */
    public int getLineWidth() {
        // synchronize lineWidth field for
        // backwards compatibility
        if (lineWidth != lastLineWidth) {
            lineAttributes.width = lineWidth;
            lastLineWidth = lineWidth;
        }

        return (int) lineAttributes.width;
    }

    /**
     * Returns the line width of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#width
     * 
     * @since 3.5
     */
    public float getLineWidthFloat() {
        // synchronize lineWidth field for
        // backwards compatibility
        if (lineWidth != lastLineWidth) {
            lineAttributes.width = lineWidth;
            lastLineWidth = lineWidth;
        }

        return lineAttributes.width;
    }

    /**
     * Returns the line join style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#join
     * 
     * @since 3.5
     */
    public int getLineJoin() {
        return lineAttributes.join;
    }

    /**
     * Returns the line cap style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#cap
     * 
     * @since 3.5
     */
    public int getLineCap() {
        return lineAttributes.cap;
    }

    /**
     * Returns the line style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#style
     * 
     * @return the line style
     */
    public int getLineStyle() {
        // synchronize line style which may have been assigned
        // to lineStyle field for backwards compatibility
        if (lineStyle != lastLineStyle) {
            lineAttributes.style = lineStyle;
            lastLineStyle = lineStyle;
        }

        return lineAttributes.style;
    }

    /**
     * Returns the line dash style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#dash
     * 
     * @since 3.5
     */
    public float[] getLineDash() {
        if (lineAttributes.dash != null) {
            return lineAttributes.dash.clone();
        } else {
            return null;
        }
    }

    /**
     * Returns the line dash offset of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#dashOffset
     * 
     * @since 3.5
     */
    public float getLineDashOffset() {
        return lineAttributes.dashOffset;
    }

    /**
     * Returns the line dash miter limit of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#miterLimit
     * 
     * @since 3.5
     */
    public float getLineMiterLimit() {
        return lineAttributes.miterLimit;
    }

    /**
     * @since 3.5
     */
    public void setAlpha(Integer value) {
        if (alpha != null) {
            if (!alpha.equals(value)) {
                alpha = value;
                repaint();
            }
        } else if (value != null) {
            alpha = value;
            repaint();
        }
    }

    /**
     * @since 3.5
     */
    @SuppressWarnings("deprecation")
    public void setAlpha(int value) {
        if (alpha != null) {
            if (alpha.intValue() != value) {
                alpha = new Integer(value);
                repaint();
            }
        } else {
            alpha = new Integer(value);
            repaint();
        }
    }

    /**
     * @see org.eclipse.swt.graphics.GC#setAntialias(int)
     * @param value
     * @since 3.5
     */
    public void setAntialias(Integer value) {
        if (antialias != null) {
            if (!antialias.equals(value)) {
                antialias = value;
                repaint();
            }
        } else if (value != null) {
            antialias = value;
            repaint();
        }
    }

    /**
     * @since 3.5
     */
    @SuppressWarnings("deprecation")
    public void setAntialias(int value) {
        if (antialias != null) {
            if (antialias.intValue() != value) {
                antialias = new Integer(value);
                repaint();
            }
        } else {
            antialias = new Integer(value);
            repaint();
        }
    }

    /**
     * Sets all line attributes at once.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes
     * 
     * @param la
     * @since 3.5
     */
    public void setLineAttributes(LineAttributes la) {
        if (!lineAttributes.equals(la)) {
            SWTGraphics.copyLineAttributes(lineAttributes, la);
            repaint();
        }
    }

    /**
     * Sets the line width to be used to outline the shape.
     * 
     * @param w
     *            the new width
     * @since 2.0
     */
    public void setLineWidth(int w) {
        float _w = w;

        if (lineAttributes.width != _w) {
            lineAttributes.width = _w;

            // synchronize lineWidth fields for
            // backwards compatibility
            lineWidth = w;
            lastLineWidth = w;

            repaint();
        }
    }

    /**
     * Sets the line width of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#width
     * 
     * @param value
     * @since 3.5
     */
    public void setLineWidthFloat(float value) {
        if (lineAttributes.width != value) {
            lineAttributes.width = value;

            // synchronize lineWidth fields for
            // backwards compatibility
            lineWidth = (int) value;
            lastLineWidth = (int) value;

            repaint();
        }
    }

    /**
     * Sets the line join style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#join
     * 
     * @param join
     * @since 3.5
     */
    public void setLineJoin(int join) {
        if (lineAttributes.join != join) {
            lineAttributes.join = join;
            repaint();
        }
    }

    /**
     * Sets the line cap style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#cap
     * 
     * @param cap
     * @since 3.5
     */
    public void setLineCap(int cap) {
        if (lineAttributes.cap != cap) {
            lineAttributes.cap = cap;
            repaint();
        }
    }

    /**
     * Sets the line style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#style
     * 
     * @param style
     *            the new line style
     * @since 2.0
     */
    public void setLineStyle(int style) {
        if (lineAttributes.style != style) {
            lineAttributes.style = style;

            // synchronize the lineStyle field
            // to the lineStyle we actually use
            lineStyle = style;
            lastLineStyle = style;

            repaint();
        }
    }

    /**
     * Sets the line dash style of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#dash
     * 
     * @param dash
     * @since 3.5
     */
    public void setLineDash(float[] dash) {
        if ((dash != null) && !dash.equals(lineAttributes.dash)) {
            lineAttributes.dash = dash.clone();
            repaint();
        } else if ((dash == null) && (lineAttributes.dash != null)) {
            lineAttributes.dash = null;
            repaint();
        }
    }

    /**
     * Sets the line dash offset of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#dashOffset
     * 
     * @param dashOffset
     * @since 3.5
     */
    public void setLineDashOffset(float dashOffset) {
        if (lineAttributes.dashOffset != dashOffset) {
            lineAttributes.dashOffset = dashOffset;
            repaint();
        }
    }

    /**
     * Sets the line dash miter limit of this shape's outline.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#miterLimit
     * 
     * @param miterLimit
     * @since 3.5
     */
    public void setLineMiterLimit(float miterLimit) {
        if (lineAttributes.miterLimit != miterLimit) {
            lineAttributes.miterLimit = miterLimit;
            repaint();
        }
    }

}
