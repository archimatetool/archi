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
 * A viewport for {@link org.eclipse.draw2d.FreeformFigure FreeformFigures}.
 * FreeformFigures can only reside in this type of viewport.
 */
public class FreeformViewport extends Viewport {

    class FreeformViewportLayout extends ViewportLayout {
        @Override
        protected Dimension calculatePreferredSize(IFigure parent, int wHint,
                int hHint) {
            getContents().validate();
            wHint = Math.max(0, wHint);
            hHint = Math.max(0, hHint);
            return ((FreeformFigure) getContents()).getFreeformExtent()
                    .getExpanded(getInsets()).union(0, 0)
                    .union(wHint - 1, hHint - 1).getSize();
        }

        @Override
        protected boolean isSensitiveHorizontally(IFigure parent) {
            return true;
        }

        @Override
        protected boolean isSensitiveVertically(IFigure parent) {
            return true;
        }

        @Override
        public void layout(IFigure figure) {
            // Do nothing, contents updates itself.
        }
    }

    /**
     * Constructs a new FreeformViewport. This viewport must use graphics
     * translation to scroll the FreeformFigures inside of it.
     */
    public FreeformViewport() {
        super(true); // Must use graphics translate to scroll freeforms.
        setLayoutManager(new FreeformViewportLayout());
    }

    /**
     * Readjusts the scrollbars. In doing so, it gets the freeform extent of the
     * contents and unions this rectangle with this viewport's client area, then
     * sets the contents freeform bounds to be this unioned rectangle. Then
     * proceeds to set the scrollbar values based on this new information.
     * 
     * @see Viewport#readjustScrollBars()
     */
    @Override
    protected void readjustScrollBars() {
        if (getContents() == null)
            return;
        if (!(getContents() instanceof FreeformFigure))
            return;
        FreeformFigure ff = (FreeformFigure) getContents();
        Rectangle clientArea = getClientArea();
        Rectangle bounds = ff.getFreeformExtent().getCopy();
        bounds.union(0, 0, clientArea.width, clientArea.height);
        ff.setFreeformBounds(bounds);

        getVerticalRangeModel().setAll(bounds.y, clientArea.height,
                bounds.bottom());
        getHorizontalRangeModel().setAll(bounds.x, clientArea.width,
                bounds.right());
    }

    /**
     * Returns <code>true</code>.
     * 
     * @see Figure#useLocalCoordinates()
     */
    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }

}
