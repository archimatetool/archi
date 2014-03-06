/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.extensions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.technology.TechnologyDeviceFigureDelegate2;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Plateau Figure
 * 
 * @author Phillip Beauvoir
 */
public class PlateauFigure
extends AbstractArchimateFigure {
    
    public PlateauFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        TechnologyDeviceFigureDelegate2 figureDelegate = new TechnologyDeviceFigureDelegate2(this);
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
        graphics.setLineWidth(2);
        graphics.setForegroundColor(ColorConstants.black);
        
        Point pt = getIconOrigin();
        
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 33, bounds.y + 27);
    }
}
