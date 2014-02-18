/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Business Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProductFigure
extends AbstractArchimateFigure {
    
    protected static final int flangeFactor = 14;

    public BusinessProductFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this) {
            @Override
            public void drawFigure(Graphics graphics) {
                graphics.pushState();
                
                Rectangle bounds = getBounds();
                
                boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
                
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
                
                graphics.setForegroundColor(getLineColor());

                // Top bit
                int middle = bounds.width / 2;
                graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
                graphics.fillRectangle(bounds.x, bounds.y, middle + 1, flangeFactor);
                graphics.setBackgroundColor(getFillColor());
                graphics.fillRectangle(bounds.x + middle, bounds.y, middle, flangeFactor);
                
                // Main Fill
                graphics.fillRectangle(bounds.x, bounds.y + flangeFactor - 1, bounds.width, bounds.height - flangeFactor + 1);
                
                // Outline
                graphics.drawLine(bounds.x, bounds.y + flangeFactor - 1, bounds.x + middle, bounds.y + flangeFactor - 1);
                graphics.drawLine(bounds.x + middle, bounds.y + flangeFactor - 1, bounds.x + middle, bounds.y);
                        
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
        
        setFigureDelegate(figureDelegate);
    }
}
