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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;


/**
 * Gap Figure
 * 
 * @author Phillip Beauvoir
 */
public class GapFigure extends DeliverableFigure {
    
    public GapFigure() {
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
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
        
        setFigurePositionFromTextPosition(rect, 5/3.0); // Should match 'widthFraction' formula
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        int widthFraction = 3 * (rect.width / 10); // 3/10ths of width
        int circleRadius = widthFraction;
        
        // height < width
        if(rect.height < rect.width) {
            circleRadius = Math.min(rect.height / 2, widthFraction); // half height or 3/10ths
        }

        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        
        Path path = new Path(null);
        
        path.addArc(xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2, 0, 360);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();

        graphics.drawLine(xCenter - (circleRadius + circleRadius / 2),
                yCenter - circleRadius / 4,
                xCenter + (circleRadius + circleRadius / 2),
                yCenter - circleRadius / 4);
        
        graphics.drawLine(xCenter - (circleRadius + circleRadius / 2),
                yCenter + circleRadius / 4,
                xCenter + (circleRadius + circleRadius / 2),
                yCenter + circleRadius / 4);
        
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
        
        graphics.drawOval(pt.x, pt.y, 13, 13);
        
        pt.translate(-2, 5);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
        
        pt.translate(0, 3);
        graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 23 : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}
