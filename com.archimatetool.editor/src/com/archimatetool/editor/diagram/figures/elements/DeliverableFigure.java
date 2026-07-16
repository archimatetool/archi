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
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Deliverable Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class DeliverableFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public DeliverableFigure() {
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

        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getFigurePath(8, rect, 0);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, getBounds().getCopy());
        //drawIconImage(graphics, getBounds().getCopy(), 0, 0, -14, 0);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
    
    protected static Path getFigurePath(float curveHeight, Rectangle rect, float inset) {
        float curveY = rect.bottom() - curveHeight;
        
        Path path = new Path(null);
        
        path.moveTo(rect.x + inset, rect.y + inset);
        
        path.lineTo(rect.x + inset, curveY - 1);
        
        path.quadTo(rect.x + (rect.width / 4), rect.bottom() + curveHeight - inset,
                    rect.x + (rect.width / 2) + 1, curveY);

        path.quadTo(rect.right() - (rect.width / 4), curveY - curveHeight - 1,
                    rect.right() - inset, curveY);
        
        path.lineTo(rect.x + rect.width - inset, rect.y + inset);
        
        path.close();
        
        return path;
    }
    
    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    // Padding around the icon glyph inside its containing box, in Outline shape style. Visible (package-protected
    // via subclass inheritance) so RepresentationFigure can reuse it instead of duplicating it
    protected static final int ICON_PADDING = 3;

    // The figure's own outline is a plain (unrounded) rectangle, so the containing box's top-right corner is
    // square too. Visible (package-protected via subclass inheritance) so RepresentationFigure can reuse it
    protected static final int ICON_BOX_CORNER_RADIUS = 0;

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

            Rectangle rect = new Rectangle(pt.x, pt.y, 14, 10);

            Path path = getFigurePath(1.5f, rect, 0.5f);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Reuses the exact same path-building method drawFigure()/drawIcon() draw with, so the badge is
            // always sized from the real geometry (including the two quadratic Bezier curves) rather than a
            // hand-estimated approximation
            return FigureUtils.getAndDisposePathBounds(getFigurePath(1.5f, new Rectangle(0, 0, 14, 10), 0.5f));
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
        return new Point(rect.x + rect.width - 18, rect.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 21) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
