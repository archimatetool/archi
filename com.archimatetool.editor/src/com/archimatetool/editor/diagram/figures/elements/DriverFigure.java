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
 * Figure for a Driver
 * 
 * @author Phillip Beauvoir
 */
public class DriverFigure extends AbstractMotivationFigure {
    
    private static final double BOTTOMLEFT_ANGLE_RADIAN = Math.toRadians(135.0d);
    private static final double BOTTOMRIGHT_ANGLE_RADIAN = Math.toRadians(45.0d);
    private static final double TOPLEFT_ANGLE_RADIAN = Math.toRadians(225.0d);
    private static final double TOPRIGHT_ANGLE_RADIAN = Math.toRadians(315.0d);
    
    public DriverFigure() {
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
        //rect.width--;
        //rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        //setLineWidth(graphics, 1, rect);
        int lineWidth = (int)(Math.sqrt(rect.width * rect.height) / 20);
        graphics.setLineWidth(lineWidth);
        
        setFigurePositionFromTextPosition(rect);

        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int radius = getRadius(rect);
        int actualRadius = getRadius(rect) - Math.round(radius / 10.0f) - lineWidth / 2;
        Point center = rect.getCenter();
        
        path.addArc((float)center.preciseX() - actualRadius, (float)center.preciseY() - actualRadius, (actualRadius * 2), (actualRadius * 2), 0, 360);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.setLineWidth(1);
        
        int topLeftX = (int)Math.round(center.x + radius * Math.cos(TOPLEFT_ANGLE_RADIAN));
        int topLeftY = (int)Math.round(center.y + radius * Math.sin(TOPLEFT_ANGLE_RADIAN));
        int bottomRightX = (int)Math.round(center.x + radius * Math.cos(BOTTOMRIGHT_ANGLE_RADIAN));
        int bottomRightY = (int)Math.round(center.y + radius * Math.sin(BOTTOMRIGHT_ANGLE_RADIAN));
        
        graphics.drawLine(topLeftX, topLeftY, bottomRightX, bottomRightY);
        graphics.drawLine(center.x - radius, center.y, center.x + radius, center.y);
        
        int bottomLeftX = (int)Math.round(center.x + radius * Math.cos(BOTTOMLEFT_ANGLE_RADIAN));
        int bottomLeftY = (int)Math.round(center.y + radius * Math.sin(BOTTOMLEFT_ANGLE_RADIAN));
        int topRightX = (int)Math.round(center.x + radius * Math.cos(TOPRIGHT_ANGLE_RADIAN));
        int topRightY = (int)Math.round(center.y + radius * Math.sin(TOPRIGHT_ANGLE_RADIAN));
        
        graphics.drawLine(bottomLeftX, bottomLeftY, topRightX, topRightY);
        graphics.drawLine(center.x, center.y - radius, center.x, center.y + radius);
        
        graphics.setBackgroundColor(getLineColor());
        
        radius = Math.round(radius / 4.0f);
        graphics.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = rect.height / 2;
        int r2 = rect.width / 2;
        int radius = Math.min(r1, r2);
        return radius - radius % 2;
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
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        graphics.setLineWidthFloat(1.2f);
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.setLineWidth(1);
        
        path = new Path(null);
        
        path.moveTo(pt.x - 2, pt.y + 6.5f);
        path.lineTo(pt.x + 15, pt.y + 6.5f);
        
        path.moveTo(pt.x + 6.5f, pt.y - 2);
        path.lineTo(pt.x + 6.5f, pt.y + 15);
       
        path.moveTo(pt.x + 0.5f, pt.y + 0.5f);
        path.lineTo(pt.x + 12.5f, pt.y + 12.5f);
        
        path.moveTo(pt.x + 0.5f, pt.y + 12.5f);
        path.lineTo(pt.x + 12.5f, pt.y + 0.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 21, bounds.y + 6);
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