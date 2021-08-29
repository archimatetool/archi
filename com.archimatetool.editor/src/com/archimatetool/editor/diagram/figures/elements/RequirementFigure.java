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

import com.archimatetool.editor.diagram.figures.IFigureDelegate;


/**
 * Figure for a Requirement
 * 
 * @author Phillip Beauvoir
 */
public class RequirementFigure extends AbstractMotivationFigure {
    
    protected IFigureDelegate fAltFigureDelegate;
    
    public RequirementFigure() {
        fAltFigureDelegate = new ParallelogramFigureDelegate(this, false);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }

        super.drawFigure(graphics);
        drawIcon(graphics);
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawPolygon(new int[] {
                pt.x, pt.y,
                pt.x + 12, pt.y,
                pt.x + 8, pt.y + 9,
                pt.x - 4, pt.y + 9,
        });
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 18, bounds.y + 8);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? null : fAltFigureDelegate;
    }
}