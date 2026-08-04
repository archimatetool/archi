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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Plateau Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class PlateauFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public PlateauFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds(), 5/3.5);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        int lineWidth = (int)Math.max(7, (Math.sqrt(rect.width * rect.height) / 16d));
        graphics.setLineWidth(lineWidth);
        
        int figureMaxSize = Math.min(rect.width, rect.height);
        int xMargin = (rect.width - figureMaxSize) / 2;
        int yMargin = (rect.height - figureMaxSize) / 2;
        
        graphics.drawLine(rect.x + xMargin,
                rect.y + yMargin + 3 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize - 2 * figureMaxSize / 6,
                rect.y + yMargin + 3 * figureMaxSize / 4);
        
        graphics.drawLine(rect.x + xMargin + 1 * figureMaxSize / 6,
                rect.y + yMargin + 2 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize - 1 * figureMaxSize / 6,
                rect.y + yMargin + 2 * figureMaxSize / 4);
        
        graphics.drawLine(rect.x + xMargin + 2 * figureMaxSize / 6,
                rect.y + yMargin + 1 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize,
                rect.y + yMargin + 1 * figureMaxSize / 4);
        
        graphics.popState();
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

            graphics.setLineWidth(2);

            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);

            pt.translate(2, -3);
            graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);

            pt.translate(2, -3);
            graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the three drawLine() calls in drawIcon() above (pt = (0, 0), the same left-to-right,
            // stepping-up sequence of pt.translate() calls) - three straight line segments, so their bounds are
            // just the union of their endpoints, no Path needed
            Point pt = new Point(0, 0);
            PointList points = new PointList();

            points.addPoint(pt.x, pt.y);
            points.addPoint(pt.x + 12, pt.y);

            pt.translate(2, -3);
            points.addPoint(pt.x, pt.y);
            points.addPoint(pt.x + 12, pt.y);

            pt.translate(2, -3);
            points.addPoint(pt.x, pt.y);
            points.addPoint(pt.x + 12, pt.y);

            return points.getBounds();
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
        return new Point(rect.x + rect.width - 20, rect.y + 13);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 17) : 0;
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
