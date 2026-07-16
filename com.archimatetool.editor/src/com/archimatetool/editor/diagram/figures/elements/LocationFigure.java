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

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // The figure's own outline is a plain (unrounded) rectangle, so the containing box's top-right corner is square too
    private static final int ICON_BOX_CORNER_RADIUS = 0;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with the top-right corner of the figure (all corners are square).
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
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }

            Path path = buildPath(pt);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);

            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            return FigureUtils.getAndDisposePathBounds(buildPath(new Point(0, 0)));
        }

        // The map pin shape: a partial arc (the "head") + a line down to the point + close
        private Path buildPath(Point pt) {
            Path path = new Path(null);
            path.addArc(pt.x - 5, pt.y - 15, 10, 10, -20, 220);
            path.lineTo(pt.x, pt.y);
            path.close();
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
        return new Point(rect.x + rect.width - 9, rect.y + 20);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 16) : 0;
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