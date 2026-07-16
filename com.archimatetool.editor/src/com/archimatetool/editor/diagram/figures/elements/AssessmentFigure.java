/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for an Assessment
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds());
        
        int radius = getRadius(rect);
        Point center = getCenter(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getCirclePath(radius, center);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());

        // Lines
        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        double ratio = 2.5d;
        int x1 = (int)(center.x - radius * ratio);
        int y1 = (int)(center.y + radius * ratio);
        
        Point intersection = getCircleIntersection(x1, y1, center.preciseX(), center.preciseY(), center.preciseX(), center.preciseY(), radius);
        if(intersection != null) {
            graphics.drawLine((int)Math.round(intersection.preciseX()), (int)Math.round(intersection.preciseY() - 1), x1 + radius + 1, y1 - radius - 1);
        }
        
        graphics.popState();
    }
    
    private Path getCirclePath(int radius, Point center) {
        Path path = new Path(null);
        path.addArc((float)center.preciseX() - radius, (float)center.preciseY() - radius,
                    radius * 2, radius * 2,
                    0.0f, 360.0f);
        return path;
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = Math.round(rect.height / 2.5f);
        int r2 = Math.round(rect.width / 2.5f);
        return Math.min(r1, r2);
    }
    
    private Point getCenter(Rectangle rect) {
        int radius = getRadius(rect);
        int figureWidth = (int)(radius * 2.5f)  - getLineWidth() - 1;
        int figureHeight = (int)(radius * 2.5f) - getLineWidth() - 1;
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
    
    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner cut off at a diagonal matching the figure's own "shaved corner" outline.
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIconChamfered(graphics, this, getIconDelegate(), ICON_PADDING, INSET);
        }
        else if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getClassicIconOrigin());
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
            
            if(backgroundColor != null) {
                graphics.fillOval(pt.x, pt.y, 8, 8);
            }
            graphics.drawOval(pt.x, pt.y, 8, 8);
            graphics.drawLine(pt.x + 2, pt.y + 7, pt.x - 3, pt.y + 12);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // The magnifying glass circle is a full oval, so its bounds equal its own defining rectangle;
            // unioned with the diagonal handle line's own extent (with pt = (0, 0))
            Rectangle bounds = new Rectangle(0, 0, 8, 8);

            Path linePath = new Path(null);
            linePath.moveTo(2, 7);
            linePath.lineTo(-3, 12);
            return bounds.union(FigureUtils.getAndDisposePathBounds(linePath));
        }
    };

    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position for Classic shape style
     */
    private Point getClassicIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 15, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 21) : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}