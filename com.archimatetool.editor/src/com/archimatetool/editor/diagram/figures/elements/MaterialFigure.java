/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;




/**
 * Material Figure
 * 
 * @author Phillip Beauvoir
 */
public class MaterialFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;

    public MaterialFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect, 10/9.0); // Should match 'figureHeight'
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int figureWidth = rect.width;
        int figureHeight = rect.height;
        
        // width < height or same
        if(rect.width <= rect.height) {
            figureHeight = rect.width - (rect.width / 10);
        }
        // height < width
        else {
            figureWidth = rect.height + (rect.width / 10);
            figureWidth = Math.min(figureWidth, rect.width); // remove possible error in width calculation
        }
        
        int xMargin = (rect.width - figureWidth) / 2;
        int yMargin = (rect.height - figureHeight) / 2;
        
        path.moveTo(rect.x + xMargin + figureWidth / 4, rect.y + yMargin);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + figureHeight / 2);
        path.lineTo(rect.x + xMargin + figureWidth / 4, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + 3 * figureWidth / 4, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight / 2);
        path.lineTo(rect.x + xMargin + 3 * figureWidth / 4, rect.y + yMargin);
        path.lineTo(rect.x + xMargin + figureWidth / 4, rect.y + yMargin);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        path = new Path(null);
        
        // Inner lines
        path.moveTo(rect.x + xMargin + 3 * figureWidth / 8, rect.y + yMargin + figureHeight / 10);
        path.lineTo(rect.x + xMargin + figureWidth / 6, rect.y + yMargin + figureHeight / 2);
        path.moveTo(rect.x + xMargin + figureWidth / 3, rect.y + yMargin + figureHeight - figureHeight / 7);
        path.lineTo(rect.x + xMargin + figureWidth - figureWidth / 3, rect.y + yMargin + figureHeight - figureHeight / 7);
        path.moveTo(rect.x + xMargin + figureWidth - 3 * figureWidth / 8, rect.y + yMargin + figureHeight / 10);
        path.lineTo(rect.x + xMargin + figureWidth - figureWidth / 6, rect.y + yMargin + figureHeight / 2);
        
        graphics.drawPath(path);
        
        path.dispose();
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        graphics.setLineWidthFloat(1.2f);
        Point pt = getIconOrigin();
        
        graphics.drawPolygon(new int[] {
                pt.x + 4, pt.y - 7,
                pt.x - 4, pt.y - 7,
                
                pt.x - 8, pt.y,
                pt.x - 5, pt.y + 7,
                
                pt.x + 4, pt.y + 7,
                pt.x + 8, pt.y,
        });

        
        Path path = new Path(null);

        path.moveTo(pt.x - 2, pt.y - 5);
        path.lineTo(pt.x - 5.3f, pt.y + 0.5f);
        
        path.moveTo(pt.x - 3.7f, pt.y + 4.5f);
        path.lineTo(pt.x + 3, pt.y + 4.5f);
        
        path.moveTo(pt.x + 5f, pt.y + 0.5f);
        path.lineTo(pt.x + 2f, pt.y - 5);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 12, bounds.y + 12);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
