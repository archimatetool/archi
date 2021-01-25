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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;


/**
 * Facility Figure
 * 
 * @author Phillip Beauvoir
 */
public class FacilityFigure extends AbstractTextControlContainerFigure {
    
    public FacilityFigure() {
        super(TEXT_FLOW_CONTROL);
        setFigureDelegate(new BoxFigureDelegate(this, 20 - getTextControlMarginWidth()));
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
        
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        graphics.setLineWidthFloat(1.2f);
        Point pt = getIconOrigin();
        
        graphics.drawPolygon(new int[] {
                pt.x , pt.y,
                pt.x + 15, pt.y,
                
                pt.x + 15, pt.y - 6,
                pt.x + 11, pt.y - 3,
                
                pt.x + 11, pt.y - 6,
                pt.x + 7, pt.y - 3,
                
                pt.x + 7, pt.y - 6,
                pt.x + 3, pt.y - 3,
                
                pt.x + 3, pt.y - 12,
                pt.x, pt.y - 12
        });
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 34, bounds.y + 30);
    }
}
