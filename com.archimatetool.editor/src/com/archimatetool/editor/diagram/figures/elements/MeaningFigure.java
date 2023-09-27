/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;




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
        
        Rectangle bounds = getBounds().getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, bounds);
        
        // The following is a not so awful code to draw a cloud...
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Meaning icon is drawn inside a 12x7 grid using bezier curves
        // (online tool to help define curves: https://www.victoriakirst.com/beziertool/)
        float gridUnitX = (float)(bounds.width / 12.0);
        float gridUnitY = (float)(bounds.height / 7.0);
        
        // Define Path
        Path path = new Path(null);
        // Big bubble
        path.moveTo( bounds.x +    gridUnitX, bounds.y + 2*gridUnitY);
        path.cubicTo(bounds.x               , bounds.y              , bounds.x +  2*gridUnitX, bounds.y              , bounds.x +  3*gridUnitX, bounds.y +   gridUnitY);
        path.cubicTo(bounds.x +  4*gridUnitX, bounds.y              , bounds.x +  6*gridUnitX, bounds.y              , bounds.x +  7*gridUnitX, bounds.y +   gridUnitY);
        path.cubicTo(bounds.x +  8*gridUnitX, bounds.y              , bounds.x + 10*gridUnitX, bounds.y              , bounds.x + 10*gridUnitX, bounds.y +   gridUnitY);
        path.cubicTo(bounds.x + 12*gridUnitX, bounds.y              , bounds.x + 12*gridUnitX, bounds.y + 2*gridUnitY, bounds.x + 11*gridUnitX, bounds.y + 3*gridUnitY);
        path.cubicTo(bounds.x + 12*gridUnitX, bounds.y + 3*gridUnitY, bounds.x + 12*gridUnitX, bounds.y + 4*gridUnitY, bounds.x + 11*gridUnitX, bounds.y + 5*gridUnitY);
        path.cubicTo(bounds.x + 11*gridUnitX, bounds.y + 7*gridUnitY, bounds.x +  8*gridUnitX, bounds.y + 7*gridUnitY, bounds.x +  7*gridUnitX, bounds.y + 6*gridUnitY);
        path.cubicTo(bounds.x +  6*gridUnitX, bounds.y + 7*gridUnitY, bounds.x +  2*gridUnitX, bounds.y + 7*gridUnitY, bounds.x +  2*gridUnitX, bounds.y + 5*gridUnitY);
        path.cubicTo(bounds.x               , bounds.y + 5*gridUnitY, bounds.x               , bounds.y + 3*gridUnitY, bounds.x +    gridUnitX, bounds.y + 2*gridUnitY);
        // Small bubble
        path.moveTo(bounds.x + 0.5f*gridUnitX, bounds.y + 5.5f*gridUnitY);
        path.cubicTo(bounds.x + gridUnitX, bounds.y +    5*gridUnitY, bounds.x + 2*gridUnitX, bounds.y + 5.5f*gridUnitY, bounds.x + 1.5f*gridUnitX, bounds.y +    6*gridUnitY);
        path.cubicTo(bounds.x + gridUnitX, bounds.y	+ 6.5f*gridUnitY, bounds.x              , bounds.y +    6*gridUnitY, bounds.x + 0.5f*gridUnitX, bounds.y + 5.5f*gridUnitY);
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, bounds);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        // Delete Path and free memory
        path.dispose();
                
        // Icon
        drawIconImage(graphics, bounds);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(getIconColor());
        
        Point pt = getIconOrigin();
        Rectangle rect = new Rectangle(pt.x, pt.y, 14, 11);
        
        Path path = new Path(null);
        path.addArc(rect.x, rect.y, rect.width/3 * 2, rect.height/3 * 2, 60, 149);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(rect.x + rect.width/3 - 1, rect.y, rect.width/3 * 2, rect.height/3 * 2, -38, 157);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(rect.x, rect.y + rect.height / 3, rect.width/5 * 3, rect.height/3 * 2 - 1, -41, -171);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(rect.x + rect.width/3, rect.y + rect.height/4, rect.width/5 * 3, rect.height/3 * 2, 7, -136);
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds().getCopy();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 8);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 19 : 0;
    }
}
