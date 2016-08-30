/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Value Figure
 * 
 * @author Phillip Beauvoir
 */
public class ValueFigure
extends AbstractArchimateFigure {

    public ValueFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 10;
        bounds.width = bounds.width - 40;
        bounds.height -= 15;
        return bounds;
    }
}
