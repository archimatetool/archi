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
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Capability
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class CapabilityFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;
    
    public CapabilityFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds());
        
        // block length
        float blockLength = Math.min(rect.height / 3.0f, rect.width / 3.0f);
        float figureLength = blockLength * 3;
        
        float xMargin = (rect.width - figureLength) / 2.0f;
        float yMargin = (rect.height - figureLength) / 2.0f;
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        Path mainPath = createPath(rect, xMargin, yMargin, blockLength);
        graphics.fillPath(mainPath);
        mainPath.dispose();
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Outer lines
        graphics.setLineWidth(getLineWidth());
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        
        rect = applyLineWidthOffset(graphics, rect);
        
        // Recaclulate these on new rect
        blockLength = Math.min(rect.height / 3.0f, rect.width / 3.0f);
        figureLength = blockLength * 3;
        
        mainPath = createPath(rect, xMargin, yMargin, blockLength);
        graphics.drawPath(mainPath);
        mainPath.dispose();
        
        // Inner lines
        Path innerPath = new Path(null);
        
        innerPath.moveTo(rect.x + xMargin + blockLength, rect.y + yMargin + 3 * blockLength);
        innerPath.lineTo(rect.x + xMargin + blockLength, rect.y + yMargin + 2 * blockLength);
        
        innerPath.moveTo(rect.x + xMargin + blockLength * 2, rect.y + yMargin + 3 * blockLength);
        innerPath.lineTo(rect.x + xMargin + blockLength * 2, rect.y + yMargin + blockLength);
        
        innerPath.moveTo(rect.x + xMargin + blockLength * 1, rect.y + yMargin + 2 * blockLength);
        innerPath.lineTo(rect.x + xMargin + blockLength * 3, rect.y + yMargin + 2 * blockLength);
        
        innerPath.moveTo(rect.x + xMargin + blockLength * 2, rect.y + yMargin + blockLength);
        innerPath.lineTo(rect.x + xMargin + blockLength * 3, rect.y + yMargin + blockLength);
        
        graphics.drawPath(innerPath);
        innerPath.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect, float xMargin, float yMargin, float blockLength) {
        Path path = new Path(null);
        
        path.moveTo(rect.x + xMargin, rect.y + yMargin + 3 * blockLength);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + 2 * blockLength);
        path.lineTo(rect.x + xMargin + 1 * blockLength, rect.y + yMargin + 2 * blockLength);
        path.lineTo(rect.x + xMargin + 1 * blockLength, rect.y + yMargin + 1 * blockLength);
        path.lineTo(rect.x + xMargin + 2 * blockLength, rect.y + yMargin + 1 * blockLength);
        path.lineTo(rect.x + xMargin + 2 * blockLength, rect.y + yMargin);
        path.lineTo(rect.x + xMargin + 3 * blockLength, rect.y + yMargin);
        path.lineTo(rect.x + xMargin + 3 * blockLength, rect.y + yMargin + 3 * blockLength);
        path.close();
        
        return path;
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

    // Size of the icon glyph itself (3 x 4px blocks, see iconDelegate below)
    private static final int ICON_SIZE = 12;

    // Padding around the icon glyph inside its containing box, in Outline shape style
    private static final int ICON_PADDING = 3;

    private static final int ICON_BOX_SIZE = ICON_SIZE + (ICON_PADDING * 2);

    // Corner rounding for the containing box's top-right corner only, so it blends into the shape's own rounded corner
    private static final int ICON_BOX_CORNER_RADIUS = 6;

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as a white outline so the box color shows through, and the box's top-right corner flush
     * with, and rounded to match, the top-right corner of the figure (its other corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    private void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_SIZE, ICON_SIZE, ICON_PADDING, ICON_BOX_CORNER_RADIUS);
        }
        else if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getClassicIconOrigin());
        }
    }

    /**
     * @return The icon start position for Classic shape style
     */
    private Point getClassicIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.getRight().x - 17, rect.y + 5);
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
            
            Rectangle rect = new Rectangle(pt.x + 8, pt.y, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 4, pt.y + 4, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 8, pt.y + 4, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 4, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            rect = new Rectangle(pt.x + 8, pt.y + 8, 4, 4);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? (isOutlineShapeStyle() ? ICON_BOX_SIZE : 19) : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}