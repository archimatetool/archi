/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;



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
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        float outlineContrast = Preferences.STORE.getInt(IPreferenceConstants.OUTLINE_CONTRAST) / 100.0f;
        
        if(isEnabled()) {
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET),
                    fArc, fArc);
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        bounds.width -= shadow_offset;
        bounds.height -= shadow_offset;
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(bounds, fArc, fArc);
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(ColorFactory.getDarkerColor(getFillColor(), outlineContrast));
        graphics.drawRoundRectangle(bounds, fArc, fArc);

        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
}
