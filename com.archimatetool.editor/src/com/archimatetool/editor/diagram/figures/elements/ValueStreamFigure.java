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
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IIconic;


/**
 * Figure for a Value Stream
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ValueStreamFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate fMainFigureDelegate;
    
    public ValueStreamFigure() {
        super(TEXT_FLOW_CONTROL);
        fMainFigureDelegate = new RoundedRectangleFigureDelegate(this);
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
        
        Path path = createPath(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        int top = 0, right = 0, left = 0, bottom = 0;
        int indent = Math.min(rect.height / 2, rect.width / 2);
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
            case IIconic.ICON_POSITION_BOTTOM_LEFT:
                left = 10;
                break;

            case IIconic.ICON_POSITION_TOP_RIGHT:
            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                right = -indent;
                break;

            case IIconic.ICON_POSITION_MIDDLE_LEFT:
                left = indent;
                break;

            case IIconic.ICON_POSITION_MIDDLE_RIGHT:
                right = -10;
                break;
        }
        drawIconImage(graphics, getBounds().getCopy(), top, right, bottom, left);

        // Outline
        graphics.setLineWidth(getLineWidth());
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        int indent = Math.min(rect.height / 2, rect.width / 2);
        int centre_y = rect.y + rect.height / 2;
        int point_startx = rect.x + rect.width - indent;
        float lineOffset = getLineWidth() / 2.0f;
        
        Path path = new Path(null);
        path.moveTo(rect.x + lineOffset, rect.y);
        path.lineTo(rect.x + indent, centre_y);
        path.lineTo(rect.x + lineOffset, rect.y + rect.height);
        path.lineTo(point_startx, rect.y + rect.height);
        path.lineTo(rect.x + rect.width, centre_y);
        path.lineTo(point_startx, rect.y);
        path.close();
        return path;
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
     * icon itself drawn as a white outline so the box color shows through, and the box's top-right corner flush
     * with, and rounded to match, the top-right corner of the figure (its other corners are square).
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

    /**
     * @return The icon start position for Classic shape style
     */
    private Point getClassicIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.getRight().x - 19, rect.y + 7);
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
            
            PointList points = new PointList();
            
            // Start at top left
            points.addPoint(pt);
            
            pt.translate(10, 0);
            points.addPoint(pt);
            
            pt.translate(5, 5);
            points.addPoint(pt);
            
            pt.translate(-5, 5);
            points.addPoint(pt);
            
            pt.translate(-10, 0);
            points.addPoint(pt);
            
            pt.translate(5, -5);
            points.addPoint(pt);
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the same translate sequence drawIcon() builds its PointList from above (with pt = (0, 0))
            // - a polygon has no curves, so PointList's own getBounds() gives the exact extent, no Path needed
            PointList points = new PointList();

            Point pt = new Point(0, 0);
            points.addPoint(pt);

            pt.translate(10, 0);
            points.addPoint(pt);

            pt.translate(5, 5);
            points.addPoint(pt);

            pt.translate(-5, 5);
            points.addPoint(pt);

            pt.translate(-10, 0);
            points.addPoint(pt);

            pt.translate(5, -5);
            points.addPoint(pt);

            return points.getBounds();
        }
    };

    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fMainFigureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 25) : 0;
    }
}