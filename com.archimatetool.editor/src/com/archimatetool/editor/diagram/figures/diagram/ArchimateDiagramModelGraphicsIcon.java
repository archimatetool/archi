/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IGraphicsIcon;


/**
 * Draw2d Icon for an ArchimateDiagramModel
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelGraphicsIcon implements IGraphicsIcon {
    
    static Color blue1 = ColorFactory.get(20, 105, 171);
    static Color blue2 = ColorFactory.get(193, 232, 255);
    static Color blue3 = ColorFactory.get(225, 246, 255);
    static Color blue4 = ColorFactory.get(150, 210, 247);

    @Override
    public void drawIcon(Graphics graphics, Point origin) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        
        // fills
        graphics.setForegroundColor(blue3);
        graphics.setBackgroundColor(blue4);
        graphics.fillGradient(origin.x, origin.y, 5, 5, true);
        graphics.fillGradient(origin.x, origin.y + 9, 5, 5, true);
        
        graphics.setForegroundColor(blue1);
        
        // squares
        graphics.drawRectangle(origin.x, origin.y, 5, 5);
        graphics.drawRectangle(origin.x, origin.y + 9, 5, 5);
        
        graphics.drawRectangle(origin.x + 8, origin.y + 6, 2, 2); // little square
        
        // lines
        graphics.translate(7, 2);
        graphics.setForegroundColor(blue2);
        graphics.drawLine(origin.x, origin.y, origin.x + 6, origin.y);
        graphics.translate(0, 1);
        graphics.setForegroundColor(blue1);
        graphics.drawLine(origin.x, origin.y, origin.x + 6, origin.y);
        
        graphics.translate(0, 8);
        graphics.setForegroundColor(blue2);
        graphics.drawLine(origin.x, origin.y, origin.x + 6, origin.y);
        graphics.translate(0, 1);
        graphics.setForegroundColor(blue1);
        graphics.drawLine(origin.x, origin.y, origin.x + 6, origin.y);
        
        // small line
        graphics.translate(4, -5);
        graphics.setForegroundColor(blue1);
        graphics.drawLine(origin.x, origin.y, origin.x + 2, origin.y);
        
        graphics.popState();
    }

}
