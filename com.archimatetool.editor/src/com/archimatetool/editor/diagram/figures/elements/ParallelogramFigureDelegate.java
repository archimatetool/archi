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

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;


/**
 * Parallelogram Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ParallelogramFigureDelegate extends AbstractFigureDelegate {

    protected static final int FLANGE = 16;
    protected static final int TEXT_INDENT = 20;
    
    protected boolean fWithSlash;
    
    public ParallelogramFigureDelegate(AbstractDiagramModelObjectFigure owner, boolean withSlash) {
        super(owner);
        fWithSlash = withSlash;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle rect = getBounds();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);

        PointList points = new PointList();
        points.addPoint(rect.x + FLANGE, rect.y);
        points.addPoint(rect.x + rect.width, rect.y);
        points.addPoint(rect.x + rect.width - FLANGE, rect.y + rect.height);
        points.addPoint(rect.x, rect.y + rect.height);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = FigureUtils.createPathFromPoints(points);
        graphics.fillPath(path);
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points);
        
        // Slash
        if(fWithSlash) {
            graphics.drawLine(rect.x + FLANGE + 20, rect.y, rect.x + 20, rect.y + rect.height);
        }
        
        // Icon
        // getOwner().drawIconImage(graphics, bounds);
        getOwner().drawIconImage(graphics, rect, 0, 0, 0, 0);

        graphics.popState();
    }
}
