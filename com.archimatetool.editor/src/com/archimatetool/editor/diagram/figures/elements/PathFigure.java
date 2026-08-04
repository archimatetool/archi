/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Path Figure
 * 
 * @author Phillip Beauvoir
 */
public class PathFigure extends DistributionNetworkFigure {
    
    public PathFigure() {
    }
    
    @Override
    protected void drawHorizontalLine(Graphics graphics, Rectangle rect, Dimension arrow) {
        // Line dashes
        graphics.setLineStyle(SWT.LINE_CUSTOM);
        graphics.setLineDash(new float[] { graphics.getLineWidthFloat() * 2, graphics.getLineWidthFloat() });
        
        graphics.setLineCap(SWT.CAP_FLAT);
        
        graphics.drawLine(rect.x,
                rect.y + rect.height / 2,
                rect.x + rect.width,
                rect.y + rect.height / 2);
    }
    
    @Override
    protected void fillSection(Graphics graphics, Rectangle rect, Dimension arrowSize) {
        // No fill
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
    @Override
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

            graphics.setLineWidthFloat(1.5f);

            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            Path path = buildPath(pt);
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            return FigureUtils.getAndDisposePathBounds(buildPath(new Point(0, 0)));
        }

        // Three short dashes + inward-pointing arrow carets at each end
        private Path buildPath(Point pt) {
            Path path = new Path(null);

            path.moveTo(pt.x + 2.5f, pt.y);
            path.lineTo(pt.x + 4.5f, pt.y);

            path.moveTo(pt.x + 6.5f, pt.y);
            path.lineTo(pt.x + 8.5f, pt.y);

            path.moveTo(pt.x + 10.5f, pt.y);
            path.lineTo(pt.x + 12.5f, pt.y);

            path.moveTo(pt.x + 4, pt.y - 5);
            path.lineTo(pt.x - 1, pt.y);
            path.lineTo(pt.x + 4, pt.y + 5);

            path.moveTo(pt.x + 11, pt.y - 5);
            path.lineTo(pt.x + 16, pt.y);
            path.lineTo(pt.x + 11, pt.y + 5);

            return path;
        }
    };

    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position for Classic shape style
     */
    @Override
    protected Point getClassicIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 20, rect.y + 10);
    }

    // Path's own icon is a different glyph from the inherited DistributionNetworkFigure one (dashes instead of
    // solid lines), and getIconDelegate() is static so it doesn't participate in overriding - leaving
    // getIconOffset() inherited would reserve space from DistributionNetworkFigure's own icon delegate, not
    // this class's. Override explicitly so it uses its own.
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 22) : 0;
    }
}
