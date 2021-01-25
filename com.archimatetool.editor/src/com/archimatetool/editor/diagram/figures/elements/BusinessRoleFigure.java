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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;


/**
 * Figure for a Business Role
 * 
 * @author Phillip Beauvoir
 */
public class BusinessRoleFigure extends AbstractTextControlContainerFigure {
    
    public BusinessRoleFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this, 20 - getTextControlMarginWidth()));
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
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 5, 8, 90, 180);
        
        path.lineTo(pt.x + 12, pt.y + 8);
        
        path.moveTo(pt.x + 2f, pt.y);
        path.lineTo(pt.x + 12, pt.y);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.drawOval(pt.x + 10, pt.y, 5, 8);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 18, bounds.y + 7);
    }
}