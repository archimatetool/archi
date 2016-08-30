/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.GradientUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Meaning Figure
 * 
 * @author Phillip Beauvoir
 */
public class MeaningFigure
extends AbstractArchimateFigure {

    public MeaningFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // The following is the most awful code to draw a cloud...
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = GradientUtils.createScaledPattern(graphics, bounds, getFillColor());
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
        graphics.setForegroundColor(getLineColor());
        graphics.drawArc(bounds.x, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, 60, 147);
        graphics.drawArc(bounds.x + bounds.width/3 - 1, bounds.y, bounds.width/3 * 2 - 1, bounds.height/3 * 2, -40, 159);
        graphics.drawArc(bounds.x, bounds.y + bounds.height / 3, bounds.width/5 * 3 - 1, bounds.height/3 * 2 - 1, -43, -167);
        graphics.drawArc(bounds.x + bounds.width/3, bounds.y + bounds.height/4, bounds.width/5 * 3 - 1, bounds.height/3 * 2 - 1, 0, -110);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 10;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }
    
}
