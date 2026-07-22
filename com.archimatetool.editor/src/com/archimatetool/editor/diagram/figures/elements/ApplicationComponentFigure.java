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
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for an Application Component
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ApplicationComponentFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INDENT = 10;
    
    protected IFigureDelegate rectangleDelegate;
    
    public ApplicationComponentFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
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
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillRectangle(rect.x + INDENT, rect.y, rect.width - INDENT, rect.height);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        drawIconImage(graphics, getBounds().getCopy(), 0, 0, 0, INDENT * 2);

        // Outline
        int lineWidth = getLineWidth();
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        Path path = new Path(null);
        path.moveTo(rect.x + INDENT, rect.y + 10);
        path.lineTo(rect.x + INDENT, rect.y);
        path.lineTo(rect.x + rect.width, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x + INDENT, rect.y + rect.height);
        path.lineTo(rect.x + INDENT, rect.y + 43);
        
        path.moveTo(rect.x + INDENT, rect.y + 23);
        path.lineTo(rect.x + INDENT, rect.y + 30);
        
        FigureUtils.drawPath(graphics, path, lineWidth);
        path.dispose();
        
        // Nubs Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(rect.x, rect.y + 10, INDENT * 2, 13);
        graphics.fillRectangle(rect.x, rect.y + 30, INDENT * 2, 13);
        
        // Nubs Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawRectangle(graphics, new Rectangle(rect.x, rect.y + 10, INDENT * 2, 13), lineWidth);
        FigureUtils.drawRectangle(graphics, new Rectangle(rect.x, rect.y + 30, INDENT * 2, 13), lineWidth);
        
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
            
            Path path = new Path(null);
            
            // start bottom left
            path.moveTo(pt.x, pt.y);
            path.lineTo(pt.x, pt.y - 4);
            
            path.moveTo(pt.x, pt.y - 6);
            path.lineTo(pt.x, pt.y - 8);
            
            path.moveTo(pt.x, pt.y - 11);
            path.lineTo(pt.x, pt.y - 13);

            // line right
            path.lineTo(pt.x + 10, pt.y - 13);
            // line down
            path.lineTo(pt.x + 10, pt.y);
            // line left
            path.lineTo(pt.x - 0.5f, pt.y);
            
            path.addRectangle(pt.x - 3, pt.y - 11, 6, 2.5f);
            path.addRectangle(pt.x - 3, pt.y - 6, 6, 2.5f);

            if(backgroundColor != null) {
                graphics.fillRectangle(pt.x - 3, pt.y - 11, 6, 2);
                graphics.fillRectangle(pt.x - 3, pt.y - 6, 6, 2);
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
    protected Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 14, rect.y + 19);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        // Compensate for left hand nubs
        if(getFigureDelegate() == null) {
            Rectangle rect = getBounds().getCopy();
            rect.x += 18;
            rect.width -= 18;
            return rect;
        }
        
        return super.calculateTextControlBounds();
    }
}