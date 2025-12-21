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
 * Meaning Figure
 * 
 * @author Phillip Beauvoir
 */
public class MeaningFigure extends AbstractMotivationFigure {
    
    public MeaningFigure() {
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        // The following is a not so awful code to draw a cloud...
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Meaning icon is drawn inside a 12x7 grid using bezier curves
        // (online tool to help define curves: https://www.victoriakirst.com/beziertool/)
        float gridUnitX = (float)(rect.width / 12.0);
        float gridUnitY = (float)(rect.height / 7.0);
        
        // Define Path
        Path path = new Path(null);
        // Big bubble
        path.moveTo( rect.x +    gridUnitX, rect.y + 2*gridUnitY);
        path.cubicTo(rect.x               , rect.y              , rect.x +  2*gridUnitX, rect.y              , rect.x +  3*gridUnitX, rect.y +   gridUnitY);
        path.cubicTo(rect.x +  4*gridUnitX, rect.y              , rect.x +  6*gridUnitX, rect.y              , rect.x +  7*gridUnitX, rect.y +   gridUnitY);
        path.cubicTo(rect.x +  8*gridUnitX, rect.y              , rect.x + 10*gridUnitX, rect.y              , rect.x + 10*gridUnitX, rect.y +   gridUnitY);
        path.cubicTo(rect.x + 12*gridUnitX, rect.y              , rect.x + 12*gridUnitX, rect.y + 2*gridUnitY, rect.x + 11*gridUnitX, rect.y + 3*gridUnitY);
        path.cubicTo(rect.x + 12*gridUnitX, rect.y + 3*gridUnitY, rect.x + 12*gridUnitX, rect.y + 4*gridUnitY, rect.x + 11*gridUnitX, rect.y + 5*gridUnitY);
        path.cubicTo(rect.x + 11*gridUnitX, rect.y + 7*gridUnitY, rect.x +  8*gridUnitX, rect.y + 7*gridUnitY, rect.x +  7*gridUnitX, rect.y + 6*gridUnitY);
        path.cubicTo(rect.x +  6*gridUnitX, rect.y + 7*gridUnitY, rect.x +  2*gridUnitX, rect.y + 7*gridUnitY, rect.x +  2*gridUnitX, rect.y + 5*gridUnitY);
        path.cubicTo(rect.x               , rect.y + 5*gridUnitY, rect.x               , rect.y + 3*gridUnitY, rect.x +    gridUnitX, rect.y + 2*gridUnitY);
        // Small bubble
        path.moveTo(rect.x + 0.5f*gridUnitX, rect.y + 5.5f*gridUnitY);
        path.cubicTo(rect.x + gridUnitX, rect.y +    5*gridUnitY, rect.x + 2*gridUnitX, rect.y + 5.5f*gridUnitY, rect.x + 1.5f*gridUnitX, rect.y +    6*gridUnitY);
        path.cubicTo(rect.x + gridUnitX, rect.y	+ 6.5f*gridUnitY, rect.x              , rect.y +    6*gridUnitY, rect.x + 0.5f*gridUnitX, rect.y + 5.5f*gridUnitY);
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        // Delete Path and free memory
        path.dispose();
                
        // Icon
        drawIconImage(graphics, rect);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null,  getIconOrigin());
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
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 14, 11);
            
            if(backgroundColor != null) {
                Path path = new Path(null);
                path.addRectangle(pt.x + 0.5f, pt.y + 1, 9.5f, 6);
                graphics.fillPath(path);
                path.dispose();
            }
            
            Path path = new Path(null);
            path.addArc(rect.x, rect.y, rect.width/3 * 2, rect.height/3 * 2, 60, 149);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            path.addArc(rect.x + rect.width/3 - 1, rect.y, rect.width/3 * 2, rect.height/3 * 2, -38, 157);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            path.addArc(rect.x, rect.y + rect.height / 3, rect.width/5 * 3, rect.height/3 * 2 - 1, -41, -171);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            path.addArc(rect.x + rect.width/3, rect.y + rect.height/4, rect.width/5 * 3, rect.height/3 * 2, 7, -136);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
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
        Rectangle rect = getBounds().getCopy();
        return new Point(rect.x + rect.width - 17 - getLineWidth(), rect.y + 8);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 19 : 0;
    }
}
