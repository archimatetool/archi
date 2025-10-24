/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Figure for a Motiviation Element
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractMotivationFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int FLANGE = 10;
    
    protected AbstractMotivationFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        bounds.resize(-1, -1);
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, bounds);
            setLineStyle(graphics);
        }
       
        PointList points = new PointList();
        points.addPoint(bounds.x + FLANGE, bounds.y);
        points.addPoint(bounds.x + bounds.width - FLANGE, bounds.y);
        points.addPoint(bounds.x + bounds.width, bounds.y + FLANGE);
        points.addPoint(bounds.x + bounds.width, bounds.y + bounds.height - FLANGE);
        points.addPoint(bounds.x + bounds.width - FLANGE, bounds.y + bounds.height);
        points.addPoint(bounds.x + FLANGE, bounds.y + bounds.height);
        points.addPoint(bounds.x, bounds.y + bounds.height - FLANGE);
        points.addPoint(bounds.x, bounds.y + FLANGE);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        //graphics.fillPolygon(points);
        Path path = FigureUtils.createPathFromPoints(points);
        graphics.fillPath(path);
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);

        // Line
        if(drawOutline) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawPolygon(points);
        }

        // Image Icon
        Rectangle imageArea = new Rectangle(bounds.x + FLANGE / 2, bounds.y + FLANGE / 2, bounds.width - FLANGE, bounds.height - FLANGE);
        drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);

        graphics.popState();
    }
}