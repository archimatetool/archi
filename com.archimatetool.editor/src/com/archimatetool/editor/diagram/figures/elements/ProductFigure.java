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
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ITextPosition;




/**
 * Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProductFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int TOP_MARGIN = 12;
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate productDelegate;

    public ProductFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
        
        // Use a Rectangle Figure Delegate to draw this
        productDelegate = new RectangleFigureDelegate(this) {
            @Override
            public void drawFigure(Graphics graphics) {
                graphics.pushState();
                
                Rectangle rect = getBounds();
                
                // Reduce width and height by 1 pixel
                rect.resize(-1, -1);

                // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
                setLineWidth(graphics, rect);

                graphics.setAlpha(getAlpha());
                
                if(!isEnabled()) {
                    setDisabledState(graphics);
                }
                
                // Fill
                graphics.setBackgroundColor(getFillColor());
                
                Pattern gradient = applyGradientPattern(graphics, rect);

                graphics.fillRectangle(rect);
                
                disposeGradientPattern(graphics, gradient);
                
                // Outline
                graphics.setForegroundColor(getLineColor());
                graphics.setAlpha(getLineAlpha());

                graphics.drawRectangle(rect);
                
                Path path = new Path(null);
                path.moveTo(rect.x, rect.y + TOP_MARGIN);
                path.lineTo(rect.getCenter().x, rect.y + TOP_MARGIN);
                path.lineTo(rect.getCenter().x, rect.y);
                graphics.drawPath(path);
                path.dispose();
                
                // Icon
                // getOwner().drawIconImage(graphics, bounds);
                int topOffset = ((IIconic)getDiagramModelObject()).getImagePosition() == IIconic.ICON_POSITION_TOP_LEFT ? TOP_MARGIN : 0;
                drawIconImage(graphics, rect, topOffset, 0, 0, 0);

                graphics.popState();
            }

            @Override
            public Rectangle calculateTextControlBounds() {
                Rectangle rect = getBounds();
                
                int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
                
                if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                    rect.y += TOP_MARGIN - getTextControlMarginHeight() - 1;
                }
                
                return rect;
            }
        };
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
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 13, 10);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            graphics.drawRectangle(pt.x, pt.y, 6, 3);
            
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
        return new Point(rect.x + rect.width - 17 - getLineWidth(), rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : productDelegate;
    }
}
