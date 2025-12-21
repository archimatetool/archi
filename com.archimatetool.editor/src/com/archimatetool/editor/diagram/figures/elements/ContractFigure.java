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

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;

/**
 * Contract Figure
 * 
 * @author Phillip Beauvoir
 */
public class ContractFigure extends ObjectFigure {
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate contractDelegate;

    class ContractFigureDelegate extends ObjectFigureDelegate {
        ContractFigureDelegate(AbstractDiagramModelObjectFigure owner) {
            super(owner);
        }
        
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
            
            // Main Fill
            graphics.setBackgroundColor(getFillColor());
            
            Pattern gradient = applyGradientPattern(graphics, rect);
            
            graphics.fillRectangle(rect);
            
            disposeGradientPattern(graphics, gradient);

            // Outline
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());

            graphics.drawLine(rect.x, rect.y + TOP_MARGIN, rect.x + rect.width, rect.y + TOP_MARGIN);
            graphics.drawLine(rect.x, rect.getBottom().y - TOP_MARGIN, rect.getRight().x, rect.getBottom().y - TOP_MARGIN);
            graphics.drawRectangle(rect);
            
            // Icon
            // getOwner().drawIconImage(graphics, bounds);
            getOwner().drawIconImage(graphics, rect, TOP_MARGIN, 0, -TOP_MARGIN, 0);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            if(textPosition == ITextPosition.TEXT_POSITION_BOTTOM) {
                Rectangle rect = getBounds();
                rect.y -= TOP_MARGIN - getTextControlMarginHeight();
                return rect;
            }
            else {
                return super.calculateTextControlBounds();
            }
        }
    }

    public ContractFigure() {
        rectangleDelegate = new RectangleFigureDelegate(this);
        contractDelegate = new ContractFigureDelegate(this);
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
            graphics.drawLine(pt.x, pt.y + 3, pt.x + 13, pt.y + 3);
            graphics.drawLine(pt.x, pt.y + 7, pt.x + 13, pt.y + 7);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : contractDelegate;
    }
}
