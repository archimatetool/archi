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
import org.eclipse.swt.graphics.Path;


/**
 * Figure for a Driver
 * 
 * @author Phillip Beauvoir
 */
public class DriverFigure extends AbstractMotivationFigure {
    
    public DriverFigure() {
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
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        graphics.setLineWidthFloat(1.2f);
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.setLineWidth(1);
        
        path = new Path(null);
        
        path.moveTo(pt.x - 2, pt.y + 6.5f);
        path.lineTo(pt.x + 15, pt.y + 6.5f);
        
        path.moveTo(pt.x + 6.5f, pt.y - 2);
        path.lineTo(pt.x + 6.5f, pt.y + 15);
       
        path.moveTo(pt.x + 0.5f, pt.y + 0.5f);
        path.lineTo(pt.x + 12.5f, pt.y + 12.5f);
        
        path.moveTo(pt.x + 0.5f, pt.y + 12.5f);
        path.lineTo(pt.x + 12.5f, pt.y + 0.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 21, bounds.y + 6);
    }
    
    @Override
    protected int getIconOffset() {
        return 23;
    }

}