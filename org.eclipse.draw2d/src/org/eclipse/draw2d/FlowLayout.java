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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Lays out children in rows or columns, wrapping when the current row/column is
 * filled. The aligment and spacing of rows in the parent can be configured. The
 * aligment and spacing of children within a row can be configured.
 */
@SuppressWarnings("rawtypes")
public class FlowLayout extends OrderedLayout {

    /**
     * Holds the necessary information for layout calculations.
     */
    protected class WorkingData {
        public Rectangle bounds[], area;
        public IFigure row[];
        public int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth;
    }

    /**
     * Constant to specify components to be aligned on the left/top
     * 
     * @deprecated Use {@link OrderedLayout#ALIGN_TOPLEFT} instead.
     */
    public static final int ALIGN_LEFTTOP = ALIGN_TOPLEFT;

    /**
     * Constant to specify components to be aligned on the right/bottom
     * 
     * @deprecated Use {@link OrderedLayout#ALIGN_BOTTOMRIGHT} instead.
     */
    public static final int ALIGN_RIGHTBOTTOM = ALIGN_BOTTOMRIGHT;

    protected WorkingData data = null;

    /**
     * The property that determines whether leftover space at the end of a
     * row/column should be filled by the last item in that row/column.
     * 
     * @deprecated Use {@link OrderedLayout#setStretchMinorAxis(boolean)} and
     *             {@link OrderedLayout#isStretchMinorAxis()} instead.
     */
    protected boolean fill;

    /**
     * The alignment along the major axis.
     * 
     * @deprecated Use {@link #getMajorAlignment()} and
     *             {@link #setMajorAlignment(int)} instead.
     */
    protected int majorAlignment = ALIGN_TOPLEFT;

    /**
     * The spacing along the major axis.
     * 
     * @deprecated Use {@link #getMajorSpacing()} and
     *             {@link #setMajorSpacing(int)} instead.
     */
    protected int majorSpacing = 5;

    /**
     * The spacing along the minor axis.
     * 
     * @deprecated Use {@link #getMinorSpacing()} and
     *             {@link #setMinorSpacing(int)} instead.
     */
    protected int minorSpacing = 5;

    /**
     * Constructs a FlowLayout with horizontal orientation.
     * 
     * @since 2.0
     */
    public FlowLayout() {
        setStretchMinorAxis(false);
    }

    /**
     * Constructs a FlowLayout whose orientation is given in the input.
     * 
     * @param isHorizontal
     *            <code>true</code> if the layout should be horizontal
     * @since 2.0
     */
    public FlowLayout(boolean isHorizontal) {
        setHorizontal(isHorizontal);
        setStretchMinorAxis(false);
    }

