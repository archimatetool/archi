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

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Stakeholder
 * 
 * @author Phillip Beauvoir
 */
public class StakeholderFigure extends AbstractMotivationFigure {
    
    private IFigureDelegate cylinderDelegate;
    
    public StakeholderFigure() {
        cylinderDelegate = new CylinderFigureDelegate(this);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }
        
        super.drawFigure(graphics);
        drawIcon(graphics);
    }
    
    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner cut off at a diagonal matching the figure's own "shaved corner" outline.
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIconChamfered(graphics, this, getIconDelegate(), ICON_PADDING, INSET);
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
            
            path.addArc(pt.x, pt.y, 8, 7, 90, 180);
            if(backgroundColor != null) {
                graphics.fillPath(path);
                graphics.fillRectangle(pt.x + 3, pt.y, 6, 7);
            }
            
            path.lineTo(pt.x + 11, pt.y + 7);
            
            path.moveTo(pt.x + 3.5f, pt.y);
            path.lineTo(pt.x + 11, pt.y);
            
            graphics.drawPath(path);
            
            path.dispose();
            
            Rectangle rect = new Rectangle(pt.x + 8, pt.y, 7, 7);
            if(backgroundColor != null) {
                graphics.fillOval(rect);
            }
            graphics.drawOval(rect);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the half-body outline drawn above (with pt = (0, 0)) so SWT computes the exact traced
            // extent of the partial arc, unioned with the head circle's own (full oval) defining rectangle
            Path path = new Path(null);
            path.addArc(0, 0, 8, 7, 90, 180);
            path.lineTo(11, 7);
            path.moveTo(3.5f, 0);
            path.lineTo(11, 0);
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(path);
            return bounds.union(new Rectangle(8, 0, 7, 7));
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
        return new Point(rect.x + rect.width - 20, rect.y + 9);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 23) : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? null : cylinderDelegate;
    }
}