/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Path Figure
 * 
 * @author Phillip Beauvoir
 */
public class PathFigure extends DistributionNetworkFigure {
    
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
    
    @Override
    protected void fillSection(Graphics graphics, Rectangle rect, Dimension arrowSize) {
        // No fill
    }
    
    /**
     * Draw the icon
     */
    @Override
    protected void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidthFloat(1.5f);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
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
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 19 - getLineWidth(), rect.y + 12);
    }
}
