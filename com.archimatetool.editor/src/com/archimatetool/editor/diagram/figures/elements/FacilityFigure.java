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
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Facility Figure
 * 
 * @author Phillip Beauvoir
 */
public class FacilityFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate figureDelegate;
    
    public FacilityFigure() {
        super(TEXT_FLOW_CONTROL);
        figureDelegate = new RectangleFigureDelegate(this);
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
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
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

            graphics.setLineWidthFloat(1.2f);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            int[] points = new int[] {
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
            };
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);
            
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
        return new Point(rect.getRight().x - 19 - getLineWidth(), rect.y + 17);
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
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
}
