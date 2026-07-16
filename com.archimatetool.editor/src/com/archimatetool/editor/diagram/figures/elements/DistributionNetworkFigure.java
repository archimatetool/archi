/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
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
 * DistributionNetwork Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class DistributionNetworkFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private static final double ARROW_ANGLE = Math.cos(Math.toRadians(60));

    private IFigureDelegate rectangleDelegate;
    
    public DistributionNetworkFigure() {
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
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds(), 5/3.0);
        
        // Calculate line width depending on size
        int lineWidth = (int)Math.max(3, Math.sqrt(getBounds().width * getBounds().height) / 24);

        // Shrink the arrow size area depending on line width
        Dimension arrowSize = getArrowSize(rect);
        rect.shrink(lineWidth, lineWidth);
        arrowSize = getArrowSize(rect);

        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Fill
        fillSection(graphics, rect, arrowSize);

        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.setLineWidth(lineWidth);
        
        drawArrows(graphics, rect, arrowSize);
        
        drawHorizontalLine(graphics, rect, arrowSize);
        
        graphics.popState();
    }
    
    protected void fillSection(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        Rectangle line1 = getLine1(rect, arrow);
        Rectangle line2 = getLine2(rect, arrow);
        
        path.moveTo(line1.x, line1.y);
        
        // Left to right horizontal
        path.lineTo(line1.width, line1.height);
        
        // Down/Right at angle
        path.lineTo(rect.x + rect.width, rect.y + rect.height / 2);
        
        // Down/Left at angle
        path.lineTo(line1.width, line2.y);
        
        // Right to left horizontal
        path.lineTo(line2.x, line2.y);
        
        // Up/Left at angle
        path.lineTo(rect.x, rect.y + rect.height / 2);
        
        path.close();
        
        graphics.fillPath(path);
        
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);
    }
    
    private Dimension getArrowSize(Rectangle rect) {
        int width = (int)(rect.width / (1 + ARROW_ANGLE) / 2);
        int size = Math.min(rect.height, width);
        return new Dimension((int)(size * ARROW_ANGLE), size);
    }

    private void drawArrows(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setLineCap(SWT.CAP_ROUND);
        
        Path path = new Path(null);
        path.moveTo(rect.x + arrow.width, rect.y + rect.height / 2 - arrow.height / 2);
        path.lineTo(rect.x, rect.y + rect.height / 2);
        path.lineTo(rect.x + arrow.width, rect.y + rect.height / 2 + arrow.height / 2);
        
        path.moveTo(rect.x + rect.width - arrow.width, rect.y + rect.height / 2 - arrow.height / 2);
        path.lineTo(rect.x + rect.width, rect.y + rect.height / 2);
        path.lineTo(rect.x + rect.width - arrow.width, rect.y + rect.height / 2 + arrow.height / 2);
        
        graphics.drawPath(path);
        path.dispose();
    }

    private Rectangle getLine1(Rectangle rect, Dimension arrow) {
        return new Rectangle(rect.x + arrow.height / 5,
                rect.y + rect.height / 2 - arrow.height / 5,
                rect.x + rect.width - arrow.height / 5,
                rect.y + rect.height / 2 - arrow.height / 5);
    }
    
    private Rectangle getLine2(Rectangle rect, Dimension arrow) {
        return new Rectangle(rect.x + arrow.height / 5,
                rect.y + rect.height / 2 + arrow.height / 5,
                rect.x + rect.width - arrow.height / 5,
                rect.y + rect.height / 2 + arrow.height / 5);
    }

    protected void drawHorizontalLine(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setLineCap(SWT.CAP_ROUND);
        
        Rectangle line1 = getLine1(rect, arrow);
        graphics.drawLine(line1.x, line1.y, line1.width, line1.height);
        
        Rectangle line2 = getLine2(rect, arrow);
        graphics.drawLine(line2.x, line2.y, line2.width, line2.height);
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
    protected void drawIcon(Graphics graphics) {
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

            if(backgroundColor != null) {
                Path path2 = new Path(null);

                path2.moveTo(pt.x + 1, pt.y - 2);
                path2.lineTo(pt.x + 14, pt.y - 2);
                path2.lineTo(pt.x + 16, pt.y + 1);
                path2.lineTo(pt.x + 14, pt.y + 2);
                path2.lineTo(pt.x + 1, pt.y + 2);
                path2.lineTo(pt.x, pt.y + 2);
                path2.lineTo(pt.x, pt.y - 2);

                graphics.fillPath(path2);
                path2.dispose();
            }

            Path path = buildPath(pt);
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            return FigureUtils.getAndDisposePathBounds(buildPath(new Point(0, 0)));
        }

        // The two lines + inward-pointing arrow carets at each end that are actually stroked (not the
        // fill-only path2 above, which is only ever built when a solid backgroundColor is supplied - never the
        // case when drawing the Outline shape style badge, whose icon is always stroke-only)
        private Path buildPath(Point pt) {
            Path path = new Path(null);

            path.moveTo(pt.x + 1, pt.y - 2);
            path.lineTo(pt.x + 14, pt.y - 2);

            path.moveTo(pt.x + 1, pt.y + 2);
            path.lineTo(pt.x + 14, pt.y + 2);

            path.moveTo(pt.x + 4, pt.y - 5);
            path.lineTo(pt.x - 1, pt.y);
            path.lineTo(pt.x + 4, pt.y + 5);

            path.moveTo(pt.x + 11, pt.y - 5);
            path.lineTo(pt.x + 16, pt.y);
            path.lineTo(pt.x + 11, pt.y + 5);

            return path;
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
        return new Point(rect.x + rect.width - 20, rect.y + 12);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 22) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
