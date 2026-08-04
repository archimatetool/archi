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
 * Figure for an Application Component
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ApplicationComponentFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INDENT = 10;
    
    protected IFigureDelegate rectangleDelegate;
    
    public ApplicationComponentFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        // Apply the offset for the fill also so it lines up with the outline
        Rectangle rect = applyLineWidthOffset(graphics);
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillRectangle(rect.x + INDENT, rect.y, rect.width - INDENT, rect.height);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        drawIconImage(graphics, getBounds().getCopy(), 0, 0, 0, INDENT * 2);

        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        Path path = new Path(null);
        path.moveTo(rect.x + INDENT, rect.y + 10);
        path.lineTo(rect.x + INDENT, rect.y);
        path.lineTo(rect.x + rect.width, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x + INDENT, rect.y + rect.height);
        path.lineTo(rect.x + INDENT, rect.y + 43);
        
        path.moveTo(rect.x + INDENT, rect.y + 23);
        path.lineTo(rect.x + INDENT, rect.y + 30);
        
        graphics.drawPath(path);
        path.dispose();
        
        // Nubs Fill - a plain paper fill in Outline shape style (matching the main body's flat fill), a subtly
        // darker shade of the fill in Classic shape style (a 3D-ish notch effect)
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(isOutlineShapeStyle() ? getFillColor() : ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(rect.x, rect.y + 10, INDENT * 2, 13);
        graphics.fillRectangle(rect.x, rect.y + 30, INDENT * 2, 13);
        
        // Nubs Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(rect.x, rect.y + 10, INDENT * 2, 13);
        graphics.drawRectangle(rect.x, rect.y + 30, INDENT * 2, 13);
        
        graphics.popState();
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

            graphics.setLineWidth(1);

            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }

            Path path = buildPath(pt);

            if(backgroundColor != null) {
                graphics.fillRectangle(pt.x - 3, pt.y - 11, 6, 2);
                graphics.fillRectangle(pt.x - 3, pt.y - 6, 6, 2);
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            return FigureUtils.getAndDisposePathBounds(buildPath(new Point(0, 0)));
        }

        // The box outline (three left-edge notch segments + top/right/bottom edges) plus the two nub rectangles
        // jutting out 3px to the left of pt.x
        private Path buildPath(Point pt) {
            Path path = new Path(null);

            // start bottom left
            path.moveTo(pt.x, pt.y);
            path.lineTo(pt.x, pt.y - 4);

            path.moveTo(pt.x, pt.y - 6);
            path.lineTo(pt.x, pt.y - 8);

            path.moveTo(pt.x, pt.y - 11);
            path.lineTo(pt.x, pt.y - 13);

            // line right
            path.lineTo(pt.x + 10, pt.y - 13);
            // line down
            path.lineTo(pt.x + 10, pt.y);
            // line left
            path.lineTo(pt.x - 0.5f, pt.y);

            path.addRectangle(pt.x - 3, pt.y - 11, 6, 2.5f);
            path.addRectangle(pt.x - 3, pt.y - 6, 6, 2.5f);

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
        return new Point(rect.x + rect.width - 14, rect.y + 19);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        // Compensate for left hand nubs
        if(getFigureDelegate() == null) {
            Rectangle rect = getBounds().getCopy();
            rect.x += 18;
            rect.width -= 18;
            return rect;
        }
        
        return super.calculateTextControlBounds();
    }
}