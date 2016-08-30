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

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * System Software Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class SystemSoftwareFigure
extends AbstractArchimateFigure {

    public SystemSoftwareFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this));
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
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 11, 11, 90, 360);
        path.addArc(pt.x + 2, pt.y - 2, 11, 11, -60, 210);

        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 19, bounds.y + 9);
    }    
}
