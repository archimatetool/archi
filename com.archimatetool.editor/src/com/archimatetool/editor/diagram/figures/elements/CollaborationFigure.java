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
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Collaboration
 * 
 * @author Phillip Beauvoir
 */
public class CollaborationFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public CollaborationFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
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
        
        setFigurePositionFromTextPosition(rect, 1.5); // Should match 'diameter'
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        int diameter;
        int x1 = rect.x, x2;

        // width < height or same
        if(rect.width <= rect.height) {
            diameter = (rect.width / 3) * 2;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, (rect.width / 3) * 2); // minimum of height or 2/3 of width
            x1 += (rect.width / 2) - (diameter * .75);
        }
        
        x2 = x1 + diameter / 2;
        int y = rect.y + (rect.height - diameter) / 2;
        
        graphics.fillOval(x1, y, diameter, diameter);
        graphics.fillOval(x2, y, diameter, diameter);
        
        disposeGradientPattern(graphics, gradient);
        
        // Line
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawOval(x1, y, diameter, diameter);
        graphics.drawOval(x2, y, diameter, diameter);
        
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
            
            // circles
            Rectangle circle1 = new Rectangle(pt.x, pt.y, 10, 10);
            
            if(backgroundColor != null) {
                graphics.fillOval(circle1);
            }
            
            Rectangle circle2 = new Rectangle(pt.x + 4, pt.y, 10, 10);

            if(backgroundColor != null) {
                graphics.fillOval(circle2);
            }
            
            graphics.drawOval(circle2);
            graphics.drawOval(circle1);
            
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
        return new Point(rect.x + rect.width - 17 - getLineWidth(), rect.y + 7);
    }

    @Override
    public int getIconOffset() {
        return 20;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}