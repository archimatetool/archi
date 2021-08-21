/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author Pratik Shah
 */
public class GuideFigure extends Figure {

    private static final Dimension H_PREFSIZE = new Dimension(9, 11);
    private static final Dimension V_PREFSIZE = new Dimension(11, 9);

    private boolean horizontal;
    private boolean drawFocus;

    public GuideFigure(boolean isHorizontal) {
        horizontal = isHorizontal;
        setBackgroundColor(ColorConstants.button);
        if (horizontal) {
            setCursor(Cursors.SIZENS);
        } else {
            setCursor(Cursors.SIZEWE);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        Dimension prefSize;
        if (isHorizontal()) {
            prefSize = H_PREFSIZE;
        } else {
            prefSize = V_PREFSIZE;
        }
        if (getBorder() != null) {
            prefSize = prefSize.getExpanded(getInsets().getWidth(), getInsets()
                    .getHeight());
        }
        return prefSize;
    }

    @Override
    public void handleFocusGained(FocusEvent event) {
        super.handleFocusGained(event);
        repaint();
        getUpdateManager().performUpdate();
    }

    @Override
    public void handleFocusLost(FocusEvent event) {
        super.handleFocusLost(event);
        repaint();
        getUpdateManager().performUpdate();
    }

    protected boolean isHorizontal() {
        return horizontal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        // Since painting can occur a lot, using a transposer is not good for
        // performance.
        // Hence, this method does not use it.
        if (isHorizontal()) {
            Rectangle clientArea = getClientArea();
            clientArea.shrink(0, 1);
            clientArea.x = clientArea.x + clientArea.width - 8;
            clientArea.width = 8;

            graphics.fillRectangle(clientArea
                    .getShrinked(new Insets(2, 2, 2, 1)));

            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawLine(clientArea.x, clientArea.y + 1, clientArea.x,
                    clientArea.y + 7);
            graphics.drawLine(clientArea.x + 1, clientArea.y, clientArea.x + 4,
                    clientArea.y);
            graphics.drawLine(clientArea.x + 1, clientArea.y + 8,
                    clientArea.x + 4, clientArea.y + 8);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 2,
                    clientArea.x + 2, clientArea.y + 5);
            graphics.drawLine(clientArea.x + 3, clientArea.y + 2,
                    clientArea.x + 3, clientArea.y + 2);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 2,
                    clientArea.x + 6, clientArea.y + 2);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 6,
                    clientArea.x + 6, clientArea.y + 6);
            graphics.drawLine(clientArea.x + 7, clientArea.y + 3,
                    clientArea.x + 7, clientArea.y + 5);

            graphics.setForegroundColor(ColorConstants.buttonDarker);
            graphics.drawLine(clientArea.x + 1, clientArea.y + 1,
                    clientArea.x + 4, clientArea.y + 1);
            graphics.drawLine(clientArea.x + 1, clientArea.y + 2,
                    clientArea.x + 1, clientArea.y + 7);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 7,
                    clientArea.x + 2, clientArea.y + 7);
            graphics.drawLine(clientArea.x + 5, clientArea.y + 2,
                    clientArea.x + 5, clientArea.y + 2);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 3,
                    clientArea.x + 6, clientArea.y + 3);

            graphics.setForegroundColor(ColorConstants.buttonDarkest);
            graphics.drawLine(clientArea.x + 3, clientArea.y + 7,
                    clientArea.x + 4, clientArea.y + 7);
            graphics.drawLine(clientArea.x + 5, clientArea.y + 6,
                    clientArea.x + 5, clientArea.y + 6);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 5,
                    clientArea.x + 6, clientArea.y + 5);
            graphics.drawLine(clientArea.x + 7, clientArea.y + 4,
                    clientArea.x + 7, clientArea.y + 4);

            if (drawFocus) {
                clientArea.expand(1, 1);
                clientArea.height -= 1;
                graphics.setForegroundColor(ColorConstants.black);
                graphics.setBackgroundColor(ColorConstants.white);
                graphics.drawFocus(clientArea);
            }
        } else {
            Rectangle clientArea = getClientArea();
            clientArea.shrink(1, 0);
            clientArea.y = clientArea.y + clientArea.height - 8;
            clientArea.height = 8;

            graphics.fillRectangle(clientArea
                    .getShrinked(new Insets(2, 2, 1, 2)));

            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawLine(clientArea.x + 1, clientArea.y, clientArea.x + 7,
                    clientArea.y);
            graphics.drawLine(clientArea.x, clientArea.y + 1, clientArea.x,
                    clientArea.y + 4);
            graphics.drawLine(clientArea.x + 8, clientArea.y + 1,
                    clientArea.x + 8, clientArea.y + 4);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 2,
                    clientArea.x + 5, clientArea.y + 2);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 3,
                    clientArea.x + 2, clientArea.y + 3);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 6,
                    clientArea.x + 2, clientArea.y + 6);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 6,
                    clientArea.x + 6, clientArea.y + 6);
            graphics.drawLine(clientArea.x + 3, clientArea.y + 7,
                    clientArea.x + 5, clientArea.y + 7);

            graphics.setForegroundColor(ColorConstants.buttonDarker);
            graphics.drawLine(clientArea.x + 1, clientArea.y + 1,
                    clientArea.x + 1, clientArea.y + 4);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 1,
                    clientArea.x + 7, clientArea.y + 1);
            graphics.drawLine(clientArea.x + 7, clientArea.y + 2,
                    clientArea.x + 7, clientArea.y + 2);
            graphics.drawLine(clientArea.x + 2, clientArea.y + 5,
                    clientArea.x + 2, clientArea.y + 5);
            graphics.drawLine(clientArea.x + 3, clientArea.y + 6,
                    clientArea.x + 3, clientArea.y + 6);

            graphics.setForegroundColor(ColorConstants.buttonDarkest);
            graphics.drawLine(clientArea.x + 7, clientArea.y + 3,
                    clientArea.x + 7, clientArea.y + 4);
            graphics.drawLine(clientArea.x + 6, clientArea.y + 5,
                    clientArea.x + 6, clientArea.y + 5);
            graphics.drawLine(clientArea.x + 5, clientArea.y + 6,
                    clientArea.x + 5, clientArea.y + 6);
            graphics.drawLine(clientArea.x + 4, clientArea.y + 7,
                    clientArea.x + 4, clientArea.y + 7);

            if (drawFocus) {
                clientArea.expand(1, 1);
                clientArea.width -= 1;
                graphics.setForegroundColor(ColorConstants.black);
                graphics.setBackgroundColor(ColorConstants.white);
                graphics.drawFocus(clientArea);
            }
        }
    }

    public void setDrawFocus(boolean drawFocus) {
        if (this.drawFocus != drawFocus) {
            this.drawFocus = drawFocus;
            repaint();
        }
    }

}
