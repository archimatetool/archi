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
 * Facility Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class FacilityFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate figureDelegate;
    
    public FacilityFigure() {
        super(TEXT_FLOW_CONTROL);
        figureDelegate = new RectangleFigureDelegate(this);
    }
    
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        // Apply the offset for the fill also so it lines up with the outline
        Rectangle rect = applyLineWidthOffset(graphics);
        
        // And then set the figure position
        rect = getFigurePositionFromTextPosition(rect);
        
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
        final float buildingHeightFactor = 2f;
        
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

        int xMargin = (rect.width - figureWidth) / 2;
        int yMargin = (rect.height - figureHeight) / 2;
        
        int xTooth = figureWidth / 4 + figureWidth / 20;
        int yTooth = figureHeight / 5;
        
        Path path = new Path(null);
        
        path.moveTo(rect.x + xMargin, rect.y + yMargin);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - 2 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - 2 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - 3 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - 3 * xTooth, rect.y + yMargin);
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

            int[] points = buildPoints(pt);

            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the polygon points in drawIcon() above (pt = (0, 0)) - a full polygon's bounds are just
            // its defining points, no Path needed
            return new PointList(buildPoints(new Point(0, 0))).getBounds();
        }

        // A crenellated "battlement" roofline
        private int[] buildPoints(Point pt) {
            return new int[] {
                    pt.x , pt.y,
                    pt.x + 15, pt.y,

                    pt.x + 15, pt.y - 6,
                    pt.x + 11, pt.y - 3,

                    pt.x + 11, pt.y - 6,
                    pt.x + 7, pt.y - 3,

                    pt.x + 7, pt.y - 6,
                    pt.x + 3, pt.y - 3,

                    pt.x + 3, pt.y - 12,
                    pt.x, pt.y - 12
            };
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
        return new Point(rect.getRight().x - 19, rect.y + 17);
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
}
