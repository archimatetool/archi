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
 * Plateau Figure
 * 
 * @author Phillip Beauvoir
 */
public class PlateauFigure extends AbstractTextControlContainerFigure {
    
    public PlateauFigure() {
        super(TEXT_FLOW_CONTROL);
        setFigureDelegate(new BoxFigureDelegate(this, 22 - getTextControlMarginWidth()));
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
        
        graphics.setLineWidth(2);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 34, bounds.y + 29);
    }
}
