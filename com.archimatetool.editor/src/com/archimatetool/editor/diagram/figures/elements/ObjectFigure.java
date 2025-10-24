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
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Object Figure
 * 
 * @author Phillip Beauvoir
 */
public class ObjectFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int TOP_MARGIN = 12;
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate objectDelegate;
    
    class ObjectFigureDelegate extends RectangleFigureDelegate {
        ObjectFigureDelegate(AbstractDiagramModelObjectFigure owner) {
            super(owner);
        }
        
        @Override
        public void drawFigure(Graphics graphics) {
            graphics.pushState();
            
            Rectangle bounds = getBounds();
            
            // Reduce width and height by 1 pixel
            bounds.resize(-1, -1);
            
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, bounds);

            graphics.setAlpha(getAlpha());
            
            if(!isEnabled()) {
                setDisabledState(graphics);
            }
            
            // Main Fill
            graphics.setBackgroundColor(getFillColor());
            
            Pattern gradient = applyGradientPattern(graphics, bounds);
            
            graphics.fillRectangle(bounds);
            
            disposeGradientPattern(graphics, gradient);

            // Outline
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());

            graphics.drawLine(bounds.x, bounds.y + TOP_MARGIN, bounds.x + bounds.width, bounds.y + TOP_MARGIN);
            graphics.drawRectangle(bounds);
            
            // Icon
            // getOwner().drawIconImage(graphics, bounds);
            getOwner().drawIconImage(graphics, bounds, TOP_MARGIN, 0, 0, 0);

            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            Rectangle bounds = getBounds();
            
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            
            if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                bounds.y += TOP_MARGIN - getTextControlMarginHeight();
            }
            
            return bounds;
        }
    }
    
    
    public ObjectFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
        objectDelegate = new ObjectFigureDelegate(this);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        int type = getDiagramModelArchimateObject().getType();
        if(type == 0) {
            drawIcon(graphics);
        }
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
            
            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.fillRectangle(pt.x, pt.y, 13, 10);
            }
            graphics.drawRectangle(pt.x, pt.y, 13, 10);
            graphics.drawLine(pt.x, pt.y + 3, pt.x + 13, pt.y + 3);
            
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
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : objectDelegate;
    }
}
