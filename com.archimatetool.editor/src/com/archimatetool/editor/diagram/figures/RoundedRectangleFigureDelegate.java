/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;




/**
 * Rounded Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RoundedRectangleFigureDelegate extends RectangleFigureDelegate
implements IRoundedRectangleFigure {

    private Dimension fArc = new Dimension(20, 20);
    
    public RoundedRectangleFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET),
                        fArc.width, fArc.height);
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
        graphics.fillRoundRectangle(bounds, fArc.width, fArc.height);
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(bounds, fArc.width, fArc.height);

        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
    
    public void setArc(Dimension arc) {
        fArc.width = arc.width;
        fArc.height = arc.height;
    }
    
    public Dimension getArc() {
        return fArc.getCopy();
    }

}
