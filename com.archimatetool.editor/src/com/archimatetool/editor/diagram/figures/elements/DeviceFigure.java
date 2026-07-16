/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
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
 * Figure for a Device
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class DeviceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INDENT = 15;

    protected IFigureDelegate figureDelegate;
    
    public DeviceFigure() {
        super(TEXT_FLOW_CONTROL);
        figureDelegate = new RectangleFigureDelegate(this);
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
        
        int height_indent = rect.height / 5;
        
        // Top part fill
        Rectangle topRect = new Rectangle(rect.x, rect.y, rect.width, rect.height - height_indent + 1);
        
        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());
        Pattern gradient2 = applyGradientPattern(graphics, topRect);
        graphics.fillRoundRectangle(topRect, 30, 30);
        disposeGradientPattern(graphics, gradient2);

        // Bottom part fill - a plain paper fill in Outline shape style (matching the top part's flat fill), a
        // subtly darker shade of the fill in Classic shape style (a 3D-ish "feet" effect)
        graphics.setBackgroundColor(isOutlineShapeStyle() ? getFillColor() : ColorFactory.getDarkerColor(getFillColor()));
        Pattern gradient1 = applyGradientPattern(graphics, rect);
        Path path = getBottomPath(rect, height_indent);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient1);
        
        // Image icon
        Rectangle imageArea = new Rectangle(rect.x + 3, rect.y + 3, rect.width - 6, rect.height - height_indent - 6);
        drawIconImage(graphics, getBounds().getCopy(), imageArea);

        // Top part line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.setLineWidth(getLineWidth());
        graphics.drawRoundRectangle(topRect, 30, 30);
        
        // Bottom part line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }
    
    private Path getBottomPath(Rectangle rect, int height_indent) {
        Path path = new Path(null);
        path.moveTo(rect.x + getLineWidth(), rect.y + rect.height);
        path.lineTo(rect.x + INDENT + 1, rect.y + rect.height - height_indent + 1);
        path.lineTo(rect.x + rect.width - INDENT, rect.y + rect.height - height_indent + 1);
        path.lineTo(rect.x + rect.width - getLineWidth(), rect.y + rect.height);
        path.close();
        return path;
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
            
            Rectangle rect = new Rectangle(pt.x, pt.y, 11, 8);
            if(backgroundColor != null) {
                graphics.fillRoundRectangle(rect, 3, 3);
            }
            graphics.drawRoundRectangle(rect, 3, 3);
            
            int[] points = new int[] {
                    pt.x - 1, pt.y + 12,
                    pt.x + 2, pt.y + 8,
                    pt.x + 9, pt.y + 8,
                    pt.x + 12, pt.y + 12
            };
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Screen: a full rounded rectangle (not a partial arc), so its defining rectangle is already its
            // exact bounds. Stand: a four-point polygon jutting out below, and slightly left/right of, the screen
            Rectangle screen = new Rectangle(0, 0, 11, 8);

            int[] points = new int[] {
                    -1, 12,
                     2, 8,
                     9, 8,
                    12, 12
            };
            Rectangle stand = new PointList(points).getBounds();

            return screen.union(stand);
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
        return new Point(rect.x + rect.width - 16, rect.y + 5);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }
}