/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.model.IDiagramModelObject;



/**
 * Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RectangleFigureDelegate extends AbstractFigureDelegate {
    
    public RectangleFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle bounds = getBounds();
        
        // Reduce width and height by 1 pixel
        bounds.resize(-1, -1);
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;

        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, bounds);
            setLineStyle(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillRectangle(bounds);
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        if(drawOutline) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawRectangle(bounds);
        }
        
        // Icon
        // getOwner().drawIconImage(graphics, bounds);
        getOwner().drawIconImage(graphics, bounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
}
