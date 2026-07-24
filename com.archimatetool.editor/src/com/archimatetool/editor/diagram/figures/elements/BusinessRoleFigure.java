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
 * Figure for a Business Role
 * 
 * @author Phillip Beauvoir
 */
public class BusinessRoleFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate cylinderDelegate;
    
    public BusinessRoleFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
        cylinderDelegate = new CylinderFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        IFigureDelegate figureDelegate = getFigureDelegate();
        figureDelegate.drawFigure(graphics);
        if(figureDelegate == rectangleDelegate) {
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

            Path path = buildPath(pt);

            if(backgroundColor != null) {
                path.lineTo(pt.x + 12, pt.y + 8);
                graphics.fillPath(path);
            }

            graphics.drawPath(path);
            path.dispose();

            if(backgroundColor != null) {
                graphics.fillOval(pt.x + 10, pt.y, 5, 8);
            }
            graphics.drawOval(pt.x + 10, pt.y, 5, 8);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // buildPath() covers the flag pole partial arc + connecting lines - the stroke-only geometry that's
            // always drawn (matching backgroundColor == null, always the case for the Outline badge); the flag
            // itself is a full oval, so its bounds equal its own defining rectangle, no Path needed for that part
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(buildPath(new Point(0, 0)));
            bounds = bounds.union(new Rectangle(10, 0, 5, 8)); // flag oval
            return bounds;
        }

        // The partial arc + connecting lines that make up the pole - shared by drawIcon() (which strokes it,
        // and for a filled background also closes and fills it) and getBounds() (which only needs the
        // stroke-only extent, since the Outline badge always draws with a null background)
        private Path buildPath(Point pt) {
            Path path = new Path(null);
            path.addArc(pt.x, pt.y, 5, 8, 90, 180);
            path.lineTo(pt.x + 12, pt.y + 8);
            path.moveTo(pt.x + 2f, pt.y);
            path.lineTo(pt.x + 12, pt.y);
            return path;
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
        return new Point(rect.getRight().x - 18, rect.y + 7);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : cylinderDelegate;
    }
}