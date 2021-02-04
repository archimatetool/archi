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




/**
 * Rounded Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RoundedRectangleFigureDelegate extends RectangleFigureDelegate
implements IRoundedRectangleFigure {

    private Dimension fArc = new Dimension(20, 20);
    
    public RoundedRectangleFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    public RoundedRectangleFigureDelegate(AbstractDiagramModelObjectFigure owner, int iconOffset) {
        super(owner, iconOffset);
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

        graphics.fillRoundRectangle(bounds, fArc.width, fArc.height);
        
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        getOwner().drawIconImage(graphics, bounds);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(bounds, fArc.width, fArc.height);

        graphics.popState();
    }
    
    @Override
    public void setArc(Dimension arc) {
        fArc.width = arc.width;
        fArc.height = arc.height;
    }
    
    @Override
    public Dimension getArc() {
        return fArc.getCopy();
    }

}
