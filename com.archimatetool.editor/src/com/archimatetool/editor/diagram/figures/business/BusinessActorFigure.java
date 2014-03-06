/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Business Actor
 * 
 * @author Phillip Beauvoir
 */
public class BusinessActorFigure
extends AbstractArchimateFigure {
    
    public BusinessActorFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this);
        setFigureDelegate(figureDelegate);
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
        graphics.setLineWidth(1);
        graphics.setForegroundColor(ColorConstants.black);
        
        Point pt = getIconOrigin();
        
        // head
        graphics.drawOval(pt.x, pt.y, 6, 6);
        
        // body
        pt.translate(3, 6);
        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
        
        // legs
        pt.translate(0, 6);
        graphics.drawLine(pt.x, pt.y, pt.x - 4, pt.y + 5);
        graphics.drawLine(pt.x, pt.y, pt.x + 4, pt.y + 5);
        
        // arms
        pt.translate(-4, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 8, pt.y);
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 16, bounds.y + 5);
    }

}