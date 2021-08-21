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
package org.eclipse.gef.ui.palette.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;

/**
 * A ToolbarLayout-like layout for the palette. This layout only works when
 * vertically oriented.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class PaletteToolbarLayout extends ToolbarLayout {

    /**
     * A figure is growing if it's an expanded drawer.
     * 
     * @param child
     *            The figure that is to be marked as growing or non-growing
     * @return <code>true</code> if the given child is considered growing
     */
    protected boolean isChildGrowing(IFigure child) {
        return child instanceof DrawerFigure
                && ((DrawerFigure) child).isExpanded();
    }

    /**
     * @see org.eclipse.draw2d.ToolbarLayout#layout(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        List childrenGrabbingVertical = new ArrayList();
        int numChildren = children.size();
        Rectangle clientArea = parent.getClientArea();
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;
        boolean stretching;
        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];
        int totalHeight = 0, totalMinHeight = 0, heightOfNonGrowingChildren = 0, heightPerChild = 0, excessHeight = 0;

        /*
         * Determine hints.
         */
        int wHint = clientArea.width;
        int hHint = -1;

        /*
         * Store the preferred and minimum sizes of all figures. Determine which
         * figures can be stretched/shrunk.
         */
        for (int i = 0; i < numChildren; i++) {
            IFigure child = (IFigure) children.get(i);

            prefSizes[i] = child.getPreferredSize(wHint, hHint);
            minSizes[i] = child.getMinimumSize(wHint, hHint);

            totalHeight += prefSizes[i].height;
            totalMinHeight += minSizes[i].height;

            if (isChildGrowing(child)) {
                childrenGrabbingVertical.add(child);
            } else {
                heightOfNonGrowingChildren += prefSizes[i].height;
            }
        }
        totalHeight += (numChildren - 1) * getSpacing();
        totalMinHeight += (numChildren - 1) * getSpacing();

        /*
         * This is the algorithm that determines which figures need to be
         * compressed/stretched and by how much.
         */
        stretching = totalHeight - Math.max(availableHeight, totalMinHeight) < 0;
        if (stretching) {
            /*
             * We only want the last child to stretch. So, we mark all but the
             * last growing child as non-growing.
             */
            for (int i = 0; i < childrenGrabbingVertical.size() - 1; i++) {
                int index = children.indexOf(childrenGrabbingVertical.get(i));
                heightOfNonGrowingChildren += prefSizes[index].height;
            }
            if (!childrenGrabbingVertical.isEmpty()) {
                Object last = childrenGrabbingVertical
                        .get(childrenGrabbingVertical.size() - 1);
                childrenGrabbingVertical.clear();
                childrenGrabbingVertical.add(last);
                heightPerChild = availableHeight - heightOfNonGrowingChildren;
            }
        } else if (!childrenGrabbingVertical.isEmpty()) {
            /*
             * So long as there is at least one child that can be grown, figure
             * out how much height should be given to each growing child.
             */
            boolean childrenDiscarded;
            // spaceToConsume is the space height available on the palette that
            // is to be
            // shared by the growing children.
            int spaceToConsume = availableHeight - heightOfNonGrowingChildren;
            // heightPerChild is the height that each growing child is to be
            // grown up to
            heightPerChild = spaceToConsume / childrenGrabbingVertical.size();
            // excessHeight is the space leftover at the bottom of the palette
            // after each
            // growing child has been grown by heightPerChild.
            excessHeight = spaceToConsume
                    - (heightPerChild * childrenGrabbingVertical.size());
            do {
                childrenDiscarded = false;
                for (Iterator iter = childrenGrabbingVertical.iterator(); iter
                        .hasNext();) {
                    IFigure childFig = (IFigure) iter.next();
                    int i = childFig.getParent().getChildren()
                            .indexOf(childFig);
                    // In the case of shrinking, if the child's height is less
                    // than
                    // heightPerChild, mark that child as non-growing
                    if (prefSizes[i].height < heightPerChild) {
                        spaceToConsume -= prefSizes[i].height;
                        heightOfNonGrowingChildren += prefSizes[i].height;
                        iter.remove();
                        if (!childrenGrabbingVertical.isEmpty()) {
                            childrenDiscarded = true;
                            heightPerChild = spaceToConsume
                                    / childrenGrabbingVertical.size();
                            excessHeight = spaceToConsume
                                    - (heightPerChild * childrenGrabbingVertical
                                            .size());
                        }
                        break;
                    }
                }
            } while (childrenDiscarded);
        }

        /*
         * Do the actual layout, i.e. set the bounds of all the figures.
         */
        for (int i = 0; i < numChildren; i++) {
            IFigure child = (IFigure) children.get(i);
            Rectangle newBounds = new Rectangle(x, y, prefSizes[i].width,
                    prefSizes[i].height);

            if (childrenGrabbingVertical.contains(child)) {
                // Set the height of growing children. If this is the last one,
                // give it
                // the excess height.
                childrenGrabbingVertical.remove(child);
                if (childrenGrabbingVertical.isEmpty())
                    newBounds.height = heightPerChild + excessHeight;
                else
                    newBounds.height = heightPerChild;
            }

            if (getStretchMinorAxis())
                newBounds.width = clientArea.width;
            else
                newBounds.width = Math
                        .min(prefSizes[i].width, clientArea.width);

            int adjust = clientArea.width - newBounds.width;
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
            child.setBounds(newBounds);
            y += newBounds.height + getSpacing();
        }
    }

}
