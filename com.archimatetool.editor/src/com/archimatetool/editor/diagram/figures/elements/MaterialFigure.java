/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
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
 * Material Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class MaterialFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;

    public MaterialFigure() {
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
        
        Rectangle rect = getBounds().getCopy();
        
        // Adjust size by line width
        int shrink = (int)Math.ceil(getLineWidth() / 2.0);
        rect.shrink(shrink, shrink);
        
        rect = getFigurePositionFromTextPosition(rect, 10/9.0); // Should match 'figureHeight'
        
        int figureWidth = rect.width;
        int figureHeight = rect.height;
        
        // width < height or same
        if(rect.width <= rect.height) {
            figureHeight = rect.width - (rect.width / 10);
        }
        // height < width
        else {
            figureWidth = rect.height + (rect.width / 10);
            figureWidth = Math.min(figureWidth, rect.width); // remove possible error in width calculation
        }
        
        int xMargin = (rect.width - figureWidth) / 2;
        int yMargin = (rect.height - figureHeight) / 2;
        
        Path path = createPath(rect, xMargin, yMargin, figureWidth, figureHeight);
        
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
        
        path = new Path(null);
        path.moveTo(rect.x + xMargin + 3 * figureWidth / 8, rect.y + yMargin + figureHeight / 10);
        path.lineTo(rect.x + xMargin + figureWidth / 6, rect.y + yMargin + figureHeight / 2);
        path.moveTo(rect.x + xMargin + figureWidth / 3, rect.y + yMargin + figureHeight - figureHeight / 7);
        path.lineTo(rect.x + xMargin + figureWidth - figureWidth / 3, rect.y + yMargin + figureHeight - figureHeight / 7);
        path.moveTo(rect.x + xMargin + figureWidth - 3 * figureWidth / 8, rect.y + yMargin + figureHeight / 10);
        path.lineTo(rect.x + xMargin + figureWidth - figureWidth / 6, rect.y + yMargin + figureHeight / 2);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect, int xMargin, int yMargin, int figureWidth, int figureHeight) {
        Path path = new Path(null);
        path.moveTo(rect.x + xMargin + figureWidth / 4, rect.y + yMargin);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + figureHeight / 2);
        path.lineTo(rect.x + xMargin + figureWidth / 4, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + 3 * figureWidth / 4, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight / 2);
        path.lineTo(rect.x + xMargin + 3 * figureWidth / 4, rect.y + yMargin);
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

            graphics.setLineWidthFloat(1.2f);

            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }

            int[] points = buildPolygonPoints(pt);

            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);

            Path path = buildLinesPath(pt);
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the hexagon polygon and the 3 diagonal lines Path in drawIcon() above (pt = (0, 0)),
            // unioned together (the lines fall entirely within the hexagon's own extent, but they're unioned
            // anyway rather than assumed, so this stays correct if either geometry changes)
            Rectangle bounds = new PointList(buildPolygonPoints(new Point(0, 0))).getBounds();
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(buildLinesPath(new Point(0, 0))));
            return bounds;
        }

        // A hexagon "package" outline
        private int[] buildPolygonPoints(Point pt) {
            return new int[] {
                    pt.x + 4, pt.y - 7,
                    pt.x - 4, pt.y - 7,

                    pt.x - 8, pt.y,
                    pt.x - 5, pt.y + 7,

                    pt.x + 4, pt.y + 7,
                    pt.x + 8, pt.y,
            };
        }

        // 3 diagonal lines inside the hexagon
        private Path buildLinesPath(Point pt) {
            Path path = new Path(null);

            path.moveTo(pt.x - 2, pt.y - 5);
            path.lineTo(pt.x - 5.3f, pt.y + 0.5f);

            path.moveTo(pt.x - 3.7f, pt.y + 4.5f);
            path.lineTo(pt.x + 3, pt.y + 4.5f);

            path.moveTo(pt.x + 5f, pt.y + 0.5f);
            path.lineTo(pt.x + 2f, pt.y - 5);

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
        return new Point(rect.x + rect.width - 12, rect.y + 12);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 22) : 0;
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
