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
 * Business Meaning Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessMeaningFigure
extends AbstractTextFlowFigure {

    protected static final int SHADOW_OFFSET = 2;
    
    public BusinessMeaningFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // The following is the most awful code to draw a cloud...
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        float outlineContrast = Preferences.STORE.getInt(IPreferenceConstants.OUTLINE_CONTRAST) / 100.0f;
        
        if(isEnabled()) {
            // Shadow fill
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillOval(bounds.x + bounds.width / 3 + 1, bounds.y + 1, bounds.width / 3 * 2, bounds.height / 3 * 2);
                graphics.fillOval(bounds.x, bounds.y + bounds.height / 3, bounds.width / 5 * 3, bounds.height / 3 * 2);
                graphics.fillOval(bounds.x + bounds.width / 3, bounds.y + bounds.height / 4, bounds.width / 5 * 3, bounds.height / 3 * 2);
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(bounds.x, bounds.y, bounds.width / 3 * 2, bounds.height / 3 * 2);
        graphics.fillOval(bounds.x + bounds.width / 3 - 1, bounds.y, bounds.width / 3 * 2, bounds.height / 3 * 2);
        graphics.fillOval(bounds.x, bounds.y + bounds.height / 3, bounds.width / 5 * 3, bounds.height / 3 * 2 - shadow_offset);
        graphics.fillOval(bounds.x + bounds.width / 3, bounds.y + bounds.height / 4, bounds.width / 5 * 3 - shadow_offset, bounds.height / 3 * 2 - shadow_offset);
        
        // Outline
        graphics.setForegroundColor(ColorFactory.getDarkerColor(getFillColor(), outlineContrast));
        graphics.drawArc(bounds.x, bounds.y, bounds.width / 3 * 2, bounds.height / 3 * 2, 60, 147);
        graphics.drawArc(bounds.x + bounds.width / 3 - 1, bounds.y, bounds.width / 3 * 2 - 1, bounds.height / 3 * 2, -40, 159);
        graphics.drawArc(bounds.x, bounds.y + bounds.height / 3, bounds.width / 5 * 3 - 1, bounds.height / 3 * 2 - shadow_offset - 1, -43, -167);
        graphics.drawArc(bounds.x + bounds.width / 3, bounds.y + bounds.height / 4, bounds.width / 5 * 3 - shadow_offset - 1, bounds.height / 3 * 2 - shadow_offset - 1, 0, -110);
        
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
