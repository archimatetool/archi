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
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;


/**
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 */
public class CapabilityFigure extends AbstractTextControlContainerFigure {
    
    public CapabilityFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rounded Rectangle Figure Delegate to Draw
        setFigureDelegate(new RoundedRectangleFigureDelegate(this, 19 - getTextControlMarginWidth()));
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
        
        graphics.drawRectangle(pt.x + 8, pt.y, 4, 4);
        
        graphics.drawRectangle(pt.x + 4, pt.y + 4, 4, 4);
        graphics.drawRectangle(pt.x + 8, pt.y + 4, 4, 4);
        
        graphics.drawRectangle(pt.x, pt.y + 8, 4, 4);
        graphics.drawRectangle(pt.x + 4, pt.y + 8, 4, 4);
        graphics.drawRectangle(pt.x + 8, pt.y + 8, 4, 4);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 17, bounds.y + 5);
    }

}