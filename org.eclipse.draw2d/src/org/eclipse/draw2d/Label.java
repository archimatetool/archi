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

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure that can display text and/or an image.
 */
public class Label extends Figure implements PositionConstants {

    private static String ELLIPSIS = "..."; //$NON-NLS-1$

    private Image icon;
    private String text = "";//$NON-NLS-1$
    private String subStringText;
    private Dimension textSize;
    private Dimension subStringTextSize;
    private Dimension iconSize = new Dimension(0, 0);
    private Point iconLocation;
    private Point textLocation;
    private int textAlignment = CENTER;
    private int iconAlignment = CENTER;
    private int labelAlignment = CENTER;
    private int textPlacement = EAST;
    private int iconTextGap = 3;

    /**
     * Construct an empty Label.
     * 
     * @since 2.0
     */
    public Label() {
    }

    /**
     * Construct a Label with passed String as its text.
     * 
     * @param s
     *            the label text
     * @since 2.0
     */
    public Label(String s) {
        setText(s);
    }

    /**
     * Construct a Label with passed Image as its icon.
     * 
     * @param i
     *            the label image
     * @since 2.0
     */
    public Label(Image i) {
        setIcon(i);
    }

    /**
     * Construct a Label with passed String as text and passed Image as its
     * icon.
     * 
     * @param s
     *            the label text
     * @param i
     *            the label image
     * @since 2.0
     */
    public Label(String s, Image i) {
        setText(s);
        setIcon(i);
    }

    private void alignOnHeight(Point loc, Dimension size, int alignment) {
        Insets insets = getInsets();
        switch (alignment) {
        case TOP:
            loc.y = insets.top;
            break;
        case BOTTOM:
            loc.y = bounds.height - size.height - insets.bottom;
            break;
        default:
            loc.y = (bounds.height - size.height) / 2;
        }
    }

    private void alignOnWidth(Point loc, Dimension size, int alignment) {
        Insets insets = getInsets();
        switch (alignment) {
        case LEFT:
            loc.x = insets.left;
            break;
        case RIGHT:
            loc.x = bounds.width - size.width - insets.right;
            break;
        default:
            loc.x = (bounds.width - size.width) / 2;
        }
    }

    private void calculateAlignment() {
        switch (textPlacement) {
        case EAST:
        case WEST:
            alignOnHeight(textLocation, getTextSize(), textAlignment);
            alignOnHeight(iconLocation, getIconSize(), iconAlignment);
            break;
        case NORTH:
        case SOUTH:
            alignOnWidth(textLocation, getSubStringTextSize(), textAlignment);
            alignOnWidth(iconLocation, getIconSize(), iconAlignment);
            break;
        }
    }

    /**
     * Calculates the size of the Label using the passed Dimension as the size
     * of the Label's text.
     * 
     * @param txtSize
     *            the precalculated size of the label's text
     * @return the label's size
     * @since 2.0
     */
    protected Dimension calculateLabelSize(Dimension txtSize) {
        int gap = getIconTextGap();
        if (getIcon() == null || getText().equals("")) //$NON-NLS-1$
            gap = 0;
        Dimension d = new Dimension(0, 0);
        if (textPlacement == WEST || textPlacement == EAST) {
            d.width = getIconSize().width + gap + txtSize.width;
            d.height = Math.max(getIconSize().height, txtSize.height);
        } else {
            d.width = Math.max(getIconSize().width, txtSize.width);
            d.height = getIconSize().height + gap + txtSize.height;
        }
        return d;
    }

    @SuppressWarnings("deprecation")
    private void calculateLocations() {
        textLocation = new Point();
        iconLocation = new Point();

        calculatePlacement();
        calculateAlignment();
        Dimension offset = getSize().getDifference(getPreferredSize());
        offset.width += getTextSize().width - getSubStringTextSize().width;
        switch (labelAlignment) {
        case CENTER:
            offset.scale(0.5f);
            break;
        case LEFT:
            offset.scale(0.0f);
            break;
        case RIGHT:
            offset.scale(1.0f);
            break;
        case TOP:
            offset.height = 0;
            offset.scale(0.5f);
            break;
        case BOTTOM:
            offset.height = offset.height * 2;
            offset.scale(0.5f);
            break;
        default:
            offset.scale(0.5f);
            break;
        }

        switch (textPlacement) {
        case EAST:
        case WEST:
            offset.height = 0;
            break;
        case NORTH:
        case SOUTH:
            offset.width = 0;
            break;
        }

        textLocation.translate(offset);
        iconLocation.translate(offset);
    }

