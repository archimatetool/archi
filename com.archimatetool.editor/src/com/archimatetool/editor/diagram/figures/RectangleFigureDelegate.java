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
        
        Rectangle rect = getBounds();
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillRectangle(rect);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        getOwner().drawIconImage(graphics, getBounds().getCopy());
        
        // Outline
        if(getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE) {
            setLineStyle(graphics);
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            FigureUtils.drawRectangle(graphics, rect, getLineWidth());
        }
        
        graphics.popState();
    }
}
