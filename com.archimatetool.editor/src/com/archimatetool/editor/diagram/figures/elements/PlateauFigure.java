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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;


/**
 * Plateau Figure
 * 
 * @author Phillip Beauvoir
 */
public class PlateauFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate boxDelegate;
    
    public PlateauFigure() {
        super(TEXT_FLOW_CONTROL);
        boxDelegate = new BoxFigureDelegate(this);
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
        
        Rectangle imageBounds = rect.getCopy();
        
        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        int lineWidth = (int)Math.max(7, (Math.sqrt(rect.width * rect.height) / 16d));
        graphics.setLineWidth(lineWidth);
        
        int figureMaxSize = Math.min(rect.width, rect.height);
        int xMargin = (rect.width - figureMaxSize) / 2;
        int yMargin = (rect.height - figureMaxSize) / 2;
        
        graphics.drawLine(rect.x + xMargin,
                rect.y + yMargin + 3 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize - 2 * figureMaxSize / 6,
                rect.y + yMargin + 3 * figureMaxSize / 4);
        
        graphics.drawLine(rect.x + xMargin + 1 * figureMaxSize / 6,
                rect.y + yMargin + 2 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize - 1 * figureMaxSize / 6,
                rect.y + yMargin + 2 * figureMaxSize / 4);
        
        graphics.drawLine(rect.x + xMargin + 2 * figureMaxSize / 6,
                rect.y + yMargin + 1 * figureMaxSize / 4,
                rect.x + xMargin + figureMaxSize,
                rect.y + yMargin + 1 * figureMaxSize / 4);
        
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
        
        graphics.setLineWidth(2);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        pt.translate(2, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 12, pt.y);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 34, bounds.y + 29);
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
        return getDiagramModelArchimateObject().getType() == 0 ? boxDelegate : null;
    }
}