    private void calculatePlacement() {
        int gap = getIconTextGap();
        if (icon == null || text.equals("")) //$NON-NLS-1$
            gap = 0;
        Insets insets = getInsets();

        switch (textPlacement) {
        case EAST:
            iconLocation.x = insets.left;
            textLocation.x = getIconSize().width + gap + insets.left;
            break;
        case WEST:
            textLocation.x = insets.left;
            iconLocation.x = getSubStringTextSize().width + gap + insets.left;
            break;
        case NORTH:
            textLocation.y = insets.top;
            iconLocation.y = getTextSize().height + gap + insets.top;
            break;
        case SOUTH:
            textLocation.y = getIconSize().height + gap + insets.top;
            iconLocation.y = insets.top;
        }
    }

    /**
     * Calculates the size of the Label's text size. The text size calculated
     * takes into consideration if the Label's text is currently truncated. If
     * text size without considering current truncation is desired, use
     * {@link #calculateTextSize()}.
     * 
     * @return the size of the label's text, taking into account truncation
     * @since 2.0
     */
    protected Dimension calculateSubStringTextSize() {
        return getTextUtilities().getTextExtents(getSubStringText(), getFont());
    }

    /**
     * Calculates and returns the size of the Label's text. Note that this
     * Dimension is calculated using the Label's full text, regardless of
     * whether or not its text is currently truncated. If text size considering
     * current truncation is desired, use {@link #calculateSubStringTextSize()}.
     * 
     * @return the size of the label's text, ignoring truncation
     * @since 2.0
     */
    protected Dimension calculateTextSize() {
        return getTextUtilities().getTextExtents(getText(), getFont());
    }

    private void clearLocations() {
        iconLocation = textLocation = null;
    }

    /**
     * Returns the Label's icon.
     * 
     * @return the label icon
     * @since 2.0
     */
    public Image getIcon() {
        return icon;
    }

    /**
     * Returns the current alignment of the Label's icon. The default is
     * {@link PositionConstants#CENTER}.
     * 
     * @return the icon alignment
     * @since 2.0
     */
    public int getIconAlignment() {
        return iconAlignment;
    }

    /**
     * Returns the bounds of the Label's icon.
     * 
     * @return the icon's bounds
     * @since 2.0
     */
    public Rectangle getIconBounds() {
        Rectangle bounds = getBounds();
        return new Rectangle(bounds.getLocation().translate(getIconLocation()),
                getIconSize());
    }

    /**
     * Returns the location of the Label's icon relative to the Label.
     * 
     * @return the icon's location
     * @since 2.0
     */
    protected Point getIconLocation() {
        if (iconLocation == null)
            calculateLocations();
        return iconLocation;
    }

    /**
     * Returns the gap in pixels between the Label's icon and its text.
     * 
     * @return the gap
     * @since 2.0
     */
    public int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * @see IFigure#getMinimumSize(int, int)
     */
    @Override
    public Dimension getMinimumSize(int w, int h) {
        if (minSize != null)
            return minSize;
        minSize = new Dimension();
        if (getLayoutManager() != null)
            minSize.setSize(getLayoutManager().getMinimumSize(this, w, h));

        Dimension labelSize = calculateLabelSize(getTextUtilities()
                .getTextExtents(getTruncationString(), getFont())
                .intersect(
                        getTextUtilities().getTextExtents(getText(), getFont())));
        Insets insets = getInsets();
        labelSize.expand(insets.getWidth(), insets.getHeight());
        minSize.union(labelSize);
        return minSize;
    }

