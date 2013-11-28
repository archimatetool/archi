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
import uk.ac.bolton.archimate.editor.diagram.figures.RectangleFigureDelegate;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Contract Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessContractFigure
extends AbstractTextFlowFigure {
    
    protected static final int flangeFactor = 14;
    
    // Use a Rectangle Figure Delegate to Draw
    RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this) {
        @Override
        public void drawFigure(Graphics graphics) {
            graphics.pushState();
            
            Rectangle bounds = getBounds();
            
            boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
            float outlineContrast = Preferences.STORE.getInt(IPreferenceConstants.OUTLINE_CONTRAST) / 100.0f;
            
            if(isEnabled()) {
                // Shadow
                if(drawShadows) {
                    graphics.setAlpha(100);
                    graphics.setBackgroundColor(ColorConstants.black);
                    graphics.fillRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));
                    graphics.setAlpha(255);
                }
            }
            else {
                setDisabledState(graphics);
            }
            
            int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
            
            bounds.width -= shadow_offset;
            bounds.height -= shadow_offset;
            
            // Top bit
            graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
            graphics.fillRectangle(bounds.x, bounds.y, bounds.width, flangeFactor);
            
            // Main Fill
            graphics.setBackgroundColor(getFillColor());
            graphics.fillRectangle(bounds.x, bounds.y + flangeFactor - 1, bounds.width, bounds.height - flangeFactor + 1);
            
            // Outline
            graphics.setForegroundColor(ColorFactory.getDarkerColor(getFillColor(), outlineContrast));	
            graphics.drawLine(bounds.x, bounds.y + flangeFactor - 1, bounds.x + bounds.width, bounds.y + flangeFactor - 1);
            
            bounds.width--;
            bounds.height--;
            graphics.drawRectangle(bounds);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            Rectangle bounds = super.calculateTextControlBounds();
            bounds.y += flangeFactor - 4;
            bounds.height -= 10;
            return bounds;
        }
    };
    
    public BusinessContractFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        setFigureDelegate(figureDelegate);
    }
}
