/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;



/**
 * Icon is drawn in a Draw2d Graphics instance at an origin point
 * 
 * @author Phillip Beauvoir
 */
public interface IIconDelegate {

    /**
     * Draw a figure icon
     * @param graphics The graphics to draw on
     * @param foregroundColor
     * @param backgroundColor
     * @param pt The start point to draw the icon
     */
    void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point origin);
}
