/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Interaction Figure
 * 
 * @author Phillip Beauvoir
 */
public class InteractionFigure extends AbstractTextControlContainerFigure {

    public InteractionFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rounded Rectangle Figure Delegate to Draw
        setFigureDelegate(new RoundedRectangleFigureDelegate(this, 20 - getTextControlMarginWidth()));
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
        
        // Start at top
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        path.addArc(pt.x - 5, pt.y, 10, 12, 90, 180);
        path.lineTo(pt.x, pt.y - 0.5f);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(pt.x - 2, pt.y, 10, 12, -90, 180);
        path.lineTo(pt.x + 3, pt.y + 12.5f);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 13, bounds.y + 6);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new RoundedRectangleAnchor(this);
    }
}
