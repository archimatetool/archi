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
 * Figure for a Collaboration
 * 
 * @author Phillip Beauvoir
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
        
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect, 1.5); // Should match 'diameter'
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        int diameter;
        int x1 = rect.x, x2;

        // width < height or same
        if(rect.width <= rect.height) {
            diameter = (rect.width / 3) * 2;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, (rect.width / 3) * 2); // minimum of height or 2/3 of width
            x1 += (rect.width / 2) - (diameter * .75);
        }
        
        x2 = x1 + diameter / 2;
        int y = rect.y + (rect.height - diameter) / 2;
        
        graphics.fillOval(x1, y, diameter, diameter);
        graphics.fillOval(x2, y, diameter, diameter);
        
        disposeGradientPattern(graphics, gradient);
        
        // Line
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawOval(x1, y, diameter, diameter);
        graphics.drawOval(x2, y, diameter, diameter);
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
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
        
        // circles
        Rectangle circle = new Rectangle(pt.x, pt.y, 10, 10);
        graphics.drawOval(circle);
        circle.translate(4, 0);
        graphics.drawOval(circle);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 18, bounds.y + 7);
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