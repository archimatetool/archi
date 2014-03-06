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
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Business Actor
 * 
 * @author Phillip Beauvoir
 */
public class BusinessLocationFigure
extends AbstractArchimateFigure {
    
    public BusinessLocationFigure(IDiagramModelArchimateObject diagramModelObject) {
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
        
        // Start at top
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.moveTo(pt.x, pt.y);
        path.addArc(pt.x - 5, pt.y - 15, 10, 10, -20, 220);
        path.lineTo(pt.x, pt.y);
        path.lineTo(pt.x + 4.8f, pt.y - 8.5f);
        
        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 13, bounds.y + 20);
    }

}