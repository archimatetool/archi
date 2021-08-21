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

/**
 * A Border that looks like the system's focus rectangle.
 */
public class FocusBorder extends AbstractBorder {

    /**
     * Constructs a new FocusBorder.
     */
    public FocusBorder() {
    }

    /**
     * @see org.eclipse.draw2d.Border#getInsets(IFigure)
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return new Insets(1);
    }

    /**
     * @see org.eclipse.draw2d.Border#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * Paints a focus rectangle.
     * 
     * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        tempRect.setBounds(getPaintRectangle(figure, insets));
        tempRect.width--;
        tempRect.height--;
        graphics.setForegroundColor(ColorConstants.black);
        graphics.setBackgroundColor(ColorConstants.white);
        graphics.drawFocus(tempRect);
    }

}
