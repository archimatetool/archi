/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

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
        setFigureDelegate(new BoxFigureDelegate(this));
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
        graphics.pushState();
        
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 33, bounds.y + 27);
    }
}
