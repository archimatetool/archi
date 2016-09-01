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
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;




/**
 * Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProductFigure
extends AbstractArchimateFigure {
    
    protected static final int FLANGE = 14;

    public ProductFigure() {
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this) {
            @Override
            public void drawFigure(Graphics graphics) {
                graphics.pushState();
                
                Rectangle bounds = getBounds();
                
                if(!isEnabled()) {
                    setDisabledState(graphics);
                }
                
                graphics.setForegroundColor(getLineColor());

                // Top bit
                int middle = bounds.width / 2;
                graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
                graphics.fillRectangle(bounds.x, bounds.y, middle + 1, FLANGE);
                graphics.setBackgroundColor(getFillColor());
                graphics.fillRectangle(bounds.x + middle, bounds.y, middle, FLANGE);
                
                Pattern gradient = null;
                if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
                    gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
                    graphics.setBackgroundPattern(gradient);
                }

                // Main Fill
                graphics.fillRectangle(bounds.x, bounds.y + FLANGE - 1, bounds.width, bounds.height - FLANGE + 1);
                
                if(gradient != null) {
                    gradient.dispose();
                }
                
                // Outline
                graphics.drawLine(bounds.x, bounds.y + FLANGE - 1, bounds.x + middle, bounds.y + FLANGE - 1);
                graphics.drawLine(bounds.x + middle, bounds.y + FLANGE - 1, bounds.x + middle, bounds.y);
                        
                bounds.width--;
                bounds.height--;
                graphics.drawRectangle(bounds);
                
                graphics.popState();
            }

            @Override
            public Rectangle calculateTextControlBounds() {
                Rectangle bounds = super.calculateTextControlBounds();
                bounds.y += FLANGE - 4;
                bounds.height -= 10;
                return bounds;
            }
        };
        
        setFigureDelegate(figureDelegate);
    }
}
