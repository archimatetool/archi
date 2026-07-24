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
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Artifact Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ArtifactFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {

    private static final int FOLD_HEIGHT = 18;
    
    protected IFigureDelegate rectangleDelegate;

    public ArtifactFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
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
        
        Path mainPath = createFigurePath(rect);

        // Main fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(mainPath);
        disposeGradientPattern(graphics, gradient);

        // Fold fill - a plain paper fill in Outline shape style (matching the main body's flat fill), a subtly
        // darker shade of the fill in Classic shape style (a folded-page effect)
        graphics.setBackgroundColor(isOutlineShapeStyle() ? getFillColor() : ColorFactory.getDarkerColor(getFillColor()));
        Path foldPath = new Path(null);
        foldPath.moveTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        foldPath.lineTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        foldPath.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + FOLD_HEIGHT);
        graphics.fillPath(foldPath);
        foldPath.dispose();
        
        // Icon Image
        //int rightOffset = ((IIconic)getDiagramModelObject()).getImagePosition() == IIconic.ICON_POSITION_TOP_RIGHT ? -FOLD_HEIGHT : 0;
        //drawIconImage(graphics, getBounds().getCopy(), 0, rightOffset, 0, 0);
        drawIconImage(graphics, getBounds().getCopy());

        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        
        // Main outline
        graphics.drawPath(mainPath);
        mainPath.dispose();
        
        // Fold outline
        Path foldPath2 = new Path(null);
        foldPath2.moveTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        foldPath2.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + FOLD_HEIGHT);
        foldPath2.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        graphics.drawPath(foldPath2);
        foldPath2.dispose();
        
        graphics.popState();
    }
    
    private Path createFigurePath(Rectangle rect) {
        Path path = new Path(null);
        path.moveTo(rect.x, rect.y);
        path.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x, rect.y + rect.height);
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
            
            Path path = new Path(null);
            
            path.moveTo(pt.x, pt.y);
            path.lineTo(pt.x + 7, pt.y);
            path.lineTo(pt.x + 12, pt.y + 5);
            path.lineTo(pt.x + 12, pt.y + 15);
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            path.lineTo(pt.x, pt.y + 15);
            path.lineTo(pt.x, pt.y - 0.5f);
            
            path.moveTo(pt.x + 7, pt.y);
            path.lineTo(pt.x + 7, pt.y + 5);
            path.lineTo(pt.x + 12, pt.y + 5);
            
            if(backgroundColor != null) {
                graphics.fillPath(path);
            }
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the fully-built Path drawIcon() draws (with pt = (0, 0)) - the two fillPath() calls in
            // drawIcon() only fill intermediate/final states of this same accumulating path and add no extra
            // geometry, so the fully-built path here has identical extent to what's actually stroked
            Path path = new Path(null);

            path.moveTo(0, 0);
            path.lineTo(7, 0);
            path.lineTo(12, 5);
            path.lineTo(12, 15);
            path.lineTo(0, 15);
            path.lineTo(0, -0.5f);

            path.moveTo(7, 0);
            path.lineTo(7, 5);
            path.lineTo(12, 5);

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
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 16, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        // will also work for the right indent in the alternate figure
        return isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 22;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
