/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextPosition;




/**
 * Process Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ProcessFigureDelegate extends AbstractFigureDelegate {

    public ProcessFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, getBounds(), getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        PointList points = getPointList();
        graphics.fillPolygon(points);
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        for(int i = 0; i < points.size() - 1; i++) {
            graphics.drawLine(points.getPoint(i), points.getPoint(i + 1));
        }
        
        graphics.popState();
    }
    
    private PointList getPointList() {
        Rectangle bounds = getBounds();
        
        bounds.width -= 1;
        bounds.height -= 1;

        PointList points = new PointList();
        
        points.addPoint(bounds.x, bounds.y + (bounds.height / 5));
        points.addPoint(bounds.x + (int)(bounds.width * 0.7), points.getPoint(0).y);
        points.addPoint(points.getPoint(1).x, bounds.y);
        points.addPoint(bounds.x + bounds.width, bounds.y + (bounds.height / 2));
        points.addPoint(points.getPoint(2).x, bounds.y + bounds.height);
        points.addPoint(points.getPoint(2).x, bounds.y + bounds.height - (bounds.height / 5));
        points.addPoint(points.getPoint(0).x, points.getPoint(5).y);
        points.addPoint(points.getPoint(0).x, points.getPoint(0).y);
        
        return points;
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        if(((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += bounds.height / 5;
        }
        
        return bounds;
    }

}
