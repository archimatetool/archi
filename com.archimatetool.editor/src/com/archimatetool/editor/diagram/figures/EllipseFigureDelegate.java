/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;



/**
 * Ellipse Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class EllipseFigureDelegate extends AbstractFigureDelegate {
    
    public EllipseFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(bounds);

        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawOval(bounds);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.y += 10;
        return bounds;
    }
}
