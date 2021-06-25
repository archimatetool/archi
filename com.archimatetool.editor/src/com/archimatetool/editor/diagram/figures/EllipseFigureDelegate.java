/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;



/**
 * Ellipse Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class EllipseFigureDelegate extends AbstractFigureDelegate {
    
    public EllipseFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();

        bounds.width--;
        bounds.height--;

        // Line Width
        setLineWidth(graphics, 1, bounds);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillOval(bounds);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawOval(bounds);
        
        // Image Icon
        Rectangle imageArea = new Rectangle(bounds.x + (bounds.width / 6), bounds.y + (bounds.height / 6),
                bounds.width - (bounds.width / 3), bounds.height - (bounds.height / 3));
        getOwner().drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);

        graphics.popState();
    }
}
