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
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Gap Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class GapFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public GapFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        // Adjust size by line width
        int shrink = (int)Math.ceil(getLineWidth() / 2.0);
        rect.shrink(shrink, shrink);
        
        rect = getFigurePositionFromTextPosition(rect, 5/3.0); // Should match 'widthFraction' formula
        
        float widthFraction = 3f * (rect.width / 10f); // 3/10ths of width
        float circleRadius = widthFraction;
        
        // height < width
        if(rect.height < rect.width) {
            circleRadius = Math.min(rect.height / 2f, widthFraction); // half height or 3/10ths
        }

        float xCenter = rect.x + rect.width / 2f;
        float yCenter = rect.y + rect.height / 2f;
        //Rectangle circleRect = new Rectangle(xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2);
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        FigureUtils.fillOvalPath(graphics, xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        FigureUtils.drawOvalPath(graphics, xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2);

        int x1 = (int)(xCenter - (circleRadius + circleRadius / 2));
        int x2 = (int)(xCenter + (circleRadius + circleRadius / 2));
        int y1 = (int)(yCenter - circleRadius / 4);
        int y2 = (int)(yCenter + circleRadius / 4);
        
        graphics.drawLine(x1, y1, x2, y1);
        graphics.drawLine(x1, y2, x2, y2);
        
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

            graphics.setLineWidth(1);

            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }

            if(backgroundColor != null) {
                graphics.fillOval(pt.x, pt.y, 13, 13);
            }
            graphics.drawOval(pt.x, pt.y, 13, 13);

            pt.translate(-2, 5);
            graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);

            pt.translate(0, 3);
            graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the oval (a full 360 degree drawOval - its bounds are just its defining rectangle, no
            // Path needed) and the two drawLine() calls in drawIcon() above (pt = (0, 0)), unioned together
            Rectangle bounds = new Rectangle(0, 0, 13, 13);

            Point pt = new Point(0, 0);
            pt.translate(-2, 5);
            bounds = bounds.union(new Rectangle(pt.x, pt.y, 17, 0));

            pt.translate(0, 3);
            bounds = bounds.union(new Rectangle(pt.x, pt.y, 17, 0));

            return bounds;
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
        return new Point(rect.x + rect.width - 18, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 23) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}
