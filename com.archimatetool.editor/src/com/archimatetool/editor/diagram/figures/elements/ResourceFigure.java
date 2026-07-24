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
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ResourceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public ResourceFigure() {
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
        
        // Apply the offset for the fill also so it lines up with the outline
        Rectangle rect = applyLineWidthOffset(graphics);
        
        Dimension nubSize = new Dimension(rect.width / 12, rect.height / 3);
        
        Path path = getFigurePath(rect, nubSize);

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
        
        Path pathLine = new Path(null);
        float x = rect.x + rect.width - nubSize.width;
        float y = rect.y + (rect.height - nubSize.height) / 2;
        pathLine.moveTo(x, y);
        pathLine.lineTo(x, y + nubSize.height);
        graphics.drawPath(pathLine);
        pathLine.dispose();
        
        int lineTop = rect.y + rect.height / 5;
        int lineBottom = rect.y + rect.height * 4 / 5;
        int lineGap = rect.width / 6;
        
        graphics.drawLine(rect.x + lineGap, lineTop, rect.x + lineGap, lineBottom);
        graphics.drawLine(rect.x + lineGap * 2, lineTop, rect.x + lineGap * 2, lineBottom);
        graphics.drawLine(rect.x + lineGap * 3, lineTop, rect.x + lineGap * 3, lineBottom);
        
        graphics.popState();
    }

    private Path getFigurePath(Rectangle rect, Dimension nubSize) {
        int arc1 = 5;
        int arc2 = 3;
        
        Path path = new Path(null);
        
        path.moveTo(rect.x + arc1, rect.y);
        path.lineTo(rect.x + rect.width - nubSize.width - arc1, rect.y);
        
        path.cubicTo(rect.x + rect.width - nubSize.width - arc1,
                     rect.y,
                     rect.x + rect.width - nubSize.width,
                     rect.y,
                     rect.x + rect.width - nubSize.width,
                     rect.y + arc1);
        
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + (rect.height - nubSize.height) / 2);
        path.lineTo(rect.x + rect.width - arc1, rect.y + (rect.height - nubSize.height) / 2);
        
        path.cubicTo(rect.x + rect.width - arc2,
                     rect.y + (rect.height - nubSize.height) / 2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + arc2);
        
        path.lineTo(rect.x + rect.width, rect.y + (rect.height - nubSize.height) / 2 + nubSize.height - arc1);
        
        path.cubicTo(rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height - arc2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height,
                     rect.x + rect.width - arc2,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height);
        
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + (rect.height - nubSize.height) / 2 + nubSize.height);
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + rect.height - arc1);
        
        path.cubicTo(rect.x + rect.width - nubSize.width,
                     rect.y + rect.height - arc1,
                     rect.x + rect.width - nubSize.width,
                     rect.y + rect.height,
                     rect.x + rect.width - nubSize.width - arc1,
                     rect.y + rect.height);
        
        path.lineTo(rect.x + arc1, rect.y + rect.height);

        path.cubicTo(rect.x + arc1,
                     rect.y + rect.height,
                     rect.x,
                     rect.y + rect.height,
                     rect.x,
                     rect.y + rect.height - arc1);

        path.lineTo(rect.x, rect.y + arc1);

        path.cubicTo(rect.x,
                     rect.y + arc1,
                     rect.x,
                     rect.y,
                     rect.x + arc1,
                     rect.y);
        
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
     * box's top-right corner flush with, and rounded to match, the top-right corner of the figure (its other
     * corners are square).
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
            
            // main rectangle
            Rectangle rect = new Rectangle(pt.x, pt.y, 15, 10);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 3, 3);
            }
            graphics.drawRoundRectangle(rect, 3, 3);
            
            // nub
            Point pt2 = pt.getCopy().translate(15, 3);
            rect = new Rectangle(pt2.x, pt2.y, 2, 4);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 3, 3);
            }
            graphics.drawRoundRectangle(rect, 1, 1);
            
            // lines
            Path path = new Path(null);
            
            path.moveTo(pt.x + 3f, pt.y + 2);
            path.lineTo(pt.x + 3f, pt.y + 8);
            
            path.moveTo(pt.x + 6f, pt.y + 2);
            path.lineTo(pt.x + 6f, pt.y + 8);
            
            path.moveTo(pt.x + 9f, pt.y + 2);
            path.lineTo(pt.x + 9f, pt.y + 8);
            
            graphics.drawPath(path);
            path.dispose();

            // Alternate method of drawing lines
//            pt.translate(-12, -1);
//            graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//            pt.translate(3, 0);
//            graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//            pt.translate(3, 0);
//            graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Both the main body and the nub are rounded rectangles - rounding cuts corners inward, so their
            // bounds equal their own defining rectangles exactly; the vertical lines drawn inside the main
            // rectangle don't extend beyond it, so they don't affect the union
            Rectangle main = new Rectangle(0, 0, 15, 10);
            Rectangle nub = new Rectangle(15, 3, 2, 4);
            return main.union(nub);
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
        return new Point(rect.getRight().x - 20, rect.y + 7);
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