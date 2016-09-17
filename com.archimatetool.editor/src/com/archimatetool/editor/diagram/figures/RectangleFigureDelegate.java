/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.ITextPosition;



/**
 * Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RectangleFigureDelegate extends AbstractFigureDelegate {
    
    protected int iconOffset;
    
    public RectangleFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    public RectangleFigureDelegate(IDiagramModelObjectFigure owner, int iconOffset) {
        super(owner);
        this.iconOffset = iconOffset;
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
        
        graphics.fillRectangle(bounds);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        if(getOwner().getDiagramModelObject() instanceof ITextPosition) {
            int textpos = ((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition();
            
            switch(textpos) {
                // If the figure has an icon move centre inwards
                case ITextPosition.TEXT_POSITION_TOP_CENTRE:
                    bounds.x += iconOffset;
                    bounds.width = bounds.width - (iconOffset * 2);
                    break;
                // top right needs indent for icon
                case ITextPosition.TEXT_POSITION_TOP_RIGHT:
                    bounds.width -= iconOffset;
                    break;
                default:
                    break;
            }
        }

        return bounds;
    }

}
