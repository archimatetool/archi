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

import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

class DropShadowButtonBorder extends AbstractBorder {

    protected Insets insets = new Insets(1, 1, 3, 3);

    @SuppressWarnings("deprecation")
    private static final Color highlight = ColorConstants.menuBackgroundSelected,
            dropshadow2 = new Color(null, ViewForm.borderMiddleRGB),
            dropshadow3 = new Color(null, ViewForm.borderOutsideRGB);

    /**
     * Returns the space used by the border for the figure provided as input. In
     * this border all sides always have equal width.
     * 
     * @param figure
     *            Figure for which this is the border.
     * @return Insets for this border.
     */
    @Override
    public Insets getInsets(IFigure figure) {
        return insets;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        ButtonModel model = ((Clickable) figure).getModel();
        Rectangle r = getPaintRectangle(figure, insets);
        g.setLineWidth(1);
        r.width -= 3;
        r.height -= 3;

        if (model.isMouseOver() && !model.isArmed()) {
            g.setForegroundColor(highlight);
            g.drawRectangle(r);

            r.translate(1, 1);
            g.setForegroundColor(dropshadow2);
            g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
            g.drawLine(r.right(), r.y, r.right(), r.bottom());

            r.translate(1, 1);
            g.setForegroundColor(dropshadow3);
            g.drawLine(r.x + 1, r.bottom(), r.right() - 1, r.bottom());
            g.drawLine(r.right(), r.y + 1, r.right(), r.bottom() - 1);
        } else if (model.isPressed()) {
            r.translate(1, 1);

            g.setForegroundColor(highlight);
            g.drawRectangle(r);

            r.translate(1, 1);
            g.setForegroundColor(dropshadow2);
            g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
            g.drawLine(r.right(), r.y, r.right(), r.bottom());
        } else {
            r.translate(1, 1);

            g.setForegroundColor(dropshadow3);
            g.drawRectangle(r);

            r.translate(1, 1);
            g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
            g.drawLine(r.right(), r.y, r.right(), r.bottom());
        }

    }

}
