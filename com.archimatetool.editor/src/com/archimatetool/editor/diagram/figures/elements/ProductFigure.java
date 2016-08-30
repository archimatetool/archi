/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProductFigure
extends AbstractArchimateFigure {
    
    protected static final int flangeFactor = 14;

    public ProductFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
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
