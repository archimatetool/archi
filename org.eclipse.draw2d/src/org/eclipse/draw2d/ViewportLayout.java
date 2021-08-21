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
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Layout for a viewport. A viewport is a flexible window onto a figure.
 */
public class ViewportLayout extends AbstractHintLayout {

    /**
     * Returns the minimum size required by the input viewport figure. Since
     * viewport is flexible, the minimum size required would be the just the
     * size of the borders.
     * 
     * @see AbstractHintLayout#calculateMinimumSize(IFigure, int, int)
     */
    @Override
    protected Dimension calculateMinimumSize(IFigure figure, int wHint,
            int hHint) {
        Viewport viewport = (Viewport) figure;
        Dimension min = new Dimension();
        Insets insets = viewport.getInsets();
        return min.getExpanded(insets.getWidth(), insets.getHeight());
    }

    /**
     * Calculates and returns the preferred size of the figure based on the
     * given hints. The given wHint is ignored unless the viewport (parent) is
     * tracking width. The same is true for the height hint.
     * 
     * @param parent
     *            the Viewport whose preferred size is to be calculated
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * @return the Preferred size of the given viewport
     * @since 2.0
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure parent, int wHint,
            int hHint) {
        Viewport viewport = (Viewport) parent;
        Insets insets = viewport.getInsets();
        IFigure contents = viewport.getContents();

        if (viewport.getContentsTracksWidth() && wHint > -1)
            wHint = Math.max(0, wHint - insets.getWidth());
        else
            wHint = -1;
        if (viewport.getContentsTracksHeight() && hHint > -1)
            hHint = Math.max(0, hHint - insets.getHeight());
        else
            hHint = -1;

        if (contents == null) {
            return new Dimension(insets.getWidth(), insets.getHeight());
        } else {
            Dimension minSize = contents.getMinimumSize(wHint, hHint);
            if (wHint > -1)
                wHint = Math.max(wHint, minSize.width);
            if (hHint > -1)
                hHint = Math.max(hHint, minSize.height);
            return contents.getPreferredSize(wHint, hHint).getExpanded(
                    insets.getWidth(), insets.getHeight());
        }

        // Layout currently does not union border's preferred size.
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(IFigure)
     */
    @Override
    protected boolean isSensitiveHorizontally(IFigure parent) {
        return ((Viewport) parent).getContentsTracksWidth();
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#isSensitiveHorizontally(IFigure)
     */
    @Override
    protected boolean isSensitiveVertically(IFigure parent) {
        return ((Viewport) parent).getContentsTracksHeight();
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
     */
    @Override
    public void layout(IFigure figure) {
        Viewport viewport = (Viewport) figure;
        IFigure contents = viewport.getContents();

        if (contents == null)
            return;
        Point p = viewport.getClientArea().getLocation();

        p.translate(viewport.getViewLocation().getNegated());

        // Calculate the hints
        Rectangle hints = viewport.getClientArea();
        int wHint = viewport.getContentsTracksWidth() ? hints.width : -1;
        int hHint = viewport.getContentsTracksHeight() ? hints.height : -1;

        Dimension newSize = viewport.getClientArea().getSize();
        Dimension min = contents.getMinimumSize(wHint, hHint);
        Dimension pref = contents.getPreferredSize(wHint, hHint);

        if (viewport.getContentsTracksHeight())
            newSize.height = Math.max(newSize.height, min.height);
        else
            newSize.height = Math.max(newSize.height, pref.height);

        if (viewport.getContentsTracksWidth())
            newSize.width = Math.max(newSize.width, min.width);
        else
            newSize.width = Math.max(newSize.width, pref.width);

        contents.setBounds(new Rectangle(p, newSize));
    }

}
