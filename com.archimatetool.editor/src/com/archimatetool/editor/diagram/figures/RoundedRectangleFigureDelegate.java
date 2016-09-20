/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

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
    
    public RoundedRectangleFigureDelegate(IDiagramModelObjectFigure owner, int iconOffset) {
        super(owner, iconOffset);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }

        graphics.fillRoundRectangle(bounds, fArc.width, fArc.height);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(bounds, fArc.width, fArc.height);

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
