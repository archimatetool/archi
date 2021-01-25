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
 * Figure for an Outcome
 * 
 * @author Phillip Beauvoir
 */
public class OutcomeFigure extends AbstractMotivationFigure {
    
    public OutcomeFigure() {
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
        if(hasIconImage()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);

        graphics.setLineWidthFloat(1.2f);
        
        // circles
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.setLineWidth(1);

        // lines
        path = new Path(null);
        
        path.moveTo(pt.x + 6.0f, pt.y + 7.0f);
        path.lineTo(pt.x + 15.5f, pt.y - 2.5f);
        
        path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
        path.lineTo(pt.x + 14.0f, pt.y - 5.0f);
        
        path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
        path.lineTo(pt.x + 18.0f, pt.y - 1.0f);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 25, bounds.y + 9);
    }
    
    @Override
    protected int getIconOffset() {
        return 27;
    }

}