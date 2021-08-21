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
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides generic support for borders.
 * 
 * @author hudsonr
 */
public abstract class AbstractBorder implements Border {

    private static final Dimension EMPTY = new Dimension();

    /** A temporary Rectangle */
    protected static Rectangle tempRect = new Rectangle();

    /**
     * Returns a temporary rectangle representing the figure's bounds cropped by
     * the specified insets. This method exists for convenience and performance;
     * the method does not new any Objects and returns a rectangle which the
     * caller can manipulate.
     * 
     * @since 2.0
     * @param figure
     *            Figure for which the paintable rectangle is needed
     * @param insets
     *            The insets
     * @return The paintable region on the Figure f
     */
    @SuppressWarnings("deprecation")
    protected static final Rectangle getPaintRectangle(IFigure figure,
            Insets insets) {
        tempRect.setBounds(figure.getBounds());
        return tempRect.crop(insets);
    }

    /**
     * @see org.eclipse.draw2d.Border#getPreferredSize(IFigure)
     */
    @Override
    public Dimension getPreferredSize(IFigure f) {
        return EMPTY;
    }

    /**
     * @see org.eclipse.draw2d.Border#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return false;
    }

}
