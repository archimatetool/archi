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
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;




/**
 * Object Figure
 * 
 * @author Phillip Beauvoir
 */
public class ObjectFigure extends AbstractTextControlContainerFigure {
    
    protected static final int flangeFactor = 14;
    
    public ObjectFigure() {
        super(TEXT_FLOW_CONTROL);
        
        setFigureDelegate(new RectangleFigureDelegate(this) {
            @Override
            public void drawFigure(Graphics graphics) {
                graphics.pushState();
                
                Rectangle bounds = getBounds();
                
                if(!isEnabled()) {
                    setDisabledState(graphics);
                }
                
                graphics.setForegroundColor(getLineColor());

                // Top bit
                graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
                graphics.fillRectangle(bounds.x, bounds.y, bounds.width, flangeFactor);
                
                // Main Fill
                graphics.setBackgroundColor(getFillColor());
                
                Pattern gradient = null;
                if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
                    gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
                    graphics.setBackgroundPattern(gradient);
                }
                
                graphics.fillRectangle(bounds.x, bounds.y + flangeFactor - 1, bounds.width, bounds.height - flangeFactor + 1);
                
                if(gradient != null) {
                    gradient.dispose();
                }

                // Outline
                bounds.width--;
                bounds.height--;
                graphics.drawLine(bounds.x, bounds.y + flangeFactor - 1, bounds.x + bounds.width, bounds.y + flangeFactor - 1);
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
        });
    }
}
