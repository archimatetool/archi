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

import com.archimatetool.editor.ui.IGraphicsIcon;


/**
 * Draw2d Icon for a Canvas model
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelGraphicsIcon implements IGraphicsIcon {
    
    private static Color blue = new Color(200, 230, 247);

    @Override
    public void drawIcon(Graphics graphics, Point origin) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        
        graphics.setBackgroundColor(ColorConstants.white);
        graphics.fillRectangle(origin.x, origin.y, 14, 12);
        
        // Note - don't use graphics.fillGradient as it messes up the co-ordinate system on Linux hi-res
        graphics.setBackgroundColor(blue);
        graphics.fillRectangle(origin.x, origin.y, 4, 8);
        graphics.fillRectangle(origin.x, origin.y + 8, 14, 4);
        
        graphics.setForegroundColor(ColorConstants.darkGray);

        graphics.drawRectangle(origin.x, origin.y, 14, 12);
        
        graphics.drawLine(origin.x, origin.y + 8, origin.x + 14, origin.y + 8);
        graphics.drawLine(origin.x + 4, origin.y, origin.x + 4, origin.y + 8);
        graphics.drawLine(origin.x + 10, origin.y, origin.x + 10, origin.y + 8);
        
        graphics.popState();
    }

}
