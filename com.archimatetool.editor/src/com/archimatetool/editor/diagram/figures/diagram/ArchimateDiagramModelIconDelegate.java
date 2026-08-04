/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Draw2d Icon delegate for an ArchimateDiagramModel
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelIconDelegate implements IIconDelegate {
    
    private static Color blue1 = new Color(20, 105, 171);
    private static Color blue2 = new Color(193, 232, 255);
    private static Color blue3 = new Color(220, 240, 250);

    @Override
    public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point origin) {
        graphics.pushState();
        
        // Ensure this is set
        graphics.setAntialias(SWT.ON);
        
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

    @Override
    public Rectangle getBounds() {
        // Mirrors the shapes drawn in drawIcon() above (with origin = (0, 0)). drawIcon() repeatedly calls
        // graphics.translate() to position the lines below the two squares - the translations are cumulative,
        // so the absolute coordinates used below sum the successive offsets: (7, 2), then (0, 1), then (0, 8),
        // then (0, 1), then (4, -5)
        Rectangle bounds = new Rectangle(0, 0, 5, 5); // top square
        bounds = bounds.union(new Rectangle(0, 9, 5, 5)); // bottom square
        bounds = bounds.union(new Rectangle(8, 6, 2, 2)); // little square
        bounds = bounds.union(new Rectangle(7, 2, 6, 0)); // line, after translate(7, 2)
        bounds = bounds.union(new Rectangle(7, 3, 6, 0)); // line, after translate(7, 2) + (0, 1)
        bounds = bounds.union(new Rectangle(7, 11, 6, 0)); // line, after translate(7, 2) + (0, 1) + (0, 8)
        bounds = bounds.union(new Rectangle(7, 12, 6, 0)); // line, after translate(7, 2) + (0, 1) + (0, 8) + (0, 1)
        bounds = bounds.union(new Rectangle(11, 7, 2, 0)); // small line, after translate(7, 2) + (0, 1) + (0, 8) + (0, 1) + (4, -5)
        return bounds;
    }

}
