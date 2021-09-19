/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.IGraphicsIcon;


/**
 * Draw2d Icon for an ArchimateDiagramModel
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelGraphicsIcon implements IGraphicsIcon {
    
    private static Color blue1 = new Color(20, 105, 171);
    private static Color blue2 = new Color(193, 232, 255);
    private static Color blue3 = new Color(220, 240, 250);

    @Override
    public void drawIcon(Graphics graphics, Point origin) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        
        // Note - don't use graphics.fillGradient as it messes up the co-ordinate system on Linux hi-res
        graphics.setBackgroundColor(blue3);
        graphics.fillRectangle(origin.x, origin.y, 5, 5);
        graphics.fillRectangle(origin.x, origin.y + 9, 5, 5);
        
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
