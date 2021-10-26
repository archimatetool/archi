/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;


/**
 * Figure for an Assessment
 * 
 * @author Phillip Beauvoir
 */
public class AssessmentFigure extends AbstractMotivationFigure {
    
    public AssessmentFigure() {
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
        
        setFigurePositionFromTextPosition(rect);

        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        int radius = getRadius(rect);
        Point center = getCenter(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        path.addArc((float)center.preciseX() - radius, (float)center.preciseY() - radius, (radius * 2), (radius * 2), 0.0F, 360.0F);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        double ratio = 2.5d;
        int x1 = (int)(center.x - radius * ratio);
        int y1 = (int)(center.y + radius * ratio);
        
        Point intersection = getCircleIntersection(x1, y1, center.preciseX(), center.preciseY(), center.preciseX(), center.preciseY(), radius);
        if(intersection != null) {
            graphics.setClip(rect); // Need this so line doesn't draw out of bounds at bottom of rect
            graphics.drawLine((int)Math.round(intersection.preciseX()), (int)Math.round(intersection.preciseY() - 1), x1, y1);
        }
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = Math.round(rect.height / 2.5f);
        int r2 = Math.round(rect.width / 2.5f);
        return Math.min(r1, r2);
    }
    
    private Point getCenter(Rectangle rect) {
        int radius = getRadius(rect);
        int figureWidth = (int)(radius * 2.5f);
        int figureHeight = (int)(radius * 2.5f);
        return new PrecisionPoint(rect.x + rect.width - radius - (rect.width - figureWidth) / 2,
                rect.y + radius + (rect.height - figureHeight) / 2);
    }
    
    private PrecisionPoint getCircleIntersection(double x1, double y1, double x2, double y2, double xCenter, double yCenter, double radius) {
        double baX = x2 - x1;
        double baY = y2 - y1;
        double caX = xCenter - x1;
        double caY = yCenter - y1;
        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;
        double pBy2 = bBy2 / a;
        double q = c / a;
        double disc = pBy2 * pBy2 - q;
        
        if(disc >= 0) {
            double sqrtDisc = Math.sqrt(disc);
            double abScalingFactor1 = -pBy2 + sqrtDisc;
            double xi1 = x1 - baX * abScalingFactor1;
            double yi1 = y1 - baY * abScalingFactor1;
            return new PrecisionPoint(xi1, yi1);
        }
        
        return null;
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
        graphics.drawOval(pt.x, pt.y, 8, 8);
        graphics.drawLine(pt.x + 2, pt.y + 7, pt.x - 3, pt.y + 12);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 15, bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 21 : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}