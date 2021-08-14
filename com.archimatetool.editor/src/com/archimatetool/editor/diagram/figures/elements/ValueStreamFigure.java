/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
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
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        int indent = Math.min(bounds.height / 2, bounds.width / 2);
        int centre_y = bounds.y + bounds.height / 2;
        int point_startx = bounds.x + bounds.width - indent;

        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Shape
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x + indent, centre_y);
        path.lineTo(bounds.x, bounds.y + bounds.height);
        path.lineTo(point_startx, bounds.y + bounds.height);
        path.lineTo(bounds.x + bounds.width, centre_y);
        path.lineTo(point_startx, bounds.y);
        path.lineTo(bounds.x, bounds.y);
        path.lineTo(bounds.x + indent, centre_y);
        
        // Fill
        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = applyGradientPattern(graphics, bounds);

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
        drawIconImage(graphics, bounds, top, right, bottom, left);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        PointList points = new PointList();
        
        // Start at top left
        Point pt = getIconOrigin();
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
        
        graphics.drawPolygon(points);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 19, bounds.y + 7);
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