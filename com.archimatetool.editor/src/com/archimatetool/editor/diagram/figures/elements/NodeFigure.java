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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Node
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class NodeFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;
    
    public NodeFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate1 = new RectangleFigureDelegate(this);
        fFigureDelegate2 = new BoxFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        int type = getDiagramModelArchimateObject().getType();
        if(type == 0) {
            drawIcon(graphics);
        }
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

            Path path = new Path(null);

            path.addRectangle(pt.x, pt.y, 11, 11);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }

            path.moveTo(pt.x - 0.2f, pt.y);
            path.lineTo(pt.x + 3.2f, pt.y - 3);
            path.lineTo(pt.x + 14, pt.y - 3);
            path.lineTo(pt.x + 14f, pt.y + 8);
            path.lineTo(pt.x + 11f, pt.y + 11.2f);

            if(backgroundColor != null) {
                graphics.fillPath(path);
            }

            path.moveTo(pt.x + 11, pt.y);
            path.lineTo(pt.x + 14, pt.y - 3);

            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the addRectangle + line segments in drawIcon() above (with pt = (0, 0)) - the box plus the
            // top-right 3D edge notch jutting out above and to the right of it. Built independently rather than
            // shared with drawIcon() since that method fills the path twice mid-construction (once after the
            // rectangle, again after the notch lines) for its own rendering reasons unrelated to bounds
            Path path = new Path(null);

            path.addRectangle(0, 0, 11, 11);

            path.moveTo(-0.2f, 0);
            path.lineTo(3.2f, -3);
            path.lineTo(14, -3);
            path.lineTo(14f, 8);
            path.lineTo(11f, 11.2f);

            path.moveTo(11, 0);
            path.lineTo(14, -3);

            return FigureUtils.getAndDisposePathBounds(path);
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
        return new Point(rect.x + rect.width - 18, rect.y + 8);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }
}