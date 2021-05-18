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

        Rectangle bounds = getBounds();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        PointList points = new PointList();
        points.addPoint(bounds.x + FLANGE, bounds.y);
        points.addPoint(bounds.x + bounds.width, bounds.y);
        points.addPoint(bounds.x + bounds.width - FLANGE, bounds.y + bounds.height);
        points.addPoint(bounds.x, bounds.y + bounds.height);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
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
            graphics.drawLine(bounds.x + FLANGE + 20, bounds.y, bounds.x + 20, bounds.y + bounds.height);
        }
        
        // Icon
        // getOwner().drawIconImage(graphics, bounds);
        getOwner().drawIconImage(graphics, bounds, 0, 0, 0, 0);

        graphics.popState();
    }
}
