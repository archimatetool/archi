/*******************************************************************************
 * Copyright (c) 2007, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Insets;

/**
 * A special border which can paint both underneath and on top of a Figure.
 * Normal borders only paint on top of a figure and its children. A background
 * has the opportunity to paint both first, and optionally last.
 * <P>
 * WARNING: Experimental for 3.3. Clients should help validate the use cases of
 * this new function.
 * 
 * @since 3.3
 */
public class AbstractBackground extends AbstractBorder {

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return IFigure.NO_INSETS;
    }

    /**
     * {@inheritDoc} By default, this method is stubbed out for backgrounds
     * which only paint underneath a figure.
     */
    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
    }

    /**
     * Called when this Background should paint. If the background is being
     * painted inside another border or background, the insets indicate how far
     * inside the target figure the background should be painted. In most cases,
     * the insets will be all zero.
     * 
     * @param figure
     *            The figure on which the background is being painted
     * @param graphics
     *            The graphics
     * @param insets
     *            Amount to inset from the figure's bounds
     * @since 3.2
     */
    public void paintBackground(IFigure figure, Graphics graphics, Insets insets) {
    }

}
