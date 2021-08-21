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

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides a two pixel wide constant sized border, having an etched look.
 */
public final class SimpleEtchedBorder extends SchemeBorder {

    /** The singleton instance of this class */
    public static final Border singleton = new SimpleEtchedBorder();

    /** The insets */
    protected static final Insets INSETS = new Insets(2);

    /**
     * Constructs a default border having a two pixel wide border.
     * 
     * @since 2.0
     */
    protected SimpleEtchedBorder() {
    }

    /**
     * Returns the Insets used by this border. This is a constant value of two
     * pixels in each direction.
     * 
     * @see Border#getInsets(IFigure)
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return new Insets(INSETS);
    }

    /**
     * Returns the opaque state of this border. This border is opaque and takes
     * responsibility to fill the region it encloses.
     * 
     * @see Border#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * @see Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        Rectangle rect = getPaintRectangle(figure, insets);
        FigureUtilities.paintEtchedBorder(g, rect);
    }

}
