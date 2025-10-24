/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Representation Figure
 * 
 * @author Phillip Beauvoir
 */
public class RepresentationFigure extends DeliverableFigure {
    
    protected static final int TOP_MARGIN = 12;
    
    public RepresentationFigure() {
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        bounds.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, bounds);

        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        Path path = getFigurePath(6, bounds, (float)getLineWidth() / 2);
        
        // Main Fill
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, bounds);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        path.dispose();
        
        // Line
        graphics.drawLine(bounds.x, bounds.y + TOP_MARGIN, bounds.x + bounds.width, bounds.y + TOP_MARGIN);
        
        // Icon
        // drawIconImage(graphics, bounds);
        drawIconImage(graphics, bounds, TOP_MARGIN, 0, -TOP_MARGIN, 0);

        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return super.calculateTextControlBounds();
        }
        
        Rectangle bounds = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += TOP_MARGIN - getTextControlMarginHeight() - 1;
        }
        
        return bounds;
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
            DeliverableFigure.getIconDelegate().drawIcon(graphics, foregroundColor, backgroundColor, pt);
            
            graphics.pushState();
            
            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            graphics.drawLine(pt.x, pt.y + 3, pt.x + 14, pt.y + 3);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
