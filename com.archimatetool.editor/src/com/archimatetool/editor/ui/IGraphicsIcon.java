/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;



/**
 * Icon as drawn in a Draw2d Graphics mode
 * Each instance is responsible for drawing the icon to the Graphics instance
 * 
 * @author Phillip Beauvoir
 */
public interface IGraphicsIcon {

    /**
     * Draw the icon to the Graphics instance
     * @param graphics The Graphics instance
     * @param origin The drawing origin
     */
    void drawIcon(Graphics graphics, Point origin);
    
}
