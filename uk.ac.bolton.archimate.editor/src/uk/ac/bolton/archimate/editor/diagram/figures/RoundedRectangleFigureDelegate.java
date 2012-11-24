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
 * Rounded Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RoundedRectangleFigureDelegate extends RectangleFigureDelegate {

    protected int fArc = 20;
    
    public RoundedRectangleFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    public void setArc(int arc) {
        fArc = arc;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        if(isEnabled()) {
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET),
                    fArc, fArc);
            graphics.setAlpha(255);
        }
        else {
            setDisabledState(graphics);
        }
        
        bounds.width -= SHADOW_OFFSET;
        bounds.height -= SHADOW_OFFSET;
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(bounds, fArc, fArc);
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawRoundRectangle(bounds, fArc, fArc);

        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
}
