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
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Figures using the StackLayout as their layout manager have their children
 * placed on top of one another. Order of placement is determined by the order
 * in which the children were added, first child added placed on the bottom.
 */
@SuppressWarnings("rawtypes")
public class StackLayout extends AbstractHintLayout {

    /**
     * Returns the minimum size required by the input container. This is the
     * size of the largest child of the container, as all other children fit
     * into this size.
     * 
     * @see AbstractHintLayout#calculateMinimumSize(IFigure, int, int)
     */
    @Override
    protected Dimension calculateMinimumSize(IFigure figure, int wHint,
            int hHint) {
        if (wHint > -1)
            wHint = Math.max(0, wHint - figure.getInsets().getWidth());
        if (hHint > -1)
            hHint = Math.max(0, hHint - figure.getInsets().getHeight());
        Dimension d = new Dimension();
        List children = figure.getChildren();
        IFigure child;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            if (!isObservingVisibility() || child.isVisible())
                d.union(child.getMinimumSize(wHint, hHint));
        }

        d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
        d.union(getBorderPreferredSize(figure));
        return d;

    }

    /**
     * Calculates and returns the preferred size of the given figure. This is
     * the union of the preferred sizes of the widest and the tallest of all its
     * children.
     * 
     * @see AbstractLayout#calculatePreferredSize(IFigure, int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure figure, int wHint,
            int hHint) {
        if (wHint > -1)
            wHint = Math.max(0, wHint - figure.getInsets().getWidth());
        if (hHint > -1)
            hHint = Math.max(0, hHint - figure.getInsets().getHeight());
        Dimension d = new Dimension();
        List children = figure.getChildren();
        IFigure child;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            if (!isObservingVisibility() || child.isVisible())
                d.union(child.getPreferredSize(wHint, hHint));
        }

        d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
        d.union(getBorderPreferredSize(figure));
        return d;
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
     */
    @Override
    public void layout(IFigure figure) {
        Rectangle r = figure.getClientArea();
        List children = figure.getChildren();
        IFigure child;
        for (int i = 0; i < children.size(); i++) {
            child = (IFigure) children.get(i);
            child.setBounds(r);
        }
    }

}
