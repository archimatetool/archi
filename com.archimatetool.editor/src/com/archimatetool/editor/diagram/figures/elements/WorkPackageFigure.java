/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangleAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class WorkPackageFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private RoundedRectangleFigureDelegate figureDelegate;

    public WorkPackageFigure() {
        super(TEXT_FLOW_CONTROL);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        figureDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getFigureDelegate() instanceof RoundedRectangleFigureDelegate rf ? new RoundedRectangleAnchor(this, rf.getArc()) : super.getDefaultConnectionAnchor();
    }

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getFigurePositionFromTextPosition(getBounds(), 8/7.0);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setBackgroundColor(getLineColor());
        
        /*
         * WorkPackage icon is drawn inside a 8x7 grid:
         *  -------------------------
         *  :  :  ://:\\:  :  :  :  :
         *  -------------------------
         *  :  ://:  :  :\\:  :  :  :
         *  -------------------------
         *  :(|:  :  :  :  :|):  :  :
         *  -------------------------
         *  :(|:  :  :  :  :|):  :  :
         *  -------------------------
         *  :  :\\:  :  :  :  :|\:  :
         *  -------------------------
         *  :  :  :\\:==:==:==:+=:=>:
         *  -------------------------
         *  :  :  :  :  :  :  :|/:  :
         *  -------------------------
         */
   
        float gridUnit = (float)(rect.width / 8.0);
        int lineWidth = (int) gridUnit;
        graphics.setLineWidth(lineWidth);
        // Use rounded ends when drawing
        graphics.setLineCap(SWT.CAP_ROUND);
        
        Path path = new Path(null);
        
        // Semi-circle is drawn inside a 6x6 square but because of line width radius is only 2,5
        float radius = (int) (2.5f * gridUnit);
        path.addArc(rect.x + gridUnit / 2,
                rect.y + gridUnit / 2,
                radius * 2,
                radius * 2,
                -25,
                295);	// this added to previous number must be equal to 270
        
        // Save current position
        float[] currentPoint = new float[2];
        path.getCurrentPoint(currentPoint);
        
        // Line
        path.lineTo(currentPoint[0] + 3 * gridUnit, currentPoint[1]);
        
        // Draw semi-circle and line segment
        graphics.drawPath(path);
        
        // Update current position before disposing the path
        path.getCurrentPoint(currentPoint);
        path.dispose();
        
        // Triangle
        path = new Path(null);
        path.moveTo(currentPoint[0], currentPoint[1]);
        path.lineTo(currentPoint[0], currentPoint[1] - 1.5f * gridUnit);
        path.lineTo(currentPoint[0] + 2 * gridUnit, currentPoint[1]);
        path.lineTo(currentPoint[0], currentPoint[1] + 1.5f * gridUnit);
        path.close();
        graphics.fillPath(path);
        path.dispose();

        graphics.popState();
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

            float circleWidth = 9;

            // If the circle part should be filled, set this to true
            boolean doFill = false;
            if(doFill && backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
                Path fillPath = new Path(null);
                fillPath.addArc(pt.x, pt.y, circleWidth, circleWidth, 0, 360);
                graphics.fillPath(fillPath);
                fillPath.dispose();
            }

            Path path = buildCircleAndLinePath(pt);
            graphics.drawPath(path);
            path.dispose();

            // Triangle
            path = buildTrianglePath(pt);

            if(foregroundColor != null) {
                graphics.setBackgroundColor(foregroundColor);
            }
            graphics.fillPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the two Path objects drawIcon() draws (with pt = (0, 0)): the partial (295 degree) circle
            // arc + line segment (one Path, stroked), and the filled triangle (a separate Path) - unioned
            // together. The circle's missing 65 degree gap sits between the cardinal extremes, so it still
            // traces the full 9x9 bounding oval - not a case of the gap actually reducing the traced extent.
            Rectangle bounds = FigureUtils.getAndDisposePathBounds(buildCircleAndLinePath(new Point(0, 0)));
            bounds = bounds.union(FigureUtils.getAndDisposePathBounds(buildTrianglePath(new Point(0, 0))));
            return bounds;
        }

        // Partial-arc "clock" circle + connecting line
        private Path buildCircleAndLinePath(Point pt) {
            float circleWidth = 9;
            float circleHalf = circleWidth / 2;

            Path path = new Path(null);

            // Circle
            path.addArc(pt.x, pt.y, circleWidth, circleWidth, 340, 295);

            // Line
            path.moveTo(pt.x + circleHalf, pt.y + circleWidth);
            path.lineTo(pt.x + 11, pt.y + circleWidth);

            return path;
        }

        // Filled arrowhead triangle at the end of the line
        private Path buildTrianglePath(Point pt) {
            float circleWidth = 9;

            Path path = new Path(null);

            path.moveTo(pt.x + 11, pt.y + circleWidth - 3);
            path.lineTo(pt.x + 15, pt.y + circleWidth);
            path.lineTo(pt.x + 11, pt.y + circleWidth + 3);

            path.close();

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
        return new Point(rect.x + rect.width - 17, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 19) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
}
