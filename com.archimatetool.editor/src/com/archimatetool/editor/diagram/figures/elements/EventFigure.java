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
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Event Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class EventFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fMainFigureDelegate;

    public EventFigure() {
        super(TEXT_FLOW_CONTROL);
        fMainFigureDelegate = new RoundedRectangleFigureDelegate(this);
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
        
        Path path = createPath(rect);
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, getBounds().getCopy());

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        int indent = Math.min(rect.height / 3, rect.width / 3);
        int centre_y = rect.y + rect.height / 2 - 1;
        int arc_startx = rect.x + rect.width - indent;
        float lineOffset = getLineWidth() / 2.0f;

        Path path = new Path(null);
        path.moveTo(rect.x + lineOffset, rect.y);
        path.lineTo(rect.x + indent, centre_y);
        path.lineTo(rect.x + lineOffset, rect.y + rect.height);
        path.lineTo(arc_startx, rect.y + rect.height);
        path.addArc(arc_startx - indent, rect.y, indent * 2, rect.height, -90, 180);
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

            if(backgroundColor != null) {
                Path path = new Path(null);

                path.addArc(pt.x + 8, pt.y, 8, 9, 270, 180);
                path.addRectangle(pt.x, pt.y, 12, 9);
                path.addArc(pt.x - 4, pt.y, 8, 9, 270, 180);

                graphics.fillPath(path);
                path.dispose();
            }

            // arc
            Path path = new Path(null);
            path.moveTo(pt.x, pt.y);
            path.addArc(pt.x - 4, pt.y, 8, 9, 270, 180);
            graphics.drawPath(path);
            path.dispose();

            // arc 2
            path = new Path(null);
            path.addArc(pt.x + 8, pt.y, 8, 9, 270, 180);

            // lines
            path.moveTo(pt.x, pt.y);
            path.lineTo(pt.x + 12, pt.y);

            path.moveTo(pt.x, pt.y + 9);
            path.lineTo(pt.x + 12, pt.y + 9);

            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the two stroke-only Path draws above ("arc" and "arc 2", with pt = (0, 0)) - the fill-only
            // rectangle + arcs built only when backgroundColor != null is excluded, since that never happens when
            // drawing the Outline badge (same pattern as DistributionNetworkFigure)
            Path path1 = new Path(null);
            path1.moveTo(0, 0);
            path1.addArc(-4, 0, 8, 9, 270, 180);
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(path1);

            Path path2 = new Path(null);
            path2.addArc(8, 0, 8, 9, 270, 180);
            path2.moveTo(0, 0);
            path2.lineTo(12, 0);
            path2.moveTo(0, 9);
            path2.lineTo(12, 9);
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(path2));

            return bounds;
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
        return new Point(rect.x + rect.width - 20, rect.y + 6);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fMainFigureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 22) : 0;
    }
}
