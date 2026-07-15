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
 * Figure for a Business Actor
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class BusinessActorFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public BusinessActorFigure() {
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
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds(), 2/3.0);
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int diameter = Math.min(rect.width / 2, rect.height / 3);
        int centerX = rect.x + rect.width / 2;
        int centerY = rect.y + (rect.height + getLineWidth()) / 2;
        
        path.addArc(centerX - diameter / 2,
                    centerY - diameter - diameter / 2,
                    diameter,
                    diameter,
                    0, 360);
        
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
        
        graphics.setLineCap(SWT.CAP_ROUND);
        graphics.setLineWidth(getLineWidth());
        
        graphics.drawLine(centerX,
                          centerY - diameter / 2,
                          centerX,
                          centerY - diameter / 2 + diameter);
        
        graphics.drawLine(centerX,
                          centerY - diameter / 2 + diameter,
                          centerX - diameter,
                          centerY - diameter / 2 + diameter * 2 - getLineWidth());
        
        graphics.drawLine(centerX,
                          centerY - diameter / 2 + diameter,
                          centerX + diameter,
                          centerY - diameter / 2 + diameter * 2 - getLineWidth());
        
        graphics.drawLine(centerX - diameter,
                          centerY - diameter / 4,
                          centerX + diameter,
                          centerY - diameter / 4);
        
        graphics.popState();
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

    // Bounding size of the icon glyph itself (a stick figure, see iconDelegate below)
    private static final int ICON_WIDTH = 8;
    private static final int ICON_HEIGHT = 17;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    // The figure's own outline is a plain (unrounded) rectangle, so the containing box's top-right corner is square too
    private static final int ICON_BOX_CORNER_RADIUS = 0;

    // The icon delegate's "pt" origin is not the top-left of its own bounding box (the head is inset 1px from the left)
    private static final int ICON_ORIGIN_OFFSET_X = 1;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with the top-right corner of the figure (all corners are square).
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
            
            // head
            if(backgroundColor != null) {
                graphics.fillOval(pt.x, pt.y, 6, 6);
            }
            graphics.drawOval(pt.x, pt.y, 6, 6);
            
            // body
            pt.translate(3, 6);
            graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
            
            // legs
            pt.translate(0, 6);
            graphics.drawLine(pt.x, pt.y, pt.x - 4, pt.y + 5);
            graphics.drawLine(pt.x, pt.y, pt.x + 4, pt.y + 5);
            
            // arms
            pt.translate(-4, -3);
            graphics.drawLine(pt.x, pt.y, pt.x + 8, pt.y);
            
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
        Rectangle rect = getBounds().getCopy();
        return new Point(rect.x + rect.width - 11, rect.y + 4);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 17) : 0;
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