/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;




/**
 * Path Figure
 * 
 * @author Phillip Beauvoir
 */
public class PathFigure extends CommunicationNetworkFigure {
    
    public PathFigure() {
    }
    
    @Override
    protected void drawHorizontalLine(Graphics graphics, Rectangle rect, Dimension arrow) {
        // Line dashes
        graphics.setLineStyle(SWT.LINE_CUSTOM);
        graphics.setLineDash(new float[] { graphics.getLineWidthFloat() * 2, graphics.getLineWidthFloat() });
        
        graphics.setLineCap(SWT.CAP_FLAT);
        
        graphics.drawLine(rect.x,
                rect.y + rect.height / 2,
                rect.x + rect.width,
                rect.y + rect.height / 2);
    }
    
    /**
     * Draw the icon
     */
    @Override
    protected void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidthFloat(1.5f);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.moveTo(pt.x + 2.5f, pt.y);
        path.lineTo(pt.x + 4.5f, pt.y);
        
        path.moveTo(pt.x + 6.5f, pt.y);
        path.lineTo(pt.x + 8.5f, pt.y);
        
        path.moveTo(pt.x + 10.5f, pt.y);
        path.lineTo(pt.x + 12.5f, pt.y);
        
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        
        path.moveTo(pt.x + 4, pt.y - 5);
        path.lineTo(pt.x - 1, pt.y);
        path.lineTo(pt.x + 4, pt.y + 5);
        
        path.moveTo(pt.x + 11, pt.y - 5);
        path.lineTo(pt.x + 16, pt.y);
        path.lineTo(pt.x + 11, pt.y + 5);
 
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    @Override
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 12);
    }
}
