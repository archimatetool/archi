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
 * Material Figure
 * 
 * @author Phillip Beauvoir
 */
public class MaterialFigure extends AbstractTextControlContainerFigure {

    public MaterialFigure() {
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
        
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        graphics.setLineWidthFloat(1.2f);
        Point pt = getIconOrigin();
        
        graphics.drawPolygon(new int[] {
                pt.x + 4, pt.y - 7,
                pt.x - 4, pt.y - 7,
                
                pt.x - 8, pt.y,
                pt.x - 5, pt.y + 7,
                
                pt.x + 4, pt.y + 7,
                pt.x + 8, pt.y,
        });

        
        Path path = new Path(null);

        path.moveTo(pt.x - 2, pt.y - 5);
        path.lineTo(pt.x - 5.3f, pt.y + 0.5f);
        
        path.moveTo(pt.x - 3.7f, pt.y + 4.5f);
        path.lineTo(pt.x + 3, pt.y + 4.5f);
        
        path.moveTo(pt.x + 5f, pt.y + 0.5f);
        path.lineTo(pt.x + 2f, pt.y - 5);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 12, bounds.y + 12);
    }
}
