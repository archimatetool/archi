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
 * System Software Service Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class SystemSoftwareFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {

    private IFigureDelegate rectangleDelegate;
    
    public SystemSoftwareFigure() {
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
        
        Rectangle rect =  getFigurePositionFromTextPosition(getBounds());
        
        int diameter = (rect.width / 3) * 2;
        int x1 = rect.x + (rect.width - diameter) / 4;
        int x2 = x1 + diameter / 6;
        
        int y1 = rect.y + (rect.height - diameter) / 2;
        int y2 = y1 + diameter / 6;
        
        graphics.setBackgroundColor(getFillColor());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.fillOval(x2, y1, diameter, diameter);
        
        // Draw
        graphics.setAlpha(getLineAlpha());
        graphics.drawOval(x2, y1, diameter, diameter);
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.fillOval(x1, y2, diameter, diameter);
        
        // Draw
        graphics.setAlpha(getLineAlpha());
        graphics.drawOval(x1, y2, diameter, diameter);

        disposeGradientPattern(graphics, gradient);
        
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

            Path path = new Path(null);

            path.addArc(pt.x, pt.y, 11, 11, 90, 360);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }

            path.addArc(pt.x + 2, pt.y - 2, 11, 11, -60, 210);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the two addArc calls in drawIcon() above (with pt = (0, 0)). The first is a full circle
            // (arcAngle 360, so its start angle doesn't affect the traced extent); the second is a partial
            // (210 degree) arc, so its true extent is smaller than its 11x11 bounding oval - letting SWT trace
            // both into one Path computes their exact combined extent rather than assuming the full ovals
            Path path = new Path(null);

            path.addArc(0, 0, 11, 11, 90, 360);
            path.addArc(2, -2, 11, 11, -60, 210);

            return FigureUtils.getAndDisposePathBounds(path);
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
        return new Point(rect.x + rect.width - 17, rect.y + 7);
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
}
