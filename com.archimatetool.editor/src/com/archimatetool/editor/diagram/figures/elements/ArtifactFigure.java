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
        
        Rectangle rect = getBounds().getCopy();
        
        Path mainPath = createFigurePath(rect);

        // Main fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(mainPath);
        disposeGradientPattern(graphics, gradient);

        // Fold fill
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
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
        int lineWidth = getLineWidth();
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        // Main outline
        FigureUtils.drawPath(graphics, mainPath, lineWidth);
        mainPath.dispose();
        
        // Fold outline
        graphics.setLineWidth(lineWidth);
        Path foldPath2 = new Path(null);
        foldPath2.moveTo(rect.x + rect.width - lineWidth, rect.y + FOLD_HEIGHT);
        foldPath2.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + FOLD_HEIGHT);
        foldPath2.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + lineWidth);
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
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
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
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 16, rect.y + 6);
    }

    @Override
    public int getIconOffset() {
        return 22; // will also work for the right indent in the alternate figure
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
