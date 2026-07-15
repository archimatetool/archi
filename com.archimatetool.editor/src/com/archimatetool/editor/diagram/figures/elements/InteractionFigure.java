/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangleAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Interaction Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class InteractionFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;

    public InteractionFigure() {
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

        // And then set figure position
        rect = getFigurePositionFromTextPosition(rect, 1 / 0.86); // Should match 'FRACTION' defined in getFigurePath()
        
        Path path = getFigurePath(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Line
        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);

        path.dispose();
        
        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        final float FRACTION = 0.86f;    // fraction to use for diameter in landscape bounds
        final float GAP = 1 - FRACTION;  // gap to use for diameter in landscape bounds
        
        float diameter;
        int x1 = rect.x;
        int x2;

        // width < height or same
        if(rect.width <= rect.height) {
            diameter = rect.width * FRACTION;
            x2 = rect.x + rect.width;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, rect.width * 0.85f); // minimum of height or 85% of width
            x1 += ((rect.width - diameter) / 2) - (diameter * GAP / 2);
            x2 = (int)(x1 + diameter + (diameter * GAP));
        }

        float y = rect.y + (rect.height - diameter) / 2f;
        
        Path path = new Path(null);
        
        path.addArc(x1, y, diameter, diameter, 90, 180);
        path.close();
        
        path.addArc(x2 - diameter, y, diameter, diameter, 270, 180);
        path.close();
        
        return path;
    }
    
    /**
     * In Outline shape style the fill always matches the view's background ("paper") color - only the outline is colored
     */
    @Override
    public Color getFillColor() {
        return isOutlineShapeStyle() ? ColorFactory.getViewBackgroundColor() : super.getFillColor();
    }

    /**
     * In Outline shape style the outline uses what would otherwise have been the fill color,
     * since the actual fill now matches the view's background
     */
    @Override
    public Color getLineColor() {
        return isOutlineShapeStyle() ? super.getFillColor() : super.getLineColor();
    }

    // Bounding size of the icon glyph itself (two half-ellipses forming a circle-like shape, see iconDelegate below)
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // Corner rounding for the containing box's top-right corner only, so it blends into the shape's own rounded corner
    private static final int ICON_BOX_CORNER_RADIUS = 8;

    // The icon delegate's "pt" origin is not the top-left of its own bounding box (the left half-ellipse extends 5px further left)
    private static final int ICON_ORIGIN_OFFSET_X = 5;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with, and rounded to match, the top-right corner of the figure (its other
     * corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_WIDTH, ICON_HEIGHT, ICON_PADDING, ICON_BOX_CORNER_RADIUS,
                    ICON_ORIGIN_OFFSET_X, 0);
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
            
            // Start at top
            Path path = new Path(null);
            path.addArc(pt.x - 5, pt.y, 10, 12, 90, 180);
            path.lineTo(pt.x, pt.y - 0.5f);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            path = new Path(null);
            path.addArc(pt.x - 2, pt.y, 10, 12, -90, 180);
            path.lineTo(pt.x + 3, pt.y + 12.5f);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();
            
            graphics.popState();
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
        return new Point(rect.x + rect.width - 12, rect.y + 5);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getFigureDelegate() instanceof RoundedRectangleFigureDelegate rf ? new RoundedRectangleAnchor(this, rf.getArc()) : super.getDefaultConnectionAnchor();
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 20) : 0;
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
