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

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Arranges figures in a single row or column. Orientation can be set to produce
 * either a row or column layout. This layout tries to fit all children within
 * the parent's client area. To do this, it compresses the children by some
 * amount, but will not compress them smaller than their minimum size. If a
 * child's preferred size is smaller than the row's or column's minor dimension,
 * the layout can be configured to stretch the child.
 */
@SuppressWarnings("rawtypes")
public class ToolbarLayout extends OrderedLayout {

    /**
     * Constant for horizontal alignment
     * 
     * @deprecated Unused.
     */
    public static final boolean HORIZONTAL = true;

    /**
     * Constant for vertical alignment
     * 
     * @deprecated Unused.
     * */
    public static final boolean VERTICAL = false;

    /**
     * Sets whether children should "stretch" with their container
     * 
     * @deprecated Use {@link OrderedLayout#setStretchMinorAxis(boolean)} and
     *             {@link OrderedLayout#isStretchMinorAxis()} instead.
     * */
    protected boolean matchWidth;

    /**
     * Space in pixels between Figures
     * 
     * @deprecated Use {@link #getSpacing()} and {@link #setSpacing(int)}
     *             instead.
     */
    protected int spacing;

    /**
     * Constructs a vertically oriented ToolbarLayout with child spacing of 0
     * pixels, {@link #setStretchMinorAxis(boolean)} <code>true</code>, and
     * {@link #ALIGN_TOPLEFT} minor alignment.
     * 
     * @since 2.0
     */
    public ToolbarLayout() {
        setStretchMinorAxis(true);
        setSpacing(0);
    }

    /**
     * Constructs a ToolbarLayout with a specified orientation. Default values
     * are: child spacing 0 pixels, {@link #setStretchMinorAxis(boolean)}
     * <code>false</code>, and {@link #ALIGN_TOPLEFT} alignment.
     * 
     * @param isHorizontal
     *            whether the children are oriented horizontally
     * @since 2.0
     */
    public ToolbarLayout(boolean isHorizontal) {
        setHorizontal(isHorizontal);
        setStretchMinorAxis(false);
        setSpacing(0);
    }

