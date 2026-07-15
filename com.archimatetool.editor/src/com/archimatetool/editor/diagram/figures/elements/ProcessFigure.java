/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangleAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Process Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProcessFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;

    public ProcessFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate1 = new RoundedRectangleFigureDelegate(this);
        fFigureDelegate2 = new ProcessFigureDelegate(this);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        if(getDiagramModelArchimateObject().getType() == 0 ) {
            drawIcon(graphics);
        }
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

    // Bounding size of the icon glyph itself (an arrow, see iconDelegate below)
    private static final int ICON_WIDTH = 14;
    private static final int ICON_HEIGHT = 10;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // Corner rounding for the containing box's top-right corner only, so it blends into the shape's own rounded corner
    private static final int ICON_BOX_CORNER_RADIUS = 8;

    // The icon delegate's "pt" origin is not the top-left of its own bounding box (the arrow's top notch dips 3px above pt)
    private static final int ICON_ORIGIN_OFFSET_Y = 3;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with, and rounded to match, the top-right corner of the figure (its other
     * corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    protected void drawIcon(Graphics graphics) {
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
            
            // Start at top left
            points.addPoint(pt);
            
            pt.translate(8, 0);
            points.addPoint(pt);
            
            pt.translate(0, -3);
            points.addPoint(pt);
            
            pt.translate(6, 5);
            points.addPoint(pt);
            
            pt.translate(-6, 5);
            points.addPoint(pt);
            
            pt.translate(0, -3);
            points.addPoint(pt);
            
            pt.translate(-8, 0);
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
    protected Point getClassicIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 18, rect.y + 10);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getFigureDelegate() instanceof RoundedRectangleFigureDelegate rf ? new RoundedRectangleAnchor(this, rf.getArc()) : new ChopboxAnchor(this);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 20) : 0;
    }
}
