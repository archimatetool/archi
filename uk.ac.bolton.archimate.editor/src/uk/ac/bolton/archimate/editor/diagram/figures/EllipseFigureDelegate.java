/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * Ellipse Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class EllipseFigureDelegate extends AbstractFigureDelegate {
    
    protected int SHADOW_OFFSET = 2;
    
    public EllipseFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();

        if(isEnabled()) {
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            graphics.fillOval(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));
            graphics.setAlpha(255);
        }
        else {
            setDisabledState(graphics);
        }
        
        bounds.width -= SHADOW_OFFSET;
        bounds.height -= SHADOW_OFFSET;
            
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(bounds);

        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(ColorConstants.black);
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
