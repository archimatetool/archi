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

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Representation Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class RepresentationFigure extends DeliverableFigure {
    
    protected static final int TOP_MARGIN = 12;
    
    public RepresentationFigure() {
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
        
        Path path = getFigurePath(6, rect, 0);
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        drawIconImage(graphics, getBounds().getCopy());
        //drawIconImage(graphics, getBounds().getCopy(), TOP_MARGIN, 0, -TOP_MARGIN, 0);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Line
        graphics.drawLine(rect.x, rect.y + TOP_MARGIN, rect.x + rect.width, rect.y + TOP_MARGIN);
        
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        if(getFigureDelegate() != null) {
            return super.calculateTextControlBounds();
        }
        
        Rectangle rect = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += TOP_MARGIN - getTextControlMarginHeight() - 1;
        }
        
        return rect;
    }
    
    // Bounding size of the icon glyph itself (DeliverableFigure's "banner" shape plus an extra header line, see iconDelegate below)
    private static final int ICON_WIDTH = 14;
    private static final int ICON_HEIGHT = 12;

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
    @Override
    protected void drawIcon(Graphics graphics) {
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
            DeliverableFigure.getIconDelegate().drawIcon(graphics, foregroundColor, backgroundColor, pt);
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.pushState();
            
            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            graphics.drawLine(pt.x, pt.y + 3, pt.x + 14, pt.y + 3);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
