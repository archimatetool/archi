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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Special FlowLayout to display the palette in the columns view.
 * 
 * @author Pratik Shah
 */
@SuppressWarnings("rawtypes")
public class ColumnsLayout extends PaletteContainerFlowLayout {

    private Dimension defaultConstraint = null;
    private Dimension cachedConstraint = null;

    /**
     * Constructs a new layout
     */
    public ColumnsLayout() {
        super(true);
        setMinorSpacing(0);
        setMajorSpacing(0);
        setStretchMinorAxis(true);
        setDefaultConstraint(new Dimension(55, 55));
    }

    /**
     * @see org.eclipse.draw2d.FlowLayout#getChildSize(IFigure, int, int)
     */
    @Override
    @SuppressWarnings("deprecation")
    protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
        if (!(child instanceof SeparatorEditPart.SeparatorFigure)) {
            Dimension hints = getMinimumHints(child, wHint, hHint);
            int numOfColumns = (wHint + majorSpacing)
                    / (hints.width + majorSpacing);
            // numOfColumns = Math.min(numOfColumns,
            // maxChildrenInRowWith(child));
            if (numOfColumns == 0) {
                wHint = hints.width;
            } else {
                wHint = (wHint - ((numOfColumns - 1) * majorSpacing))
                        / numOfColumns;
            }
            hHint = hints.height;
        }

        return super.getChildSize(child, wHint, hHint);
    }

    /*
     * Returns a dimension which has a width that is the greater of the
     * following two: the default width (set on defaultConstraint), and the
     * minimum width of the widest child.
     */
    private Dimension getMinimumHints(IFigure figure, int wHint, int hHint) {
        if (cachedConstraint == null) {
            cachedConstraint = defaultConstraint.getCopy();
            List children = figure.getParent().getChildren();
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                IFigure child = (IFigure) iter.next();
                Dimension childSize = (child instanceof PinnablePaletteStackFigure) ? ((PinnablePaletteStackFigure) child)
                        .getHeaderPreferredSize(cachedConstraint.width,
                                cachedConstraint.height) : child
                        .getPreferredSize(cachedConstraint.width,
                                cachedConstraint.height);
                cachedConstraint.width = Math.max(cachedConstraint.width,
                        childSize.width);
            }
            cachedConstraint.height = hHint;
        }
        return cachedConstraint;
    }

    /**
     * @see org.eclipse.draw2d.AbstractHintLayout#invalidate()
     */
    @Override
    public void invalidate() {
        super.invalidate();
        cachedConstraint = null;
    }

    /**
     * For use by the palette
     * 
     * @param d
     *            The constraints to be respected by the children of the figure
     *            that has this layout; Should not be <code>null</code>.
     */
    public void setDefaultConstraint(Dimension d) {
        defaultConstraint = d;
    }

}
