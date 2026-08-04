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
 * Figure for a Principle
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class PrincipleFigure extends AbstractMotivationFigure {
    
    public PrincipleFigure() {
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
        
        Path path = createPath(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
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
        
        Point center = rect.getCenter();
        int width = Math.max(1, Math.round((rect.height - 2.0f * graphics.getLineWidth()) / 20.0f));
        
        if(width >= rect.width / 2) {
            width = Math.max(1, rect.width / 4);
        }
        
        graphics.setBackgroundColor(getLineColor());

        graphics.fillPolygon(new int[] { center.x - Math.round(width), rect.y + 3 * width, center.x + Math.round(width),
                rect.y + 3 * width, center.x + Math.round(0.8f * width), rect.y + rect.height - 7 * width,
                center.x - Math.round(0.8f * width), rect.y + rect.height - 7 * width} );
        
        graphics.fillPolygon(new int[] { center.x + Math.round(0.8f * width), rect.y + rect.height - 5 * width,
                center.x - Math.round(0.8f * width), rect.y + rect.height - 5 * width, center.x - Math.round(0.8f * width),
                rect.y + rect.height - 3 * width, center.x + Math.round(0.8F * width), rect.y + rect.height - 3 * width });
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        int divisions = 24;
        int div1 = 8;
        int div2 = 16;
        
        int fractionX = rect.width / divisions;
        int fractionY = rect.height / divisions;
        int corner = Math.min(fractionX, fractionY);
        
        Path path = new Path(null);
        
        path.moveTo(rect.x + corner, rect.y + corner);

        path.cubicTo(rect.x + fractionX * div1,
                     rect.y,
                     rect.x + fractionX * div2,
                     rect.y,
                     rect.x + rect.width - corner,
                     rect.y + corner);

        path.cubicTo(rect.x + rect.width,
                     rect.y + fractionY * div1,
                     rect.x + rect.width,
                     rect.y + fractionY * div2,
                     rect.x + rect.width - corner,
                     rect.y + rect.height - corner);

        path.cubicTo(rect.x + fractionX * div2,
                     rect.y + rect.height,
                     rect.x + fractionX * div1,
                     rect.y + rect.height,
                     rect.x + corner,
                     rect.y + rect.height - corner);

        path.cubicTo(rect.x,
                     rect.y + fractionY * div2,
                     rect.x,
                     rect.y + fractionY * div1,
                     rect.x + corner,
                     rect.y + corner);        
        
        path.close();
        
        return path;
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
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 12, 14);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 4, 4);
            }
            graphics.drawRoundRectangle(rect, 4, 4);
            
            Path path = new Path(null);

            path.moveTo(pt.x + 5.5f, pt.y + 2);
            path.lineTo(pt.x + 5.5f, pt.y + 9);
            
            path.moveTo(pt.x + 6.5f, pt.y + 2);
            path.lineTo(pt.x + 6.5f, pt.y + 9);
            
            path.moveTo(pt.x + 5.5f, pt.y + 10.5f);
            path.lineTo(pt.x + 5.5f, pt.y + 12.5f);
            
            path.moveTo(pt.x + 6.5f, pt.y + 10.5f);
            path.lineTo(pt.x + 6.5f, pt.y + 12.5f);
            
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // The rounded rectangle's rounding doesn't extend beyond its own defining rectangle; unioned with
            // the "!" mark lines' own extent (with pt = (0, 0)), which sit entirely inside it anyway
            Rectangle bounds = new Rectangle(0, 0, 12, 14);

            Path linesPath = new Path(null);
            linesPath.moveTo(5.5f, 2);
            linesPath.lineTo(5.5f, 9);
            linesPath.moveTo(6.5f, 2);
            linesPath.lineTo(6.5f, 9);
            linesPath.moveTo(5.5f, 10.5f);
            linesPath.lineTo(5.5f, 12.5f);
            linesPath.moveTo(6.5f, 10.5f);
            linesPath.lineTo(6.5f, 12.5f);
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
}