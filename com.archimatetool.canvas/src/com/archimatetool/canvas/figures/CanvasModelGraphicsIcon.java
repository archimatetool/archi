/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IGraphicsIcon;


/**
 * Draw2d Icon for a Canvas model
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelGraphicsIcon implements IGraphicsIcon {
    
    static Color blue = ColorFactory.get(150, 210, 247);

    @Override
    public void drawIcon(Graphics graphics, Point origin) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        
        graphics.setBackgroundColor(ColorConstants.white);
        graphics.fillRectangle(origin.x, origin.y, 14, 12);
        
        graphics.setForegroundColor(ColorConstants.white);
        graphics.setBackgroundColor(blue);
        
        graphics.fillGradient(origin.x, origin.y, 4, 8, true);
        graphics.fillGradient(origin.x, origin.y + 9, 14, 3, true);
        
        graphics.setForegroundColor(ColorConstants.darkGray);

        graphics.drawRectangle(origin.x, origin.y, 14, 12);
        
        graphics.drawLine(origin.x, origin.y + 8, origin.x + 14, origin.y + 8);
        graphics.drawLine(origin.x + 4, origin.y, origin.x + 4, origin.y + 8);
        graphics.drawLine(origin.x + 10, origin.y, origin.x + 10, origin.y + 8);
        
        graphics.popState();
    }

}