    /**
     * @see IFigure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (prefSize == null) {
            prefSize = calculateLabelSize(getTextSize());
            Insets insets = getInsets();
            prefSize.expand(insets.getWidth(), insets.getHeight());
            if (getLayoutManager() != null)
                prefSize.union(getLayoutManager().getPreferredSize(this, wHint,
                        hHint));
        }
        if (wHint >= 0 && wHint < prefSize.width) {
            Dimension minSize = getMinimumSize(wHint, hHint);
            Dimension result = prefSize.getCopy();
            result.width = Math.min(result.width, wHint);
            result.width = Math.max(minSize.width, result.width);
            return result;
        }
        return prefSize;
    }

    /**
     * Calculates the amount of the Label's current text will fit in the Label,
     * including an elipsis "..." if truncation is required.
     * 
     * @return the substring
     * @since 2.0
     */
    public String getSubStringText() {
        if (subStringText != null)
            return subStringText;

        subStringText = text;
        int widthShrink = getPreferredSize().width - getSize().width;
        if (widthShrink <= 0)
            return subStringText;

        Dimension effectiveSize = getTextSize().getExpanded(-widthShrink, 0);
        Font currentFont = getFont();
        int dotsWidth = getTextUtilities().getTextExtents(
                getTruncationString(), currentFont).width;

        if (effectiveSize.width < dotsWidth)
            effectiveSize.width = dotsWidth;

        int subStringLength = getTextUtilities().getLargestSubstringConfinedTo(
                text, currentFont, effectiveSize.width - dotsWidth);
        subStringText = new String(text.substring(0, subStringLength)
                + getTruncationString());
        return subStringText;
    }

    /**
     * Returns the size of the Label's current text. If the text is currently
     * truncated, the truncated text with its ellipsis is used to calculate the
     * size.
     * 
     * @return the size of this label's text, taking into account truncation
     * @since 2.0
     */
    protected Dimension getSubStringTextSize() {
        if (subStringTextSize == null)
            subStringTextSize = calculateSubStringTextSize();
        return subStringTextSize;
    }

    /**
     * Returns the text of the label. Note that this is the complete text of the
     * label, regardless of whether it is currently being truncated. Call
     * {@link #getSubStringText()} to return the label's current text contents
     * with truncation considered.
     * 
     * @return the complete text of this label
     * @since 2.0
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the current alignment of the Label's text. The default text
     * alignment is {@link PositionConstants#CENTER}.
     * 
     * @return the text alignment
     */
    public int getTextAlignment() {
        return textAlignment;
    }

    /**
     * Returns the bounds of the label's text. Note that the bounds are
     * calculated using the label's complete text regardless of whether the
     * label's text is currently truncated.
     * 
     * @return the bounds of this label's complete text
     * @since 2.0
     */
    public Rectangle getTextBounds() {
        Rectangle bounds = getBounds();
        return new Rectangle(bounds.getLocation().translate(getTextLocation()),
                textSize);
    }

    /**
     * Returns the location of the label's text relative to the label.
     * 
     * @return the text location
     * @since 2.0
     */
    protected Point getTextLocation() {
        if (textLocation != null)
            return textLocation;
        calculateLocations();
        return textLocation;
    }

    /**
     * Returns the current placement of the label's text relative to its icon.
     * The default text placement is {@link PositionConstants#EAST}.
     * 
     * @return the text placement
     * @since 2.0
     */
    public int getTextPlacement() {
        return textPlacement;
    }

    /**
     * Returns the size of the label's complete text. Note that the text used to
     * make this calculation is the label's full text, regardless of whether the
     * label's text is currently being truncated and is displaying an ellipsis.
     * If the size considering current truncation is desired, call
     * {@link #getSubStringTextSize()}.
     * 
     * @return the size of this label's complete text
     * @since 2.0
     */
    protected Dimension getTextSize() {
        if (textSize == null)
            textSize = calculateTextSize();
        return textSize;
    }

    /**
     * @see IFigure#invalidate()
     */
    @Override
    public void invalidate() {
        prefSize = null;
        minSize = null;
        clearLocations();
        textSize = null;
        subStringTextSize = null;
        subStringText = null;
        super.invalidate();
    }

    /**
     * Returns <code>true</code> if the label's text is currently truncated and
     * is displaying an ellipsis, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the label's text is truncated
     * @since 2.0
     */
    public boolean isTextTruncated() {
        return !getSubStringText().equals(getText());
    }

