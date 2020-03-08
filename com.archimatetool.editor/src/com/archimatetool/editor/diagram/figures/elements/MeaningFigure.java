/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.model.IDiagramModelObject;




/**
 * Meaning Figure
 * 
 * @author Phillip Beauvoir
 */
public class MeaningFigure extends AbstractTextControlContainerFigure {

    public MeaningFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // The following is the most awful code to draw a cloud...
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }

        graphics.fillOval(bounds.x, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2);
        graphics.fillOval(bounds.x + bounds.width/3 - 1, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2);
        graphics.fillOval(bounds.x, bounds.y + bounds.height/3, bounds.width/5 * 3, bounds.height/3 * 2);
        graphics.fillOval(bounds.x + bounds.width/3, bounds.y + bounds.height/4, bounds.width/5 * 3, bounds.height/3 * 2);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawArc(bounds.x, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, 60, 147);
        graphics.drawArc(bounds.x + bounds.width/3 - 1, bounds.y, bounds.width/3 * 2 - 1, bounds.height/3 * 2, -40, 159);
        graphics.drawArc(bounds.x, bounds.y + bounds.height / 3, bounds.width/5 * 3 - 1, bounds.height/3 * 2 - 1, -43, -167);
        graphics.drawArc(bounds.x + bounds.width/3, bounds.y + bounds.height/4, bounds.width/5 * 3 - 1, bounds.height/3 * 2 - 1, 0, -110);
        
        graphics.popState();
    }
}
