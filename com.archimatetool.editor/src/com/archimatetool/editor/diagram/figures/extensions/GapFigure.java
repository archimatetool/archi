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

import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Gap Figure
 * 
 * @author Phillip Beauvoir
 */
public class GapFigure
extends DeliverableFigure {
    
    protected int SHADOW_OFFSET = 3;

    public GapFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
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
        
        graphics.drawOval(pt.x, pt.y, 13, 13);
        
        pt.translate(-2, 5);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
        
        pt.translate(0, 3);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 21, bounds.y + 7);
    }
}
