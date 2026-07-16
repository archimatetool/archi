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
 * Figure for an Outcome
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class OutcomeFigure extends AbstractMotivationFigure {
    
    public OutcomeFigure() {
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
        
        // Adjust size by line width
        int shrink = (int)Math.ceil(getLineWidth() / 2.0);
        rect.shrink(shrink, shrink);

        // And then set the figure position
        rect = getFigurePositionFromTextPosition(rect);

        int radius = getRadius(rect);
        int diameter = radius * 2;
        Point center = getCenter(rect);
        
        // Circle
        float circleX = (float)center.preciseX() - radius;
        float circleY = (float)center.preciseY() - radius;
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        FigureUtils.fillOvalPath(graphics,circleX, circleY, diameter, diameter);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Circle Line
        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawOvalPath(graphics, circleX, circleY, diameter, diameter);
        
        graphics.setBackgroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        
        int radius2 = Math.round(radius * 2.0f / 3.0f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius2, center.y - radius2, 2 * radius2, 2 * radius2);

        int radius3 = Math.round(radius / 3.0f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius3, center.y - radius3, 2 * radius3, 2 * radius3);
        
        //int arrowLineWidth = Math.round(graphics.getLineWidth() * 1.2f);
        //graphics.setLineWidth(arrowLineWidth);
        
        int arrowLength = (int)(radius3 * 0.8f);
        int arrowLineWidth = Math.max(2, arrowLength / 6); // calculate this now but don't set it in graphics
        
        // Small adjustment for the arrow head
        Rectangle rectTemp = rect.getCopy();
        rectTemp.width--;
        Point centerTemp = getCenter(rectTemp);

        graphics.fillPolygon(new int[] { centerTemp.x - arrowLineWidth, centerTemp.y + arrowLineWidth,
                                         centerTemp.x + arrowLength, centerTemp.y, centerTemp.x, centerTemp.y - arrowLength });

        graphics.setLineWidth(arrowLineWidth); // Now set this
        
        double ratio = 1.2d;
        graphics.drawLine(center.x, center.y,
                         (int)(center.x + radius * ratio - 0.5d * arrowLength),
                         (int)(center.y - radius * ratio + 0.5d * arrowLength));
        
        graphics.drawLine((int)(center.x + ratio * (radius - arrowLength * 1.2d)),
                               (int)(center.y + ratio * (arrowLength * 1.2d - radius)),
                               (int)(center.x + radius * ratio),
                               (int)(center.y - radius * ratio) + arrowLength + arrowLineWidth);
        
        graphics.drawLine((int)(center.x + ratio * (radius - arrowLength * 1.2d)),
                          (int)(center.y + ratio * (arrowLength * 1.2d - radius)),
                          (int)(center.x + radius * ratio) - arrowLength - arrowLineWidth,
                          (int)(center.y - radius * ratio));
        
        graphics.popState();
    }
    
    private Point getCenter(Rectangle rect) {
        int radius = getRadius(rect);
        int figureWidth = (int)(radius * 2.2f);
        int figureHeight = (int)(radius * 2.2f);
        return new PrecisionPoint(rect.x + radius + (rect.width - figureWidth) / 2, rect.y + rect.height - radius - (rect.height - figureHeight) / 2);
    }

    private int getRadius(Rectangle rect) {
        int r1 = Math.round(rect.height / 2.2F);
        int r2 = Math.round(rect.width / 2.2F);
        return Math.min(r1, r2);
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
            
            Path path = new Path(null);

            graphics.setLineWidthFloat(1.2f);
            
            // circles
            path.addArc(pt.x, pt.y, 13, 13, 0, 360);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
            path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
            path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
            graphics.drawPath(path);
            path.dispose();
            
            graphics.setLineWidth(1);

            // lines
            path = new Path(null);
            
            path.moveTo(pt.x + 6.0f, pt.y + 7.0f);
            path.lineTo(pt.x + 15.5f, pt.y - 2.5f);
            
            path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
            path.lineTo(pt.x + 14.0f, pt.y - 5.0f);
            
            path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
            path.lineTo(pt.x + 18.0f, pt.y - 1.0f);
            
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Nested full circles - the outer circle's bounds equal its own defining rectangle and contain
            // the inner rings; unioned with the "achieved" arrow lines' own extent (with pt = (0, 0))
            Rectangle bounds = new Rectangle(0, 0, 13, 13);

            Path linesPath = new Path(null);
            linesPath.moveTo(6.0f, 7.0f);
            linesPath.lineTo(15.5f, -2.5f);
            linesPath.moveTo(13.0f, 0.0f);
            linesPath.lineTo(14.0f, -5.0f);
            linesPath.moveTo(13.0f, 0.0f);
            linesPath.lineTo(18.0f, -1.0f);
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(linesPath));

            return bounds;
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
        return new Point(rect.x + rect.width - 24, rect.y + 9);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 27) : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}