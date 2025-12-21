/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Principle
 * 
 * @author Phillip Beauvoir
 */
public class PrincipleFigure extends AbstractMotivationFigure {
    
    public PrincipleFigure() {
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);

        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int divisions = 24;
        int div1 = 8;
        int div2 = 16;
        
        int fractionX = rect.width / divisions;
        int fractionY = rect.height / divisions;
        int corner = Math.min(fractionX, fractionY);
        
        path.moveTo(rect.x + corner, rect.y + corner);

        path.cubicTo(rect.x + fractionX * div1,
                     rect.y,
                     rect.x + fractionX * div2,
                     rect.y,
                     rect.x + rect.width - corner,
                     rect.y + corner);
        
        path.cubicTo(rect.x + rect.width,
                     rect.y + fractionY * div1,
                     rect.x + rect.width,
                     rect.y + fractionY * div2,
                     rect.x + rect.width - corner,
                     rect.y + rect.height - corner);
        
        path.cubicTo(rect.x + fractionX * div2,
                     rect.y + rect.height,
                     rect.x + fractionX * div1,
                     rect.y + rect.height,
                     rect.x + corner,
                     rect.y + rect.height - corner);
   
        path.cubicTo(rect.x,
                     rect.y + fractionY * div2,
                     rect.x,
                     rect.y + fractionY * div1,
                     rect.x + corner,
                     rect.y + corner);
        
        path.close();
        
        graphics.fillPath(path);

        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.setBackgroundColor(getLineColor());

        Point center = rect.getCenter();
        int width = Math.max(1, Math.round((rect.height - 2.0f * graphics.getLineWidth()) / 20.0f));
        
        if(width >= rect.width / 2) {
            width = Math.max(1, rect.width / 4);
        }
        
        graphics.fillPolygon(new int[] { center.x - Math.round(width), rect.y + 3 * width, center.x + Math.round(width),
                rect.y + 3 * width, center.x + Math.round(0.8f * width), rect.y + rect.height - 7 * width,
                center.x - Math.round(0.8f * width), rect.y + rect.height - 7 * width} );
        
        graphics.fillPolygon(new int[] { center.x + Math.round(0.8f * width), rect.y + rect.height - 5 * width,
                center.x - Math.round(0.8f * width), rect.y + rect.height - 5 * width, center.x - Math.round(0.8f * width),
                rect.y + rect.height - 3 * width, center.x + Math.round(0.8F * width), rect.y + rect.height - 3 * width });
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
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

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 12, 14);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 4, 4);
            }
            graphics.drawRoundRectangle(rect, 4, 4);
            
            Path path = new Path(null);

            path.moveTo(pt.x + 5.5f, pt.y + 2);
            path.lineTo(pt.x + 5.5f, pt.y + 9);
            
            path.moveTo(pt.x + 6.5f, pt.y + 2);
            path.lineTo(pt.x + 6.5f, pt.y + 9);
            
            path.moveTo(pt.x + 5.5f, pt.y + 10.5f);
            path.lineTo(pt.x + 5.5f, pt.y + 12.5f);
            
            path.moveTo(pt.x + 6.5f, pt.y + 10.5f);
            path.lineTo(pt.x + 6.5f, pt.y + 12.5f);
            
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
        return new Point(rect.x + rect.width - 19 - getLineWidth(), rect.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 23 : 0;
    }
}