/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Extends <code>FlowLayout</code> to allow the pane of a pinnable stack in icon
 * mode to occupy the row following the row in which the icon of the palette
 * stack header appears.
 * 
 * @author crevells
 * @since 3.4
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class PaletteContainerFlowLayout extends FlowLayout {

    /**
     * Constructs a PaletteContainerFlowLayout with horizontal orientation.
     */
    public PaletteContainerFlowLayout() {
    }

    /**
     * Constructs a PaletteContainerFlowLayout whose orientation is given in the
     * input.
     * 
     * @param isHorizontal
     *            <code>true</code> if the layout should be horizontal
     */
    public PaletteContainerFlowLayout(boolean isHorizontal) {
        setHorizontal(isHorizontal);
    }

    /**
     * Overridden to include the size of the expanded pane of an expanded
     * pinnable palette stack.
     * 
     * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(IFigure,
     *      int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {

        Dimension prefSize = super.calculatePreferredSize(container, wHint,
                hHint);

        List children = container.getChildren();
        IFigure child;

        // Build the sizes for each row, and update prefSize accordingly
        Dimension expandedPaneSize = null;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);

            if (child instanceof PinnablePaletteStackFigure
                    && ((PinnablePaletteStackFigure) child).isExpanded()) {

                // Subtract out the insets from the hints
                if (wHint > -1)
                    wHint = Math.max(0, wHint
                            - container.getInsets().getWidth());
                if (hHint > -1)
                    hHint = Math.max(0, hHint
                            - container.getInsets().getHeight());

                // Figure out the new hint that we are interested in based on
                // the
                // orientation. Ignore the other hint (by setting it to -1).
                // NOTE:
                // The children of the parent figure will then be asked to
                // ignore
                // that hint as well.
                if (isHorizontal()) {
                    hHint = -1;
                } else {
                    wHint = -1;
                }

                expandedPaneSize = ((PinnablePaletteStackFigure) child)
                        .getExpandedContainerPreferredSize(wHint, hHint);

                break; // there can only be one expanded stack
            }
        }

        if (expandedPaneSize != null) {
            // increment height to account for expanded stack
            prefSize.height += transposer.t(expandedPaneSize).height;
            prefSize.union(getBorderPreferredSize(container));
        }

        return prefSize;
    }

    /**
     * Overridden to handle <code>PinnablePaletteStackFigure</code>.
     * 
     * @see FlowLayout#getChildSize(IFigure, int, int)
     */
    @Override
    protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
        if (child instanceof PinnablePaletteStackFigure) {
            return ((PinnablePaletteStackFigure) child).getHeaderPreferredSize(
                    wHint, hHint);
        } else {
            return child.getPreferredSize(wHint, hHint);
        }
    }

    /**
     * Overridden to include the size of the expanded pane of an expanded
     * pinnable palette stack during the layout.
     * 
     * @see FlowLayout#layoutRow(IFigure)
     */
    @Override
    protected void layoutRow(IFigure parent) {
        int majorAdjustment = 0;
        int minorAdjustment = 0;
        int correctMajorAlignment = majorAlignment;
        int correctMinorAlignment = minorAlignment;

        majorAdjustment = data.area.width - data.rowWidth + getMinorSpacing();

        switch (correctMajorAlignment) {
        case ALIGN_LEFTTOP:
            majorAdjustment = 0;
            break;
        case ALIGN_CENTER:
            majorAdjustment /= 2;
            break;
        case ALIGN_RIGHTBOTTOM:
            break;
        }

        int expandedPaneHeight = 0;
        for (int j = 0; j < data.rowCount; j++) {
            if (fill) {
                data.bounds[j].height = data.rowHeight;
            } else {
                minorAdjustment = data.rowHeight - data.bounds[j].height;
                switch (correctMinorAlignment) {
                case ALIGN_LEFTTOP:
                    minorAdjustment = 0;
                    break;
                case ALIGN_CENTER:
                    minorAdjustment /= 2;
                    break;
                case ALIGN_RIGHTBOTTOM:
                    break;
                }
                data.bounds[j].y += minorAdjustment;
            }
            data.bounds[j].x += majorAdjustment;

            IFigure child = data.row[j];
            setBoundsOfChild(parent, data.row[j], transposer.t(data.bounds[j]));

            if (child instanceof PinnablePaletteStackFigure
                    && ((PinnablePaletteStackFigure) child).isExpanded()) {

                int wHint = -1;
                int hHint = -1;
                if (isHorizontal())
                    wHint = parent.getClientArea().width;
                else
                    hHint = parent.getClientArea().height;

                expandedPaneHeight = ((PinnablePaletteStackFigure) child)
                        .getExpandedContainerPreferredSize(wHint, hHint).height;
                child.setBounds(new Rectangle(data.area.x, data.area.y
                        + data.rowY, data.area.width, data.rowHeight
                        + expandedPaneHeight));
            }
        }
        data.rowY += getMajorSpacing() + data.rowHeight + expandedPaneHeight;
        initRow();
    }

    /**
     * Overridden to set the bounds for <code>PinnablePaletteStackFigures</code>
     * .
     * 
     * @see FlowLayout#setBoundsOfChild(IFigure, IFigure, Rectangle)
     */
    @Override
    protected void setBoundsOfChild(IFigure parent, IFigure child,
            Rectangle bounds) {

        if (child instanceof PinnablePaletteStackFigure
                && ((PinnablePaletteStackFigure) child).isExpanded()) {
            parent.getClientArea(Rectangle.SINGLETON);
            bounds.translate(Rectangle.SINGLETON.x, Rectangle.SINGLETON.y);
            ((PinnablePaletteStackFigure) child)
                    .setHeaderBoundsLayoutHint(bounds);
        } else {
            super.setBoundsOfChild(parent, child, bounds);
        }
    }

}
