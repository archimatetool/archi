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
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 */
public class ResourceFigure extends AbstractTextControlContainerFigure {
    
    public ResourceFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this, 22 - getTextControlMarginWidth()));
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
        
        // main rectangle
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 15, 10), 3, 3);
        
        // nub
        pt.translate(15, 3);
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 2, 4), 1, 1);
        
        // lines
        pt = getIconOrigin();
        Path path = new Path(null);
        
        path.moveTo(pt.x + 3f, pt.y + 2);
        path.lineTo(pt.x + 3f, pt.y + 8);
        
        path.moveTo(pt.x + 6f, pt.y + 2);
        path.lineTo(pt.x + 6f, pt.y + 8);
        
        path.moveTo(pt.x + 9f, pt.y + 2);
        path.lineTo(pt.x + 9f, pt.y + 8);
        
        graphics.drawPath(path);
        path.dispose();

        // Alternate method of drawing lines
//        pt.translate(-12, -1);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//        pt.translate(3, 0);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//        pt.translate(3, 0);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 20, bounds.y + 7);
    }

}