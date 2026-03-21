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
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Collaboration
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        setFigurePositionFromTextPosition(rect, 1.5); // Should match 'diameter'
        
        float diameter;
        float x1 = rect.x;

        // width < height or same
        if(rect.width <= rect.height) {
            diameter = (rect.width / 3f) * 2f;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, (rect.width / 3f) * 2f); // minimum of height or 2/3 of width
            x1 += (rect.width / 2f) - (diameter * .75);
        }
        
        float x2 = x1 + diameter / 2f;
        float y = rect.y + (rect.height - diameter) / 2f;
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        FigureUtils.fillOvalPath(graphics, x1, y, diameter, diameter);
        FigureUtils.fillOvalPath(graphics, x2, y, diameter, diameter);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Lines
        int lineWidth = getLineWidth();
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawOvalPath(graphics, x1, y, diameter, diameter, lineWidth);
        FigureUtils.drawOvalPath(graphics, x2, y, diameter, diameter, lineWidth);
        
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
        return new Point(rect.x + rect.width - 18, rect.y + 7);
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