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

/**
 * CompoundBorder allows for the nesting of two borders. The nested borders are
 * referred to as the <i>inner</i> and <i>outer</i> borders.
 */
public class CompoundBorder extends AbstractBorder {

    /** The inner Border. */
    protected Border inner;
    /** The outer Border. */
    protected Border outer;

    /**
     * Constructs a default CompoundBorder with no borders under it.
     * 
     * @since 2.0
     */
    public CompoundBorder() {
    }

    /**
     * Constructs a CompoundBorder with the two borders specified as input.
     * 
     * @param outer
     *            Border which is drawn on the outside
     * @param inner
     *            Border which is drawn inside the outer border
     * 
     * @since 2.0
     */
    public CompoundBorder(Border outer, Border inner) {
        this.outer = outer;
        this.inner = inner;
    }

    /**
     * Returns the inner border of this CompoundBorder.
     * 
     * @return The inner border
     * @since 2.0
     */
    public Border getInnerBorder() {
        return inner;
    }

    /**
     * Returns the total insets required to hold both the inner and outer
     * borders of this CompoundBorder.
     * 
     * @param figure
     *            Figure for which this is the border
     * @return The total insets for this border
     * @since 2.0
     */
    @Override
    public Insets getInsets(IFigure figure) {
        Insets insets = null;
        if (inner != null)
            insets = inner.getInsets(figure);
        else
            insets = new Insets();
        if (outer != null) {
            Insets moreInsets = outer.getInsets(figure);
            insets = insets.getAdded(moreInsets);
        }
        return insets;
    }

    /**
     * @see org.eclipse.draw2d.Border#getPreferredSize(IFigure)
     */
    @Override
    public Dimension getPreferredSize(IFigure fig) {
        Dimension prefSize = new Dimension(inner.getPreferredSize(fig));
        Insets outerInsets = outer.getInsets(fig);
        prefSize.expand(outerInsets.getWidth(), outerInsets.getHeight());
        prefSize.union(outer.getPreferredSize(fig));
        return prefSize;
    }

    /**
     * Returns the outer border of this CompoundBorder.
     * 
     * @return The outer border
     * @since 2.0
     */
    public Border getOuterBorder() {
        return outer;
    }

    /**
     * Returns <code>true</code> if this border is opaque. Return value is
     * dependent on the opaque state of both the borders it contains. Both
     * borders have to be opaque for this border to be opaque. In the absence of
     * any of the borders, this border is not opaque.
     * 
     * @return <code>true</code> if this border is opaque
     */
    @Override
    public boolean isOpaque() {
        return ((inner != null) ? inner.isOpaque() : false)
                && ((outer != null) ? outer.isOpaque() : false);
    }

    /**
     * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        if (outer != null) {
            g.pushState();
            outer.paint(figure, g, insets);
            g.popState();

            insets = insets.getAdded(outer.getInsets(figure));
        }
        if (inner != null)
            inner.paint(figure, g, insets);
    }

}
