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
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IIconic;




/**
 * Function Figure
 * 
 * @author Phillip Beauvoir
 */
public class FunctionFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;

    public FunctionFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    private final int OFFSET = 5;
    
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
        
        Path path = getFigurePath(rect);
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Image Icon
        int top = 0, right = 0, left = 0, bottom = 0;
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
            case IIconic.ICON_POSITION_TOP_RIGHT:
                top = rect.height / OFFSET;
                break;

            case IIconic.ICON_POSITION_BOTTOM_CENTRE:
                bottom = -(rect.height / OFFSET);
                break;

            case IIconic.ICON_POSITION_BOTTOM_LEFT:
            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                bottom = -(rect.height / OFFSET / 3);
                break;
        }
        drawIconImage(graphics, getBounds().getCopy(), top, right, bottom, left);

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.setLineWidth(getLineWidth());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        Path path = new Path(null);
        
        float y1 = rect.y + (rect.height / OFFSET);
        float y2 = rect.y + rect.height - (rect.height / OFFSET);
        
        path.moveTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x, y1);
        path.lineTo(rect.x + (rect.width / 2), rect.y);
        path.lineTo(rect.x + rect.width, y1);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x + (rect.width / 2), y2);
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

    // Bounding size of the icon glyph itself (a hexagon "gear-like" shape, see iconDelegate below)
    private static final int ICON_WIDTH = 12;
    private static final int ICON_HEIGHT = 14;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // Corner rounding for the containing box's top-right corner only, so it blends into the shape's own rounded corner
    private static final int ICON_BOX_CORNER_RADIUS = 8;

    // The icon delegate's "pt" origin is the bottom-left of the hexagon, not its top-left
    private static final int ICON_ORIGIN_OFFSET_Y = 14;

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
                    0, ICON_ORIGIN_OFFSET_Y);
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
            
            PointList points = new PointList();
            
            // Start at bottom left
            points.addPoint(pt);
            
            pt.translate(0, -9);
            points.addPoint(pt);
            
            pt.translate(6, -5);
            points.addPoint(pt);
            
            pt.translate(6, 5);
            points.addPoint(pt);
            
            pt.translate(0, 9);
            points.addPoint(pt);
            
            pt.translate(-6, -6);
            points.addPoint(pt);
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);
            
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
        return new Point(rect.x + rect.width - 16, rect.y + 19);
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
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}
