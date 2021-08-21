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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ScrollPaneLayout;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public class OverlayScrollPaneLayout extends ScrollPaneLayout {

    /**
     * {@inheritDoc} In OverlayScrollPane, scrollbars are overlayed on top of
     * the Viewport, so the preferred size is just the Viewports preferred size.
     * 
     * @since 2.0
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {
        ScrollPane scrollpane = (ScrollPane) container;
        Insets insets = scrollpane.getInsets();

        int excludedWidth = insets.getWidth();
        int excludedHeight = insets.getHeight();

        return scrollpane
                .getViewport()
                .getPreferredSize(wHint - excludedWidth, hHint - excludedHeight)
                .getExpanded(excludedWidth, excludedHeight);
    }

    /** {@inheritDoc} */
    @Override
    public void layout(IFigure parent) {
        ScrollPane scrollpane = (ScrollPane) parent;
        Rectangle clientArea = parent.getClientArea();

        ScrollBar hBar = scrollpane.getHorizontalScrollBar(), vBar = scrollpane
                .getVerticalScrollBar();
        Viewport viewport = scrollpane.getViewport();

        Insets insets = new Insets();
        insets.bottom = hBar.getPreferredSize(clientArea.width,
                clientArea.height).height;
        insets.right = vBar.getPreferredSize(clientArea.width,
                clientArea.height).width;

        int hVis = scrollpane.getHorizontalScrollBarVisibility(), vVis = scrollpane
                .getVerticalScrollBarVisibility();

        Dimension available = clientArea.getSize(), preferred = viewport
                .getPreferredSize(available.width, available.height).getCopy();

        boolean none = available.contains(preferred), both = !none
                && vVis != NEVER && hVis != NEVER
                && preferred.contains(available), showV = both
                || preferred.height > available.height, showH = both
                || preferred.width > available.width;

        // Adjust for visibility override flags
        showV = !(vVis == NEVER) && (showV || vVis == ALWAYS);
        showH = !(hVis == NEVER) && (showH || hVis == ALWAYS);

        if (!showV)
            insets.right = 0;
        if (!showH)
            insets.bottom = 0;
        Rectangle bounds, viewportArea = clientArea;

        if (showV) {
            bounds = new Rectangle(viewportArea.right() - insets.right,
                    viewportArea.y, insets.right, viewportArea.height);
            vBar.setBounds(bounds);
            // vBar.setMaximum(preferred.height);
        }
        if (showH) {
            bounds = new Rectangle(viewportArea.x, viewportArea.bottom()
                    - insets.bottom, viewportArea.width, insets.bottom);
            hBar.setBounds(bounds);
            // hBar.setMaximum(preferred.width);
        }
        vBar.setVisible(showV);
        hBar.setVisible(showH);
        viewport.setBounds(viewportArea);
    }

}
