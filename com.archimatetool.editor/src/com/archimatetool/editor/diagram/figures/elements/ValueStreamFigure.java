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
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IIconic;


/**
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 */
public class ValueStreamFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate fMainFigureDelegate;
    
    public ValueStreamFigure() {
        super(TEXT_FLOW_CONTROL);
        fMainFigureDelegate = new RoundedRectangleFigureDelegate(this);
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

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);

        int indent = Math.min(rect.height / 2, rect.width / 2);
        int centre_y = rect.y + rect.height / 2;
        int point_startx = rect.x + rect.width - indent;

        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Shape
        Path path = new Path(null);
        path.moveTo(rect.x, rect.y);
        path.lineTo(rect.x + indent, centre_y);
        path.lineTo(rect.x, rect.y + rect.height);
        path.lineTo(point_startx, rect.y + rect.height);
        path.lineTo(rect.x + rect.width, centre_y);
        path.lineTo(point_startx, rect.y);
        path.lineTo(rect.x, rect.y);
        path.lineTo(rect.x + indent, centre_y);
        
        // Fill
        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = applyGradientPattern(graphics, rect);

        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        path.dispose();
        
        // Icon
        // drawIconImage(graphics, bounds);
        
        int top = 0, right = 0, left = 0, bottom = 0;
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
            case IIconic.ICON_POSITION_BOTTOM_LEFT:
                left = 10;
                break;

            case IIconic.ICON_POSITION_TOP_RIGHT:
            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                right = -indent;
                break;

            case IIconic.ICON_POSITION_MIDDLE_LEFT:
                left = indent;
                break;

            case IIconic.ICON_POSITION_MIDDLE_RIGHT:
                right = -10;
                break;
        }
        drawIconImage(graphics, rect, top, right, bottom, left);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null,  getIconOrigin());
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
            
            // Start at top left
            points.addPoint(pt);
            
            pt.translate(10, 0);
            points.addPoint(pt);
            
            pt.translate(5, 5);
            points.addPoint(pt);
            
            pt.translate(-5, 5);
            points.addPoint(pt);
            
            pt.translate(-10, 0);
            points.addPoint(pt);
            
            pt.translate(5, -5);
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
    protected Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.getRight().x - 18 - getLineWidth(), rect.y + 7);
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fMainFigureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 25 : 0;
    }
}