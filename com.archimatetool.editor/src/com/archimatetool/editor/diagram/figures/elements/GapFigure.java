/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;




/**
 * Gap Figure
 * 
 * @author Phillip Beauvoir
 */
public class GapFigure
extends DeliverableFigure {
    
    public GapFigure() {
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        drawIcon(graphics);
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!getDiagramModelObject().isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawOval(pt.x, pt.y, 13, 13);
        
        pt.translate(-2, 5);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
        
        pt.translate(0, 3);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 6);
    }
    
    @Override
    protected int getIconOffset() {
        return 23;
    }

}