    private Dimension calculateChildrenSize(List children, int wHint,
            int hHint, boolean preferred) {
        Dimension childSize;
        IFigure child;
        int height = 0, width = 0;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            childSize = transposer.t(preferred ? getChildPreferredSize(child,
                    wHint, hHint) : getChildMinimumSize(child, wHint, hHint));
            height += childSize.height;
            width = Math.max(width, childSize.width);
        }
        return new Dimension(width, height);
    }

    /**
     * Calculates the minimum size of the container based on the given hints. If
     * this is a vertically-oriented Toolbar Layout, then only the widthHint is
     * respected (which means that the children can be as tall as they desire).
     * In this case, the minimum width is that of the widest child, and the
     * minimum height is the sum of the minimum heights of all children, plus
     * the spacing between them. The border and insets of the container figure
     * are also accounted for.
     * 
     * @param container
     *            the figure whose minimum size has to be calculated
     * @param wHint
     *            the width hint (the desired width of the container)
     * @param hHint
     *            the height hint (the desired height of the container)
     * @return the minimum size of the container
     * @see #getMinimumSize(IFigure, int, int)
     * @since 2.1
     */
    @Override
    protected Dimension calculateMinimumSize(IFigure container, int wHint,
            int hHint) {
        Insets insets = container.getInsets();
        if (isHorizontal()) {
            wHint = -1;
            if (hHint >= 0)
                hHint = Math.max(0, hHint - insets.getHeight());
        } else {
            hHint = -1;
            if (wHint >= 0)
                wHint = Math.max(0, wHint - insets.getWidth());
        }

        List children = container.getChildren();
        Dimension minSize = calculateChildrenSize(children, wHint, hHint, false);
        // Do a second pass, if necessary
        if (wHint >= 0 && minSize.width > wHint) {
            minSize = calculateChildrenSize(children, minSize.width, hHint,
                    false);
        } else if (hHint >= 0 && minSize.width > hHint) {
            minSize = calculateChildrenSize(children, wHint, minSize.width,
                    false);
        }

        minSize.height += Math.max(0, children.size() - 1) * spacing;
        return transposer.t(minSize)
                .expand(insets.getWidth(), insets.getHeight())
                .union(getBorderPreferredSize(container));
    }

    /**
     * Calculates the preferred size of the container based on the given hints.
     * If this is a vertically-oriented Toolbar Layout, then only the widthHint
     * is respected (which means that the children can be as tall as they
     * desire). In this case, the preferred width is that of the widest child,
     * and the preferred height is the sum of the preferred heights of all
     * children, plus the spacing between them. The border and insets of the
     * container figure are also accounted for.
     * 
     * @param container
     *            the figure whose preferred size has to be calculated
     * @param wHint
     *            the width hint (the desired width of the container)
     * @param hHint
     *            the height hint (the desired height of the container)
     * @return the preferred size of the container
     * @see #getPreferredSize(IFigure, int, int)
     * @since 2.0
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {
        Insets insets = container.getInsets();
        if (isHorizontal()) {
            wHint = -1;
            if (hHint >= 0)
                hHint = Math.max(0, hHint - insets.getHeight());
        } else {
            hHint = -1;
            if (wHint >= 0)
                wHint = Math.max(0, wHint - insets.getWidth());
        }

        List children = container.getChildren();
        Dimension prefSize = calculateChildrenSize(children, wHint, hHint, true);
        // Do a second pass, if necessary
        if (wHint >= 0 && prefSize.width > wHint) {
            prefSize = calculateChildrenSize(children, prefSize.width, hHint,
                    true);
        } else if (hHint >= 0 && prefSize.width > hHint) {
            prefSize = calculateChildrenSize(children, wHint, prefSize.width,
                    true);
        }

        prefSize.height += Math.max(0, children.size() - 1) * spacing;
        return transposer.t(prefSize)
                .expand(insets.getWidth(), insets.getHeight())
                .union(getBorderPreferredSize(container));
    }

    /**
     * @param child
     *            the figure whose minimum size is to be determined
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * @return the given figure's minimum size
     * @since 3.3
     */
    protected Dimension getChildMinimumSize(IFigure child, int wHint, int hHint) {
        return child.getMinimumSize(wHint, hHint);
    }

    /**
     * @param child
     *            the figure whose preferred size is to be determined
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * @return given figure's preferred size
     * @since 3.3
     */
    protected Dimension getChildPreferredSize(IFigure child, int wHint,
            int hHint) {
        return child.getPreferredSize(wHint, hHint);
    }

    /**
     * Returns {@link PositionConstants#VERTICAL} by default.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#getDefaultOrientation()
     */
    @Override
    protected int getDefaultOrientation() {
        return PositionConstants.VERTICAL;
    }

    /**
     * @return the spacing between children
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(IFigure)
     */
    @Override
    protected boolean isSensitiveHorizontally(IFigure parent) {
        return !isHorizontal();
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveVertically(IFigure)
     */
    @Override
    protected boolean isSensitiveVertically(IFigure parent) {
        return isHorizontal();
    }

    /**
     * Returns <code>true</code> if stretch minor axis has been enabled. The
     * default value is false.
     * 
     * @return <code>true</code> if stretch minor axis is enabled
     * @deprecated Use {@link #isStretchMinorAxis()} instead.
     */
    public boolean getStretchMinorAxis() {
        return isStretchMinorAxis();
    }

    /**
     * Overwritten to guarantee backwards compatibility with {@link #matchWidth}
     * field.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#isStretchMinorAxis()
     */
    @Override
    public boolean isStretchMinorAxis() {
        return matchWidth;
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
     */
    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        int numChildren = children.size();
        Rectangle clientArea = transposer.t(parent.getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];

        // Calculate the width and height hints. If it's a vertical
        // ToolBarLayout,
        // then ignore the height hint (set it to -1); otherwise, ignore the
        // width hint. These hints will be passed to the children of the parent
        // figure when getting their preferred size.
        int wHint = -1;
        int hHint = -1;
        if (isHorizontal()) {
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
        } else {
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;
        }

        /*
         * Calculate sum of preferred heights of all children(totalHeight).
         * Calculate sum of minimum heights of all children(minHeight). Cache
         * Preferred Sizes and Minimum Sizes of all children.
         * 
         * totalHeight is the sum of the preferred heights of all children
         * totalMinHeight is the sum of the minimum heights of all children
         * prefMinSumHeight is the sum of the difference between all children's
         * preferred heights and minimum heights. (This is used as a ratio to
         * calculate how much each child will shrink).
         */
        IFigure child;
        int totalHeight = 0;
        int totalMinHeight = 0;
        int prefMinSumHeight = 0;

        for (int i = 0; i < numChildren; i++) {
            child = (IFigure) children.get(i);

            prefSizes[i] = transposer.t(getChildPreferredSize(child, wHint,
                    hHint));
            minSizes[i] = transposer
                    .t(getChildMinimumSize(child, wHint, hHint));

            totalHeight += prefSizes[i].height;
            totalMinHeight += minSizes[i].height;
        }
        totalHeight += (numChildren - 1) * spacing;
        totalMinHeight += (numChildren - 1) * spacing;
        prefMinSumHeight = totalHeight - totalMinHeight;
        /*
         * The total amount that the children must be shrunk is the sum of the
         * preferred Heights of the children minus Max(the available area and
         * the sum of the minimum heights of the children).
         * 
         * amntShrinkHeight is the combined amount that the children must shrink
         * amntShrinkCurrentHeight is the amount each child will shrink
         * respectively
         */
        int amntShrinkHeight = totalHeight
                - Math.max(availableHeight, totalMinHeight);

        if (amntShrinkHeight < 0) {
            amntShrinkHeight = 0;
        }

        for (int i = 0; i < numChildren; i++) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int prefWidth = prefSizes[i].width;
            int minWidth = minSizes[i].width;
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

            child = (IFigure) children.get(i);
            if (prefMinSumHeight != 0)
                amntShrinkCurrentHeight = (prefHeight - minHeight)
                        * amntShrinkHeight / (prefMinSumHeight);

            int width = Math.min(prefWidth,
                    transposer.t(child.getMaximumSize()).width);
            if (isStretchMinorAxis())
                width = transposer.t(child.getMaximumSize()).width;
            width = Math.max(minWidth, Math.min(clientArea.width, width));
            newBounds.width = width;

            int adjust = clientArea.width - width;
            switch (getMinorAlignment()) {
            case ALIGN_TOPLEFT:
                adjust = 0;
                break;
            case ALIGN_CENTER:
                adjust /= 2;
                break;
            case ALIGN_BOTTOMRIGHT:
                break;
            }
            newBounds.x += adjust;
            newBounds.height -= amntShrinkCurrentHeight;
            child.setBounds(transposer.t(newBounds));

            amntShrinkHeight -= amntShrinkCurrentHeight;
            prefMinSumHeight -= (prefHeight - minHeight);
            y += newBounds.height + spacing;
        }
    }

    /**
     * Sets children's width (if vertically oriented) or height (if horizontally
     * oriented) to stretch with their container.
     * 
     * @deprecated use {@link #setStretchMinorAxis(boolean)}
     * @param match
     *            whether to stretch children
     * @since 2.0
     */
    public void setMatchWidth(boolean match) {
        matchWidth = match;
    }

    /**
     * Sets the amount of space between children.
     * 
     * @param space
     *            the amount of space between children
     * @since 2.0
     */
    public void setSpacing(int space) {
        spacing = space;
    }

    /**
     * Overwritten to guarantee backwards compatibility with {@link #matchWidth}
     * field.
     * 
     * @see org.eclipse.draw2d.OrderedLayout#setStretchMinorAxis(boolean)
     */
    @Override
    public void setStretchMinorAxis(boolean value) {
        matchWidth = value;
    }

    /**
     * Sets the orientation of the layout
     * 
     * @param flag
     *            whether the orientation should be vertical
     * @since 2.0
     * @deprecated Use {@link #setHorizontal(boolean)} with argument
     *             <code>false</code> instead.
     */
    public void setVertical(boolean flag) {
        setHorizontal(!flag);
    }

}
