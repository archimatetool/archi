/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.model.ITextAlignment;
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
        
        bounds.width--;
        bounds.height--;

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillRectangle(bounds);
        
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        ((AbstractDiagramModelObjectFigure)getOwner()).drawIconImage(graphics, bounds);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        if(getOwner().getDiagramModelObject() instanceof ITextPosition) {
            int textpos = ((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition();
            int textAlignment = getOwner().getDiagramModelObject().getTextAlignment();
            
            if(textpos == ITextPosition.TEXT_POSITION_TOP) {
                if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                    bounds.x += iconOffset;
                    bounds.width = bounds.width - (iconOffset * 2);
                }
                else if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                    bounds.width -= iconOffset;
                }
            }
        }

        return bounds;
    }

}
