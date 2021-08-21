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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Pratik Shah
 */
public class RaisedBorder extends MarginBorder {

    private static final Insets DEFAULT_INSETS = new Insets(1, 1, 1, 1);

    /**
     * @see org.eclipse.draw2d.Border#getInsets(IFigure)
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return insets;
    }

    public RaisedBorder() {
        this(DEFAULT_INSETS);
    }

    public RaisedBorder(Insets insets) {
        super(insets);
    }

    public RaisedBorder(int t, int l, int b, int r) {
        super(t, l, b, r);
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
     */
    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        g.setLineStyle(Graphics.LINE_SOLID);
        g.setLineWidth(1);
        g.setForegroundColor(ColorConstants.buttonLightest);
        Rectangle r = getPaintRectangle(figure, insets);
        r.resize(-1, -1);
        g.drawLine(r.x, r.y, r.right(), r.y);
        g.drawLine(r.x, r.y, r.x, r.bottom());
        g.setForegroundColor(ColorConstants.buttonDarker);
        g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
        g.drawLine(r.right(), r.y, r.right(), r.bottom());
    }

}
