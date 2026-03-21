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

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Representation Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        Rectangle rect = getBounds().getCopy();
        
        Path path = getFigurePath(6, rect, 0);
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        drawIconImage(graphics, getBounds().getCopy());
        //drawIconImage(graphics, getBounds().getCopy(), TOP_MARGIN, 0, -TOP_MARGIN, 0);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawPath(graphics, path, getLineWidth());
        
        path.dispose();
        
        // Line
        graphics.setLineWidth(getLineWidth());
        graphics.drawLine(rect.x, rect.y + TOP_MARGIN, rect.x + rect.width, rect.y + TOP_MARGIN);
        
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return super.calculateTextControlBounds();
        }
        
        Rectangle rect = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += TOP_MARGIN - getTextControlMarginHeight() - 1;
        }
        
        return rect;
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
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

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
