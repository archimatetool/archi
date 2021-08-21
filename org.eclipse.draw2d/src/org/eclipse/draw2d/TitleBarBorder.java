/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Michael Houston (schmeeky@gmail.com) - Bugzilla 259088
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Border provides a title bar on the Figure for which this is the border of.
 * Generally used in conjunction with other borders to create window-like
 * effects. Also provides for alignment of the text in the bar.
 * 
 * @see FrameBorder
 */
public class TitleBarBorder extends AbstractLabeledBorder {

    private static Color defaultBackgroundColor = ColorConstants.menuBackgroundSelected;
    private static Color defaultForegroundColor = ColorConstants.menuForegroundSelected;

    private int textAlignment = PositionConstants.LEFT;
    private Insets padding = new Insets(1, 3, 2, 2);
    private Color fillColor = defaultBackgroundColor;

    /**
     * Constructs a TitleBarBorder with its label set to the name of this class.
     * 
     * @since 2.0
     */
    public TitleBarBorder() {
        setTextColor(defaultForegroundColor);
    }

    /**
     * Constructs a TitleBarBorder with its label set to the passed String.
     * 
     * @param s
     *            text of the label
     * @since 2.0
     */
    public TitleBarBorder(String s) {
        this();
        setLabel(s);
    }

    /**
     * Calculates and returns the Insets for this border.
     * 
     * @param figure
     *            the figure on which Insets calculations are based
     * @return the calculated Insets
     * @since 2.0
     */
    @Override
    protected Insets calculateInsets(IFigure figure) {
        return new Insets(getTextExtents(figure).height + padding.getHeight(),
                0, 0, 0);
    }

    /**
     * Returns the background Color of this TitleBarBorder.
     * 
     * @return the background color
     * @since 2.0
     */
    protected Color getBackgroundColor() {
        return fillColor;
    }

    /**
     * Returns this TitleBarBorder's padding. Padding provides spacing along the
     * sides of the TitleBarBorder. The default value is no padding along all
     * sides.
     * 
     * @return the Insets representing the space along the sides of the
     *         TitleBarBorder
     * @since 2.0
     */
    protected Insets getPadding() {
        return padding;
    }

    /**
     * Overwritten to handle padding appropriately.
     * 
     * @see org.eclipse.draw2d.AbstractLabeledBorder#getPreferredSize(org.eclipse.draw2d.IFigure)
     */
    @Override
    public Dimension getPreferredSize(IFigure fig) {
        return super.getPreferredSize(fig).getExpanded(padding.getWidth(), 0);
    }

    /**
     * Returns the alignment of the text in the title bar. Possible values are
     * {@link PositionConstants#LEFT}, {@link PositionConstants#CENTER} and
     * {@link PositionConstants#RIGHT}.
     * 
     * @return the text alignment
     * @since 2.0
     */
    public int getTextAlignment() {
        return textAlignment;
    }

    /**
     * Returns <code>true</code> thereby filling up all the contents within its
     * boundaries, eleminating the need by the figure to clip the boundaries and
     * do the same.
     * 
     * @see Border#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * @see Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        tempRect.setBounds(getPaintRectangle(figure, insets));
        Rectangle rec = tempRect;
        rec.height = Math.min(rec.height, getTextExtents(figure).height
                + padding.getHeight());
        g.clipRect(rec);
        g.setBackgroundColor(fillColor);
        g.fillRectangle(rec);

        int x = rec.x + padding.left;
        int y = rec.y + padding.top;

        int textWidth = getTextExtents(figure).width;
        int freeSpace = rec.width - padding.getWidth() - textWidth;

        if (getTextAlignment() == PositionConstants.CENTER)
            freeSpace /= 2;
        if (getTextAlignment() != PositionConstants.LEFT)
            x += freeSpace;

        g.setFont(getFont(figure));
        g.setForegroundColor(getTextColor());
        g.drawString(getLabel(), x, y);
    }

    /**
     * Sets the background color of the area within the boundaries of this
     * border. This is required as this border takes responsibility for filling
     * up the region, as TitleBarBorders are always opaque.
     * 
     * @param color
     *            the background color
     * @since 2.0
     */
    public void setBackgroundColor(Color color) {
        fillColor = color;
    }

    /**
     * Sets the padding space to be applied on all sides of the border. The
     * default value is no padding on all sides.
     * 
     * @param all
     *            the value of the padding on all sides
     * @since 2.0
     */
    public void setPadding(int all) {
        padding = new Insets(all);
        invalidate();
    }

    /**
     * Sets the padding space of this TitleBarBorder to the passed value. The
     * default value is no padding on all sides.
     * 
     * @param pad
     *            the padding
     * @since 2.0
     */
    public void setPadding(Insets pad) {
        padding = pad;
        invalidate();
    }

    /**
     * Sets the alignment of the text in the title bar. Possible values are
     * {@link PositionConstants#LEFT}, {@link PositionConstants#CENTER} and
     * {@link PositionConstants#RIGHT}.
     * 
     * @param align
     *            the new text alignment
     * @since 2.0
     */
    public void setTextAlignment(int align) {
        textAlignment = align;
    }

}
