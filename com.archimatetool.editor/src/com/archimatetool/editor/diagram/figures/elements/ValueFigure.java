/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IIconic;




/**
 * Value Figure
 * 
 * @author Phillip Beauvoir
 */
public class ValueFigure extends AbstractMotivationFigure {

    public ValueFigure() {
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);

        graphics.fillOval(rect);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawOval(rect);
        
        // Icon
        // drawIconImage(graphics, bounds);
        
        int top = 0, right = 0, left = 0, bottom = 0;
        int offset = 6;
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
                top = rect.height / offset;
                left = rect.width / offset;
                break;

            case IIconic.ICON_POSITION_TOP_RIGHT:
                top = rect.height / offset;
                right = -(rect.width / offset);
                break;

            case IIconic.ICON_POSITION_BOTTOM_LEFT:
                bottom = -(rect.height / offset);
                left = rect.width / offset;
                break;

            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                bottom = -(rect.height / offset);
                right = -(rect.width / offset);
                break;
        }
        drawIconImage(graphics, rect, top, right, bottom, left);

        //Rectangle iconArea = new Rectangle(bounds.x + (bounds.width / 6), bounds.y + (bounds.height / 6),
        //        bounds.width - (bounds.width / 3), bounds.height - (bounds.height / 3));
        //drawIconImage(graphics, iconArea, 0, 0, 0, 0);

        graphics.popState();
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getDefaultConnectionAnchor() : new EllipseAnchor(this) ;
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
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 14, 9);
            if(backgroundColor != null) {
                graphics.fillOval(rect);
            }
            graphics.drawOval(rect);
            
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
        return new Point(rect.x + rect.width - 19 - getLineWidth(), rect.y + 7);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 21 : 0;
    }
}
