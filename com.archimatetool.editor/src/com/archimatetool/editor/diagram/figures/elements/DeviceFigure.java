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
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Device
 * 
 * @author Phillip Beauvoir
 */
public class DeviceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INDENT = 15;

    protected IFigureDelegate figureDelegate;
    
    public DeviceFigure() {
        super(TEXT_FLOW_CONTROL);
        figureDelegate = new RectangleFigureDelegate(this);
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
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        int height_indent = rect.height / 6;
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Bottom part
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));

        Path path = new Path(null);
        path.moveTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x + INDENT + 1, rect.y + rect.height - height_indent);
        path.lineTo(rect.x + rect.width - INDENT, rect.y + rect.height - height_indent);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x, rect.y + rect.height);
        
        Pattern gradient1 = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient1);
        
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        
        path.dispose();

        // Top part
        Rectangle topRect = new Rectangle(rect.x, rect.y, rect.width, rect.height - height_indent);

        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());

        Pattern gradient2 = applyGradientPattern(graphics, topRect);
        graphics.fillRoundRectangle(topRect, 30, 30);
        disposeGradientPattern(graphics, gradient2);

        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawRoundRectangle(topRect, 30, 30);
        
        // Image icon
        Rectangle imageArea = new Rectangle(rect.x + 3, rect.y + 3, rect.width - 6, rect.height - height_indent - 6);
        drawIconImage(graphics, rect, imageArea, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
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

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 11, 8);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 3, 3);
            }
            graphics.drawRoundRectangle(rect, 3, 3);
            
            int[] points = new int[] {
                    pt.x - 1, pt.y + 12,
                    pt.x + 2, pt.y + 8,
                    pt.x + 9, pt.y + 8,
                    pt.x + 12, pt.y + 12
            };
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 15 - getLineWidth(), rect.y + 5);
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
}