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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;


/**
 * Figure for a CourseOfAction
 * 
 * @author Phillip Beauvoir
 */
public class CourseOfActionFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;
    
    public CourseOfActionFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
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
        
        setFigurePositionFromTextPosition(rect, 1.24); // Should match '3.1 / 2.5' (values used in getRadius() and getCenter())
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int radius = getRadius(rect);
        Point center = getCenter(rect);
        
        path.addArc((float)center.preciseX() - radius, (float)center.preciseY() - radius, (radius * 2), (radius * 2), 0, 360);

        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());

        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.setBackgroundColor(getLineColor());

        int radius2 = Math.round(radius * 2f / 3f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius2, center.y - radius2, 2 * radius2, 2 * radius2);

        int radius3 = Math.round(radius / 3.0F - (graphics.getLineWidth() / 2));
        graphics.fillOval(center.x - radius3, center.y - radius3, 2 * radius3, 2 * radius3);
        
        int x = Math.round(center.x - radius * 2f);
        int y = Math.round(center.y + radius * 2f);
        
        Point intersection = getCircleIntersection(x, y, center.preciseX() - radius3, center.preciseY() - radius3,
                                                   center.preciseX() - radius3, center.preciseY() - radius3, radius);
        
        if(intersection != null) {
            graphics.setClip(rect); // Need this so line doesn't draw out of bounds at bottom of rect
            
            int arrowLength = (int)(radius3 * 1.5f);
            
            //int arrowLineWidth = Math.round(graphics.getLineWidth() * 1.5f);
            int arrowLineWidth = Math.max(3, arrowLength / 5);
            graphics.setLineWidth(arrowLineWidth);
            
            graphics.fillPolygon(new int[]{intersection.x, intersection.y, intersection.x - arrowLength, intersection.y - arrowLength / 3,
                    intersection.x - arrowLength / 3, intersection.y + arrowLength});
            
            graphics.setLineCap(SWT.CAP_ROUND);
            
            graphics.drawLine(x,
                              rect.y + rect.height,
                              x + (intersection.x - x) / 3,
                              intersection.y + (y - intersection.y) / 3);
            
            graphics.drawLine(x + (intersection.x - x) / 3,
                              intersection.y + (y - intersection.y) / 3,
                              intersection.x - arrowLineWidth,
                              intersection.y + arrowLineWidth);
        }

        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = Math.round(rect.height / 2.5f);
        int r2 = Math.round(rect.width / 3.1f);
        return Math.min(r1, r2);
    }
    
    private Point getCenter(Rectangle rect) {
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        int radius = getRadius(rect);
        int figureWidth = (int)(radius * 3.1f);
        int figureHeight = (int)(radius * 2.5f);
        return new PrecisionPoint(x + w - radius - (w - figureWidth) / 2, y + radius + (h - figureHeight) / 2);
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
            double tmpSqrt = Math.sqrt(disc);
            double abScalingFactor1 = -pBy2 + tmpSqrt;
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
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        // triangle of shortcut glyph
        Path path = new Path(null);
        Point pt = getIconOrigin();
        float x = pt.x - 5.4f, y = pt.y + 9f;

        path.moveTo(x, y);
        path.lineTo(x + 6f, y + 1f);
        path.lineTo(x + 3f, y + 6.2f);
        
        graphics.fillPath(path);
        path.dispose();
        
        // line of shortcut glyph
        path = new Path(null);
        graphics.setLineWidthFloat(2f);
        path.addArc(pt.x - 7.5f, pt.y + 12f, 10, 10, 90, 80);
        graphics.drawPath(path);
        path.dispose();
        
        // 2 circles and blob
        graphics.setLineWidthFloat(1.2f);
        path = new Path(null);
        pt = getIconOrigin();
        
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 17, bounds.y + 3);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 25 : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}