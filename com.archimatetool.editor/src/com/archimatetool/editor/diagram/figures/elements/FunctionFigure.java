/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IIconic;




/**
 * Function Figure
 * 
 * @author Phillip Beauvoir
 */
public class FunctionFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;

    public FunctionFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    private final int OFFSET = 5;
    
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
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getFigurePath(rect);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Image Icon
        //drawIconImage(graphics, bounds, 0, 0, 0, 0);
        //drawIconImage(graphics, bounds, bounds.height / 5 + 1, 0, -(bounds.height / 5 + 1), 0);
        
        int top = 0, right = 0, left = 0, bottom = 0;
        
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
            case IIconic.ICON_POSITION_TOP_RIGHT:
                top = rect.height / OFFSET;
                break;

            case IIconic.ICON_POSITION_BOTTOM_CENTRE:
                bottom = -(rect.height / OFFSET);
                break;

            case IIconic.ICON_POSITION_BOTTOM_LEFT:
            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                bottom = -(rect.height / OFFSET / 3);
                break;
        }
        
        drawIconImage(graphics, rect, top, right, bottom, left);

        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        Path path = new Path(null);
        
        float y1 = rect.y + (rect.height / OFFSET);
        float y2 = rect.y + rect.height - (rect.height / OFFSET);
        
        path.moveTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x, y1);
        path.lineTo(rect.x + (rect.width / 2), rect.y);
        path.lineTo(rect.x + rect.width, y1);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x + (rect.width / 2), y2);
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
            
            PointList points = new PointList();
            
            // Start at bottom left
            points.addPoint(pt);
            
            pt.translate(0, -9);
            points.addPoint(pt);
            
            pt.translate(6, -5);
            points.addPoint(pt);
            
            pt.translate(6, 5);
            points.addPoint(pt);
            
            pt.translate(0, 9);
            points.addPoint(pt);
            
            pt.translate(-6, -6);
            points.addPoint(pt);
            
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
        return new Point(rect.x + rect.width - 16 - getLineWidth(), rect.y + 19);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? new RoundedRectangleAnchor(this) : super.getDefaultConnectionAnchor();
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}
