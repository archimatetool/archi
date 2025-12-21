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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Capability
 * 
 * @author Phillip Beauvoir
 */
public class CapabilityFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;
    
    public CapabilityFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
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
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        // block length
        int blockLength = Math.min(rect.height / 3, rect.width / 3);
        int figureLength = blockLength * 3;
        
        int xMargin = (rect.width - figureLength) / 2;
        int yMargin = (rect.height - figureLength) / 2;
        
        path.moveTo(rect.x + xMargin, rect.y + yMargin + 3 * blockLength);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + 2 * blockLength);
        path.lineTo(rect.x + xMargin + 1 * blockLength, rect.y + yMargin + 2 * blockLength);
        path.lineTo(rect.x + xMargin + 1 * blockLength, rect.y + yMargin + 1 * blockLength);
        path.lineTo(rect.x + xMargin + 2 * blockLength, rect.y + yMargin + 1 * blockLength);
        path.lineTo(rect.x + xMargin + 2 * blockLength, rect.y + yMargin);
        path.lineTo(rect.x + xMargin + 3 * blockLength, rect.y + yMargin);
        path.lineTo(rect.x + xMargin + 3 * blockLength, rect.y + yMargin + 3 * blockLength);
        path.close();
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Inner lines
        
        graphics.drawLine(rect.x + xMargin + 1 * blockLength,
                rect.y + yMargin + 3 * blockLength,
                rect.x + xMargin + 1 * blockLength,
                rect.y + yMargin + 2 * blockLength);
        
        graphics.drawLine(rect.x + xMargin + 2 * blockLength,
                rect.y + yMargin + 3 * blockLength,
                rect.x + xMargin + 2 * blockLength,
                rect.y + yMargin + 1 * blockLength);
        
        graphics.drawLine(rect.x + xMargin + 1 * blockLength,
                rect.y + yMargin + 2 * blockLength,
                rect.x + xMargin + 3 * blockLength,
                rect.y + yMargin + 2 * blockLength);
        
        graphics.drawLine(rect.x + xMargin + 2 * blockLength,
                rect.y + yMargin + 1 * blockLength,
                rect.x + xMargin + 3 * blockLength,
                rect.y + yMargin + 1 * blockLength);

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
            
            Rectangle rect = new Rectangle(pt.x + 8, pt.y, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 4, pt.y + 4, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 8, pt.y + 4, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 4, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 8, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
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
        return new Point(rect.getRight().x - 16 - getLineWidth(), rect.y + 5);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 19 : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}