/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Technology Communication Path Figure
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyCommunicationPathFigure
extends AbstractArchimateFigure {

    public TechnologyCommunicationPathFigure(IDiagramModelArchimateObject diagramModelObject) {
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
        graphics.setLineWidthFloat(1.4f);
        graphics.setForegroundColor(ColorConstants.black);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.moveTo(pt.x + 3, pt.y);
        path.lineTo(pt.x + 6, pt.y);
        
        path.moveTo(pt.x + 9, pt.y);
        path.lineTo(pt.x + 12, pt.y);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.setLineWidthFloat(1.6f);
        
        path = new Path(null);
        
        path.moveTo(pt.x + 5, pt.y - 5);
        path.lineTo(pt.x, pt.y);
        path.lineTo(pt.x + 5, pt.y + 5);
        
        path.moveTo(pt.x + 10, pt.y - 5);
        path.lineTo(pt.x + 15, pt.y);
        path.lineTo(pt.x + 10, pt.y + 5);
 
        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 12);
    }
}
