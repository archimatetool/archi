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


/**
 * Facility Figure
 * 
 * @author Phillip Beauvoir
 */
public class FacilityFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate boxDelegate;
    
    public FacilityFigure() {
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
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getFigurePath(rect);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        final float buildingHeightFactor = 2f;
        
        Path path = new Path(null);
        
        int figureWidth = 0;
        int figureHeight = 0;
        
        // width < height or same
        if(rect.width <= rect.height) {
            figureWidth = rect.width;
            figureHeight = rect.width;
        }
        // height < width
        else {
            figureHeight = rect.height;
            figureWidth = rect.height;
        }

        int xMargin = (rect.width - figureWidth) / 2;
        int yMargin = (rect.height - figureHeight) / 2;
        
        int xTooth = figureWidth / 4 + figureWidth / 20;
        int yTooth = figureHeight / 5;
        
        path.moveTo(rect.x + xMargin, rect.y + yMargin);
        path.lineTo(rect.x + xMargin, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight);
        path.lineTo(rect.x + xMargin + figureWidth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - 2 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - 2 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor);
        path.lineTo(rect.x + xMargin + figureWidth - 3 * xTooth, rect.y + yMargin + figureHeight / buildingHeightFactor + yTooth);
        path.lineTo(rect.x + xMargin + figureWidth - 3 * xTooth, rect.y + yMargin);
        path.close();

        return path;
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
                pt.x , pt.y,
                pt.x + 15, pt.y,
                
                pt.x + 15, pt.y - 6,
                pt.x + 11, pt.y - 3,
                
                pt.x + 11, pt.y - 6,
                pt.x + 7, pt.y - 3,
                
                pt.x + 7, pt.y - 6,
                pt.x + 3, pt.y - 3,
                
                pt.x + 3, pt.y - 12,
                pt.x, pt.y - 12
        });
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 34, bounds.y + 30);
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? boxDelegate : null;
    }
}
