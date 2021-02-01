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
 * Figure for a Stakeholder
 * 
 * @author Phillip Beauvoir
 */
public class StakeholderFigure extends AbstractMotivationFigure {
    
    public StakeholderFigure() {
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
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 8, 7, 90, 180);
        
        path.lineTo(pt.x + 11, pt.y + 7);
        
        path.moveTo(pt.x + 3.5f, pt.y);
        path.lineTo(pt.x + 11, pt.y);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.drawOval(pt.x + 8, pt.y, 7, 7);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 21, bounds.y + 9);
    }
    
    @Override
    protected int getIconOffset() {
        return 23;
    }
}