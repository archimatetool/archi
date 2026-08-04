/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Driver
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds());

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int lineWidth = (int)(Math.sqrt(getBounds().width * getBounds().height) / 20);
        graphics.setLineWidth(lineWidth);
        
        int radius = getRadius(rect);
        int actualRadius = getRadius(rect) - Math.round(radius / 10.0f) - lineWidth / 2;
        Point center = rect.getCenter();
        
        path.addArc((float)center.preciseX() - actualRadius, (float)center.preciseY() - actualRadius, (actualRadius * 2), (actualRadius * 2), 0, 360);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
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
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = rect.height / 2;
        int r2 = rect.width / 2;
        int radius = Math.min(r1, r2);
        return radius - radius % 2;
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
            path.addArc(pt.x, pt.y, 13, 13, 0, 360);
            path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
            path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
            
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
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

        @Override
        public Rectangle getBounds() {
            // Mirrors the two Path blocks drawn above (with pt = (0, 0)): the nested full circles, then the
            // crosshair lines, so SWT computes the exact extent of each rather than solving for it by hand
            Path circlesPath = new Path(null);
            circlesPath.addArc(0, 0, 13, 13, 0, 360);
            circlesPath.addArc(5f, 5f, 3, 3, 0, 360);
            circlesPath.addArc(6f, 6f, 1f, 1f, 0, 360);
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(circlesPath);

            Path linesPath = new Path(null);
            linesPath.moveTo(-2, 6.5f);
            linesPath.lineTo(15, 6.5f);
            linesPath.moveTo(6.5f, -2);
            linesPath.lineTo(6.5f, 15);
            linesPath.moveTo(0.5f, 0.5f);
            linesPath.lineTo(12.5f, 12.5f);
            linesPath.moveTo(0.5f, 12.5f);
            linesPath.lineTo(12.5f, 0.5f);
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
        return new Point(rect.x + rect.width - 20, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 23) : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}