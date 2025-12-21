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
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * System Software Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class SystemSoftwareFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {

    private IFigureDelegate rectangleDelegate;
    
    public SystemSoftwareFigure() {
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

        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        graphics.setForegroundColor(getLineColor());

        Pattern gradient = applyGradientPattern(graphics, rect);

        int diameter = (rect.width / 3) * 2;
        int x1 = rect.x + (rect.width - diameter) / 4;
        int x2 = x1 + diameter / 6;
        
        int y1 = rect.y + (rect.height - diameter) / 2;
        int y2 = y1 + diameter / 6;
        
        graphics.fillOval(x2, y1, diameter, diameter);
        
        graphics.setAlpha(getLineAlpha());
        graphics.drawOval(x2, y1, diameter, diameter);
        
        graphics.setAlpha(getAlpha());
        graphics.fillOval(x1, y2, diameter, diameter);
        
        graphics.setAlpha(getLineAlpha());
        graphics.drawOval(x1, y2, diameter, diameter);

        disposeGradientPattern(graphics, gradient);
        
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
            
            Path path = new Path(null);
            
            path.addArc(pt.x, pt.y, 11, 11, 90, 360);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            
            path.addArc(pt.x + 2, pt.y - 2, 11, 11, -60, 210);
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
        return new Point(rect.x + rect.width - 16 - getLineWidth(), rect.y + 8);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
