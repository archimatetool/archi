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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Pratik Shah
 */
public class GuidePlaceHolder extends GuideFigure {

    public GuidePlaceHolder(boolean isHorizontal) {
        super(isHorizontal);
        setBackgroundColor(ColorConstants.lightGray);
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        PointList list = new PointList();
        if (isHorizontal()) {
            Rectangle clientArea = getClientArea();
            clientArea.x = clientArea.getTopRight().x - 7;
            clientArea.y++;
            list.addPoint(clientArea.x, clientArea.y);
            list.addPoint(clientArea.x + 3, clientArea.y);
            list.addPoint(clientArea.x + 6, clientArea.y + 3);
            list.addPoint(clientArea.x + 3, clientArea.y + 6);
            list.addPoint(clientArea.x, clientArea.y + 6);
            graphics.fillPolygon(list);
            graphics.drawPolygon(list);
            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawLine(clientArea.x - 1, clientArea.y, clientArea.x - 1,
                    clientArea.y + 6);
            graphics.drawLine(clientArea.x, clientArea.y - 1, clientArea.x + 3,
                    clientArea.y - 1);
            graphics.drawLine(clientArea.x, clientArea.y + 7, clientArea.x + 3,
                    clientArea.y + 7);
        } else {
            Rectangle clientArea = getClientArea();
            clientArea.y = clientArea.getBottomLeft().y - 7;
            clientArea.x++;
            list.addPoint(clientArea.x, clientArea.y);
            list.addPoint(clientArea.x + 6, clientArea.y);
            list.addPoint(clientArea.x + 6, clientArea.y + 3);
            list.addPoint(clientArea.x + 3, clientArea.y + 6);
            list.addPoint(clientArea.x, clientArea.y + 3);
            graphics.fillPolygon(list);
            graphics.drawPolygon(list);
            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawLine(clientArea.x, clientArea.y - 1, clientArea.x + 6,
                    clientArea.y - 1);
            graphics.drawLine(clientArea.x - 1, clientArea.y, clientArea.x - 1,
                    clientArea.y + 3);
            graphics.drawLine(clientArea.x + 7, clientArea.y, clientArea.x + 7,
                    clientArea.y + 3);
        }
    }

}
