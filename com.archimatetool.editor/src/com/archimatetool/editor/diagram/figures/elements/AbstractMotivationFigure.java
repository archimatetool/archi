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
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, rect);
            setLineStyle(graphics);
        }
       
        PointList points = new PointList();
        points.addPoint(rect.x + FLANGE, rect.y);
        points.addPoint(rect.x + rect.width - FLANGE, rect.y);
        points.addPoint(rect.x + rect.width, rect.y + FLANGE);
        points.addPoint(rect.x + rect.width, rect.y + rect.height - FLANGE);
        points.addPoint(rect.x + rect.width - FLANGE, rect.y + rect.height);
        points.addPoint(rect.x + FLANGE, rect.y + rect.height);
        points.addPoint(rect.x, rect.y + rect.height - FLANGE);
        points.addPoint(rect.x, rect.y + FLANGE);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);
        
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
        Rectangle imageArea = new Rectangle(rect.x + FLANGE / 2, rect.y + FLANGE / 2, rect.width - FLANGE, rect.height - FLANGE);
        drawIconImage(graphics, rect, imageArea, 0, 0, 0, 0);

        graphics.popState();
    }
}