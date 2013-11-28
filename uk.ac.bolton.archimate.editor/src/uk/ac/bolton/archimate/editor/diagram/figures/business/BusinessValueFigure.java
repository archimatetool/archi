/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Value Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessValueFigure
extends AbstractTextFlowFigure {

    protected static final int SHADOW_OFFSET = 2;
    
    public BusinessValueFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        float outlineContrast = Preferences.STORE.getInt(IPreferenceConstants.OUTLINE_CONTRAST) / 100.0f;
        
        if(isEnabled()) {
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillOval(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height  - SHADOW_OFFSET);
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(bounds.x, bounds.y, bounds.width - shadow_offset, bounds.height - shadow_offset);
        
        // Outline
        graphics.setForegroundColor(ColorFactory.getDarkerColor(getFillColor(), outlineContrast));
        graphics.drawOval(bounds.x, bounds.y, bounds.width - shadow_offset - 1, bounds.height - shadow_offset - 1);
        
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
