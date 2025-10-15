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
            
            Rectangle bounds = getBounds();
            
            bounds.width--;
            bounds.height--;
            
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
            graphics.drawLine(bounds.x, bounds.getBottom().y - TOP_MARGIN, bounds.getRight().x, bounds.getBottom().y - TOP_MARGIN);
            graphics.drawRectangle(bounds);
            
            // Icon
            // getOwner().drawIconImage(graphics, bounds);
            getOwner().drawIconImage(graphics, bounds, TOP_MARGIN, 0, -TOP_MARGIN, 0);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            if(textPosition == ITextPosition.TEXT_POSITION_BOTTOM) {
                Rectangle bounds = getBounds();
                bounds.y -= TOP_MARGIN - getTextControlMarginHeight();
                return bounds;
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
