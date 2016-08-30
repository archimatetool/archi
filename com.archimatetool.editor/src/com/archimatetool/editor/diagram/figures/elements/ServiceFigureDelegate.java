/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;




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
        
        Dimension arc = getArc();
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
                arc.width, arc.height);
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1),
                arc.width, arc.height);
        
        graphics.popState();
    }
    
    public Dimension getArc() {
        Rectangle bounds = getBounds();
        return new Dimension(Math.min(bounds.height, bounds.width * 8/10), bounds.height);
    }
    
    @Override
    public void setArc(Dimension arc) {
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }
}