    /**
     * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(IFigure,
     *      int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {
        // Subtract out the insets from the hints
        if (wHint > -1)
            wHint = Math.max(0, wHint - container.getInsets().getWidth());
        if (hHint > -1)
            hHint = Math.max(0, hHint - container.getInsets().getHeight());

        // Figure out the new hint that we are interested in based on the
        // orientation
        // Ignore the other hint (by setting it to -1). NOTE: The children of
        // the
        // parent figure will then be asked to ignore that hint as well.
        int maxWidth;
        if (isHorizontal()) {
            maxWidth = wHint;
            hHint = -1;
        } else {
            maxWidth = hHint;
            wHint = -1;
        }
        if (maxWidth < 0) {
            maxWidth = Integer.MAX_VALUE;
        }

        // The preferred dimension that is to be calculated and returned
        Dimension prefSize = new Dimension();

        List children = container.getChildren();
        int width = 0;
        int height = 0;
        IFigure child;
        Dimension childSize;

        // Build the sizes for each row, and update prefSize accordingly
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            childSize = transposer.t(getChildSize(child, wHint, hHint));
            if (i == 0) {
                width = childSize.width;
                height = childSize.height;
            } else if (width + childSize.width + getMinorSpacing() > maxWidth) {
                // The current row is full, start a new row.
                prefSize.height += height + getMajorSpacing();
                prefSize.width = Math.max(prefSize.width, width);
                width = childSize.width;
                height = childSize.height;
            } else {
                // The current row can fit another child.
                width += childSize.width + getMinorSpacing();
                height = Math.max(height, childSize.height);
            }
        }

        // Flush out the last row's data
        prefSize.height += height;
        prefSize.width = Math.max(prefSize.width, width);

        // Transpose the dimension back, and compensate for the border.
        prefSize = transposer.t(prefSize);
        prefSize.width += container.getInsets().getWidth();
        prefSize.height += container.getInsets().getHeight();
        prefSize.union(getBorderPreferredSize(container));

        return prefSize;
    }

    /**
     * Provides the given child's preferred size.
     * 
     * @param child
     *            the Figure whose preferred size needs to be calculated
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * @return the child's preferred size
     */
    protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
        return child.getPreferredSize(wHint, hHint);
    }

    /**
     * Returns {@link PositionConstants#HORIZONTAL} by default.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#getDefaultOrientation()
     */
    @Override
    protected int getDefaultOrientation() {
        return PositionConstants.HORIZONTAL;
    }

    /**
     * Returns the alignment used for an entire row/column.
     * <P>
     * Possible values are :
     * <ul>
     * <li>{@link #ALIGN_CENTER}
     * <li>{@link #ALIGN_LEFTTOP}
     * <li>{@link #ALIGN_RIGHTBOTTOM}
     * </ul>
     * 
     * @return the major alignment
     * @since 2.0
     */
    public int getMajorAlignment() {
        return majorAlignment;
    }

    /**
     * Returns the spacing in pixels to be used between children in the
     * direction parallel to the layout's orientation.
     * 
     * @return the major spacing
     */
    public int getMajorSpacing() {
        return majorSpacing;
    }

    /**
     * Returns the spacing to be used between children within a row/column.
     * 
     * @return the minor spacing
     */
    public int getMinorSpacing() {
        return minorSpacing;
    }

    /**
     * Initializes the state of row data, which is internal to the layout
     * process.
     */
    protected void initRow() {
        data.rowX = 0;
        data.rowHeight = 0;
        data.rowWidth = 0;
        data.rowCount = 0;
    }

    /**
     * Initializes state data for laying out children, based on the Figure given
     * as input.
     * 
     * @param parent
     *            the parent figure
     * @since 2.0
     */
    protected void initVariables(IFigure parent) {
        data.row = new IFigure[parent.getChildren().size()];
        data.bounds = new Rectangle[data.row.length];
        data.maxWidth = data.area.width;
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(IFigure)
     */
    @Override
    protected boolean isSensitiveHorizontally(IFigure parent) {
        return isHorizontal();
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveVertically(IFigure)
     */
    @Override
    protected boolean isSensitiveVertically(IFigure parent) {
        return !isHorizontal();
    }

    /**
     * Overwritten to guarantee backwards compatibility with {@link #fill}
     * field.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#isStretchMinorAxis()
     */
    @Override
    public boolean isStretchMinorAxis() {
        return fill;
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
     */
    @Override
    public void layout(IFigure parent) {
        data = new WorkingData();
        Rectangle relativeArea = parent.getClientArea();
        data.area = transposer.t(relativeArea);

        Iterator iterator = parent.getChildren().iterator();
        int dx;

        // Calculate the hints to be passed to children
        int wHint = -1;
        int hHint = -1;
        if (isHorizontal())
            wHint = parent.getClientArea().width;
        else
            hHint = parent.getClientArea().height;

        initVariables(parent);
        initRow();
        while (iterator.hasNext()) {
            IFigure f = (IFigure) iterator.next();
            Dimension pref = transposer.t(getChildSize(f, wHint, hHint));
            Rectangle r = new Rectangle(0, 0, pref.width, pref.height);

            if (data.rowCount > 0) {
                if (data.rowWidth + pref.width > data.maxWidth)
                    layoutRow(parent);
            }
            r.x = data.rowX;
            r.y = data.rowY;
            dx = r.width + getMinorSpacing();
            data.rowX += dx;
            data.rowWidth += dx;
            data.rowHeight = Math.max(data.rowHeight, r.height);
            data.row[data.rowCount] = f;
            data.bounds[data.rowCount] = r;
            data.rowCount++;
        }
        if (data.rowCount != 0)
            layoutRow(parent);
        data = null;
    }

    /**
     * Layouts one row of components. This is done based on the layout's
     * orientation, minor alignment and major alignment.
     * 
     * @param parent
     *            the parent figure
     * @since 2.0
     */
    protected void layoutRow(IFigure parent) {
        int majorAdjustment = 0;
        int minorAdjustment = 0;
        int correctMajorAlignment = getMajorAlignment();
        int correctMinorAlignment = getMinorAlignment();

        majorAdjustment = data.area.width - data.rowWidth + getMinorSpacing();

        switch (correctMajorAlignment) {
        case ALIGN_TOPLEFT:
            majorAdjustment = 0;
            break;
        case ALIGN_CENTER:
            majorAdjustment /= 2;
            break;
        case ALIGN_BOTTOMRIGHT:
            break;
        }

        for (int j = 0; j < data.rowCount; j++) {
            if (isStretchMinorAxis()) {
                data.bounds[j].height = data.rowHeight;
            } else {
                minorAdjustment = data.rowHeight - data.bounds[j].height;
                switch (correctMinorAlignment) {
                case ALIGN_TOPLEFT:
                    minorAdjustment = 0;
                    break;
                case ALIGN_CENTER:
                    minorAdjustment /= 2;
                    break;
                case ALIGN_BOTTOMRIGHT:
                    break;
                }
                data.bounds[j].y += minorAdjustment;
            }
            data.bounds[j].x += majorAdjustment;

            setBoundsOfChild(parent, data.row[j], transposer.t(data.bounds[j]));
        }
        data.rowY += getMajorSpacing() + data.rowHeight;
        initRow();
    }

    /**
     * Sets the given bounds for the child figure input.
     * 
     * @param parent
     *            the parent figure
     * @param child
     *            the child figure
     * @param bounds
     *            the size of the child to be set
     * @since 2.0
     */
    protected void setBoundsOfChild(IFigure parent, IFigure child,
            Rectangle bounds) {
        parent.getClientArea(Rectangle.SINGLETON);
        bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);
        child.setBounds(bounds);
    }

    /**
     * Sets the alignment for an entire row/column within the parent figure.
     * <P>
     * Possible values are :
     * <ul>
     * <li>{@link #ALIGN_CENTER}
     * <li>{@link #ALIGN_LEFTTOP}
     * <li>{@link #ALIGN_RIGHTBOTTOM}
     * </ul>
     * 
     * @param align
     *            the major alignment
     * @since 2.0
     */
    public void setMajorAlignment(int align) {
        majorAlignment = align;
    }

    /**
     * Sets the spacing in pixels to be used between children in the direction
     * parallel to the layout's orientation.
     * 
     * @param n
     *            the major spacing
     * @since 2.0
     */
    public void setMajorSpacing(int n) {
        majorSpacing = n;
    }

    /**
     * Sets the spacing to be used between children within a row/column.
     * 
     * @param n
     *            the minor spacing
     * @since 2.0
     */
    public void setMinorSpacing(int n) {
        minorSpacing = n;
    }

    /**
     * Overwritten to guarantee backwards compatibility with {@link #fill}
     * field.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#setStretchMinorAxis(boolean)
     */
    @Override
    public void setStretchMinorAxis(boolean value) {
        fill = value;
    }

}
