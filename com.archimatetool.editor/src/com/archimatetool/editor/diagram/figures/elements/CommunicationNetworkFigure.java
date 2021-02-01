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
 * Communication Network Figure
 * 
 * @author Phillip Beauvoir
 */
public class CommunicationNetworkFigure extends AbstractTextControlContainerFigure {

    public CommunicationNetworkFigure() {
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
        
        graphics.setLineWidthFloat(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 5, 5, 0, 360);
        path.addArc(pt.x + 2, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 10, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 8, pt.y, 5, 5, 0, 360);
        
        path.moveTo(pt.x + 3, pt.y);
        path.lineTo(pt.x + 4, pt.y - 3);
        
        path.moveTo(pt.x + 11, pt.y);
        path.lineTo(pt.x + 12, pt.y - 3);
        
        path.moveTo(pt.x + 5, pt.y + 2.5f);
        path.lineTo(pt.x + 8, pt.y + 2.5f);
        
        path.moveTo(pt.x + 7, pt.y - 5.5f);
        path.lineTo(pt.x + 10, pt.y - 5.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 14);
    }

}
