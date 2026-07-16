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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a CourseOfAction
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        // Adjust size by line width
        int shrink = (int)Math.ceil(getLineWidth() / 2.0);
        rect.shrink(shrink, shrink);
        
        rect = getFigurePositionFromTextPosition(rect, 1.24); // Should match '3.1 / 2.5' (values used in getRadius() and getCenter())
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
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
        graphics.setLineWidth(getLineWidth());

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
            int arrowLength = (int)(radius3 * 1.5f);
            
            //int arrowLineWidth = Math.round(graphics.getLineWidth() * 1.5f);
            int arrowLineWidth = Math.max(3, arrowLength / 5);
            graphics.setLineWidth(arrowLineWidth);
            
            graphics.fillPolygon(new int[]{intersection.x, intersection.y, intersection.x - arrowLength, intersection.y - arrowLength / 3,
                    intersection.x - arrowLength / 3, intersection.y + arrowLength});
            
            Path linePath = new Path(null);
            linePath.moveTo(x, rect.y + rect.height - 1);
            linePath.lineTo(x + (intersection.x - x) / 3, intersection.y + (y - intersection.y) / 3);
            linePath.lineTo(intersection.x - arrowLineWidth, intersection.y + arrowLineWidth);
            graphics.drawPath(linePath);
            linePath.dispose();
        }

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

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // Corner rounding for the containing box's top-right corner only, so it blends into the shape's own rounded corner
    private static final int ICON_BOX_CORNER_RADIUS = 8;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with, and rounded to match, the top-right corner of the figure (its other
     * corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_PADDING, ICON_BOX_CORNER_RADIUS);
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
                graphics.setBackgroundColor(foregroundColor);
            }
            
            // triangle of shortcut glyph
            Path path = buildTrianglePath(pt);
            graphics.fillPath(path);
            path.dispose();

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }

            // line of shortcut glyph
            path = buildArcLinePath(pt);
            graphics.setLineWidthFloat(2f);
            graphics.drawPath(path);
            path.dispose();

            // 2 circles and blob - NOTE: fillPath() is deliberately called with only the outer circle's arc
            // added to the path so far (matching the original, pre-refactor behavior exactly for callers like
            // LegendGraphics that pass a non-null backgroundColor) - the other 3 arcs are added afterwards and
            // are only ever stroked, never filled. getBounds() below doesn't reuse this exact sequencing since
            // the final path's traced geometry (and therefore its bounds) is the same either way - only the
            // fill/stroke behavior differs, not the extent of what's stroked.
            graphics.setLineWidthFloat(1.2f);
            path = new Path(null);
            path.addArc(pt.x, pt.y, 13, 13, 0, 360);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
            path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
            path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Union of the three pieces drawIcon() draws above (with pt = (0, 0)): the triangle (straight
            // lines), the partial-arc "line" stroke, and the four full circles - each rebuilt via the same
            // path-building geometry drawIcon() itself uses, so bounds can't drift out of sync with the drawing
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(buildTrianglePath(new Point(0, 0)));
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(buildArcLinePath(new Point(0, 0))));
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(buildCirclesPath(new Point(0, 0))));
            return bounds;
        }

        // Triangle of the shortcut glyph - straight lines only, always filled regardless of backgroundColor
        private Path buildTrianglePath(Point pt) {
            Path path = new Path(null);
            float x = pt.x - 5.4f, y = pt.y + 9f;
            path.moveTo(x, y);
            path.lineTo(x + 6f, y + 1f);
            path.lineTo(x + 3f, y + 6.2f);
            return path;
        }

        // Partial (80 degree) arc "line" of the shortcut glyph - its traced extent is smaller than its 10x10
        // bounding oval, which is exactly why this needs a Path rather than a hand-derived rectangle
        private Path buildArcLinePath(Point pt) {
            Path path = new Path(null);
            path.addArc(pt.x - 7.5f, pt.y + 12f, 10, 10, 90, 80);
            return path;
        }

        // The 2 concentric circles + central blob - all full (360 degree) ovals, so a Path isn't strictly
        // required for these individually, but they're combined into one Path here (used by getBounds() only -
        // drawIcon() above builds this same geometry inline, since it needs to fill only the first arc partway
        // through construction, which this combined form can't express)
        private Path buildCirclesPath(Point pt) {
            Path path = new Path(null);
            path.addArc(pt.x, pt.y, 13, 13, 0, 360);
            path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
            path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
            path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
            return path;
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
        return new Point(rect.getRight().x - 17, rect.y + 4);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 25) : 0;
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