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
 * Figure for a Principle
 * 
 * @author Phillip Beauvoir
 */
public class PrincipleFigure extends AbstractMotivationFigure {
    
    public PrincipleFigure() {
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
        
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 12, 14), 4, 4);
        
        Path path = new Path(null);

        path.moveTo(pt.x + 5.5f, pt.y + 2);
        path.lineTo(pt.x + 5.5f, pt.y + 9);
        
        path.moveTo(pt.x + 6.5f, pt.y + 2);
        path.lineTo(pt.x + 6.5f, pt.y + 9);
        
        path.moveTo(pt.x + 5.5f, pt.y + 10.5f);
        path.lineTo(pt.x + 5.5f, pt.y + 12.5f);
        
        path.moveTo(pt.x + 6.5f, pt.y + 10.5f);
        path.lineTo(pt.x + 6.5f, pt.y + 12.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
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