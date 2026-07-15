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
import com.archimatetool.editor.ui.ColorFactory;
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
    
    /**
     * In Outline shape style the fill always matches the view's background ("paper") color - only the outline is colored
     */
    @Override
    public Color getFillColor() {
        return isOutlineShapeStyle() ? ColorFactory.getViewBackgroundColor() : super.getFillColor();
    }

    /**
     * In Outline shape style the outline uses what would otherwise have been the fill color,
     * since the actual fill now matches the view's background
     */
    @Override
    public Color getLineColor() {
        return isOutlineShapeStyle() ? super.getFillColor() : super.getLineColor();
    }

    // Bounding size of the icon glyph itself (a "role" cylinder-with-flag glyph, see iconDelegate below)
    private static final int ICON_WIDTH = 15;
    private static final int ICON_HEIGHT = 8;

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
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_WIDTH, ICON_HEIGHT, ICON_PADDING, ICON_BOX_CORNER_RADIUS);
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
            
            path.addArc(pt.x, pt.y, 5, 8, 90, 180);
            
            path.lineTo(pt.x + 12, pt.y + 8);
            
            path.moveTo(pt.x + 2f, pt.y);
            path.lineTo(pt.x + 12, pt.y);
            
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
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_WIDTH + (ICON_PADDING * 2) : 20) : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : cylinderDelegate;
    }
}