    /**
     * @see Figure#paintFigure(Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        if (isOpaque())
            super.paintFigure(graphics);
        Rectangle bounds = getBounds();
        graphics.translate(bounds.x, bounds.y);
        if (icon != null)
            graphics.drawImage(icon, getIconLocation());
        if (!isEnabled()) {
            graphics.translate(1, 1);
            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawText(getSubStringText(), getTextLocation());
            graphics.translate(-1, -1);
            graphics.setForegroundColor(ColorConstants.buttonDarker);
        }
        graphics.drawText(getSubStringText(), getTextLocation());
        graphics.translate(-bounds.x, -bounds.y);
    }

    /**
     * Sets the label's icon to the passed image.
     * 
     * @param image
     *            the new label image
     * @since 2.0
     */
    public void setIcon(Image image) {
        if (icon == image)
            return;
        icon = image;
        // Call repaint, in case the image dimensions are the same.
        repaint();
        if (icon == null)
            setIconDimension(new Dimension());
        else
            setIconDimension(new Dimension(image));
    }

    /**
     * This method sets the alignment of the icon within the bounds of the
     * label. If the label is larger than the icon, then the icon will be
     * aligned according to this alignment. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * 
     * @param align
     *            the icon alignment
     * @since 2.0
     */
    public void setIconAlignment(int align) {
        if (iconAlignment == align)
            return;
        iconAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the label's icon size to the passed Dimension.
     * 
     * @param d
     *            the new icon size
     * @deprecated the icon is automatically displayed at 1:1
     * @since 2.0
     */
    public void setIconDimension(Dimension d) {
        if (d.equals(getIconSize()))
            return;
        iconSize = d;
        revalidate();
    }

    /**
     * Sets the gap in pixels between the label's icon and text to the passed
     * value. The default is 4.
     * 
     * @param gap
     *            the gap
     * @since 2.0
     */
    public void setIconTextGap(int gap) {
        if (iconTextGap == gap)
            return;
        iconTextGap = gap;
        repaint();
        revalidate();
    }

    /**
     * Sets the alignment of the label (icon and text) within the figure. If
     * this figure's bounds are larger than the size needed to display the
     * label, the label will be aligned accordingly. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * 
     * @param align
     *            label alignment
     */
    public void setLabelAlignment(int align) {
        if (labelAlignment == align)
            return;
        labelAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the label's text.
     * 
     * @param s
     *            the new label text
     * @since 2.0
     */
    public void setText(String s) {
        // "text" will never be null.
        if (s == null)
            s = "";//$NON-NLS-1$
        if (text.equals(s))
            return;
        text = s;
        revalidate();
        repaint();
    }

    /**
     * Sets the alignment of the text relative to the icon within the label. The
     * text alignment must be orthogonal to the text placement. For example, if
     * the placement is EAST, then the text can be aligned using TOP, CENTER, or
     * BOTTOM. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * 
     * @see #setLabelAlignment(int)
     * @param align
     *            the text alignment
     * @since 2.0
     */
    public void setTextAlignment(int align) {
        if (textAlignment == align)
            return;
        textAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the placement of the text relative to the icon within the label.
     * Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#EAST}</EM>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#WEST}
     * </UL>
     * 
     * @param where
     *            the text placement
     * @since 2.0
     */
    public void setTextPlacement(int where) {
        if (textPlacement == where)
            return;
        textPlacement = where;
        revalidate();
        repaint();
    }

    /**
     * Gets the <code>TextUtilities</code> instance to be used in measurement
     * calculations.
     * 
     * @return a <code>TextUtilities</code> instance
     * @since 3.4
     */
    public TextUtilities getTextUtilities() {
        return TextUtilities.INSTANCE;
    }

    /**
     * Gets the string that will be appended to the text when the label is
     * truncated. By default, this returns an ellipsis.
     * 
     * @return the string to append to the text when truncated
     * @since 3.4
     */
    protected String getTruncationString() {
        return ELLIPSIS;
    }

    /**
     * Gets the icon size
     * 
     * @return the icon size
     * @since 3.4
     */
    protected Dimension getIconSize() {
        return iconSize;
    }

    /**
     * Returns the alignment of the entire label (icon and text). The default
     * label alignment is {@link PositionConstants#CENTER}.
     * 
     * @return the label alignment
     * @since 3.5
     */
    public int getLabelAlignment() {
        return labelAlignment;
    }
}
