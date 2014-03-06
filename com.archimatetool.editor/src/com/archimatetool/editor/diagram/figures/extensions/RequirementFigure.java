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
 * Figure for a Requirement
 * 
 * @author Phillip Beauvoir
 */
public class RequirementFigure extends AbstractMotivationFigure {
    
    public RequirementFigure(IDiagramModelArchimateObject diagramModelObject) {
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
        
        graphics.drawPolygon(new int[] {
                pt.x, pt.y,
                pt.x + 12, pt.y,
                pt.x + 8, pt.y + 9,
                pt.x - 4, pt.y + 9,
        });
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 18, bounds.y + 8);
    }
}