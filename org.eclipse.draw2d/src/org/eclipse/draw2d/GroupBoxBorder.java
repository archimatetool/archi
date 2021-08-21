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
 * A labeled border intended to house a Figure with a group of children. The
 * label should serve as a description of the group.
 */
public class GroupBoxBorder extends AbstractLabeledBorder {

    /**
     * Constructs a GroupBoxBorder with the name of this class as its label.
     * 
     * @since 2.0
     */
    public GroupBoxBorder() {
    }

    /**
     * Constructs a GroupBoxBorder with label s.
     * 
     * @param s
     *            the label
     * @since 2.0
     */
    public GroupBoxBorder(String s) {
        super(s);
    }

    /**
     * Calculates and returns the Insets for this GroupBoxBorder.
     * 
     * @param figure
     *            IFigure on which the calculations should be made. Generally
     *            this is the IFigure of which this GroupBoxBorder is
     *            surrounding.
     * @return the Insets for this GroupBoxBorder.
     * @since 2.0
     */
    @Override
    protected Insets calculateInsets(IFigure figure) {
        int height = getTextExtents(figure).height;
        return new Insets(height);
    }

    /**
     * @see org.eclipse.draw2d.Border#getPreferredSize(IFigure)
     */
    @Override
    public Dimension getPreferredSize(IFigure fig) {
        Dimension textSize = getTextExtents(fig);
        return textSize.getCopy().expand(textSize.height * 2, 0);
    }

    /**
     * @see Border#paint(IFigure, Graphics, Insets)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void paint(IFigure figure, Graphics g, Insets insets) {
        tempRect.setBounds(getPaintRectangle(figure, insets));
        Rectangle r = tempRect;
        if (r.isEmpty())
            return;

        Rectangle textLoc = new Rectangle(r.getTopLeft(),
                getTextExtents(figure));
        r.crop(new Insets(getTextExtents(figure).height / 2));
        FigureUtilities.paintEtchedBorder(g, r);

        textLoc.x += getInsets(figure).left;
        g.setFont(getFont(figure));
        g.setForegroundColor(getTextColor());
        g.clipRect(textLoc);
        g.fillText(getLabel(), textLoc.getTopLeft());
    }

}
