/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.model.IDiagramModelObject;




/**
 * Service Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ServiceFigureDelegate
extends AbstractFigureDelegate
implements IRoundedRectangleFigure {

    public ServiceFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        bounds.width--;
        bounds.height--;

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        Dimension arc = getArc();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }

        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
                arc.width, arc.height);
        
        if(gradient != null) {
            graphics.setBackgroundPattern(null); // Must set this to null in case of calling graphics.pushState() / graphics.popState();
            gradient.dispose();
        }

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
                arc.width, arc.height);
        
        graphics.popState();
    }
    
    @Override
    public Dimension getArc() {
        Rectangle bounds = getBounds();
        return new Dimension(Math.min(bounds.height, bounds.width * 8/10), bounds.height);
    }
    
    @Override
    public void setArc(Dimension arc) {
    }
}
