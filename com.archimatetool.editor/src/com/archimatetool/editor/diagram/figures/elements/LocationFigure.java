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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Location
 * 
 * @author Phillip Beauvoir
 */
public class LocationFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public LocationFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds());
        
        Path path = getFigurePath(rect);

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
        
        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        int figureWidth = 0;
        int figureHeight = 0;
        
        // width < height or same
        if(rect.width <= rect.height) {
            figureWidth = rect.width;
            figureHeight = rect.width;
        }
        // height < width
        else {
            figureHeight = rect.height;
            figureWidth = rect.height;
        }

        int yMargin = (rect.height - figureHeight + getLineWidth() + 1) / 2;
        int xCenter = rect.x + rect.width / 2;
        float diameter = (figureWidth  / 4) * 3;

        Path path = new Path(null);
        
        path.addArc(xCenter - diameter / 2, rect.y + yMargin, diameter, diameter, -35, 250);
        path.lineTo(xCenter, rect.y + rect.height - yMargin);
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

    // Bounding size of the icon glyph itself (a map pin: partial arc + point, see iconDelegate below)
    private static final int ICON_WIDTH = 10;
    private static final int ICON_HEIGHT = 15;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // The figure's own outline is a plain (unrounded) rectangle, so the containing box's top-right corner is square too
    private static final int ICON_BOX_CORNER_RADIUS = 0;

    // The icon delegate's "pt" origin is the pin's point (bottom), not the top-left of its bounding box
    private static final int ICON_ORIGIN_OFFSET_X = 5;
    private static final int ICON_ORIGIN_OFFSET_Y = 15;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with the top-right corner of the figure (all corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_WIDTH, ICON_HEIGHT, ICON_PADDING, ICON_BOX_CORNER_RADIUS,
                    ICON_ORIGIN_OFFSET_X, ICON_ORIGIN_OFFSET_Y);
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
            
            path.addArc(pt.x - 5, pt.y - 15, 10, 10, -20, 220);
            path.lineTo(pt.x, pt.y);
            path.close();
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
        return new Point(rect.x + rect.width - 9, rect.y + 20);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 16) : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}