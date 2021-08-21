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

final class SeparatorBorder extends MarginBorder {

    SeparatorBorder(int t, int l, int b, int r) {
        super(t, l, b, r);
    }

    @Override
    public void paint(IFigure f, Graphics g, Insets i) {
        Rectangle r = getPaintRectangle(f, i);
        r.height--;
        g.setForegroundColor(ColorConstants.buttonDarker);
        g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
    }

}
