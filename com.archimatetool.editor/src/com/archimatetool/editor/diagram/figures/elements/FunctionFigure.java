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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Function Figure
 * 
 * @author Phillip Beauvoir
 */
public class FunctionFigure extends AbstractTextControlContainerFigure {

    public FunctionFigure() {
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
        
        PointList points = new PointList();
        
        // Start at bottom left
        Point pt = getIconOrigin();
        points.addPoint(pt);
        
        pt.translate(0, -9);
        points.addPoint(pt);
        
        pt.translate(6, -5);
        points.addPoint(pt);
        
        pt.translate(6, 5);
        points.addPoint(pt);
        
        pt.translate(0, 9);
        points.addPoint(pt);
        
        pt.translate(-6, -6);
        points.addPoint(pt);
        
        graphics.drawPolygon(points);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 18, bounds.y + 19);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new RoundedRectangleAnchor(this);
    }

}
