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
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Collaboration
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class CollaborationFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public CollaborationFigure() {
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

        rect = getFigurePositionFromTextPosition(rect, 1.5); // Should match 'diameter'
        
        float diameter;
        float x1 = rect.x;
        
        // width < height or same
        if(rect.width <= rect.height) {
            diameter = (rect.width / 3f) * 2f;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, (rect.width / 3f) * 2f); // minimum of height or 2/3 of width
            x1 += (rect.width / 2f) - (diameter * .75);
        }
        
        float x2 = x1 + diameter / 2f;
        float y = rect.y + (rect.height - diameter) / 2f;
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        FigureUtils.fillOvalPath(graphics, x1, y, diameter, diameter);
        FigureUtils.fillOvalPath(graphics, x2, y, diameter, diameter);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Lines
        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawOvalPath(graphics, x1, y, diameter, diameter);
        FigureUtils.drawOvalPath(graphics, x2, y, diameter, diameter);
        
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

    // Bounding size of the icon glyph itself (two overlapping circles, see iconDelegate below)
    private static final int ICON_WIDTH = 14;
    private static final int ICON_HEIGHT = 10;

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
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_WIDTH, ICON_HEIGHT, ICON_PADDING, ICON_BOX_CORNER_RADIUS);
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
            
            // circles
            Rectangle circle1 = new Rectangle(pt.x, pt.y, 10, 10);
            
            if(backgroundColor != null) {
                graphics.fillOval(circle1);
            }
            
            Rectangle circle2 = new Rectangle(pt.x + 4, pt.y, 10, 10);

            if(backgroundColor != null) {
                graphics.fillOval(circle2);
            }
            
            graphics.drawOval(circle2);
            graphics.drawOval(circle1);
            
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
        return new Point(rect.x + rect.width - 18, rect.y + 7);
    }

    @Override
    public int getIconOffset() {
        return isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 20;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}