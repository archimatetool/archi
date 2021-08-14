/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;


/**
 * Figure for an Interface
 * 
 * @author Phillip Beauvoir
 */
public class InterfaceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public InterfaceFigure() {
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
        
        rect.width--;
        rect.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        int diameter;
        int lineLength;
        int x = rect.x, y = rect.y;

        // width < height or same
        if(rect.width <= rect.height) {
            lineLength = rect.width / 3;
            diameter = rect.width - lineLength;
            x += rect.width - diameter;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, (rect.width / 3) * 2); // minimum of height or 2/3 of width
            lineLength = diameter / 2;
            x += (rect.width / 2) - (diameter / 4);
        }
        
        y += (rect.height / 2) - (diameter / 2);

        graphics.fillOval(x, y, diameter, diameter);
        
        disposeGradientPattern(graphics, gradient);
        
        // Line
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawLine(x - lineLength, rect.y + rect.height / 2, x, rect.y + rect.height / 2);
        graphics.drawOval(x, y, diameter, diameter);
        
        // Image Icon
        drawIconImage(graphics, rect, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();

        Rectangle rect = new Rectangle(pt.x, pt.y, 10, 10);
        
        // circle
        graphics.drawOval(rect);
        
        // line
        graphics.drawLine(pt.x, pt.y + 5, pt.x - 7, pt.y + 5);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 14, bounds.y + 8);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}