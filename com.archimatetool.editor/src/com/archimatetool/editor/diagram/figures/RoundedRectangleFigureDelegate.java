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

import com.archimatetool.model.IDiagramModelObject;




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
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();

        bounds.width--;
        bounds.height--;
        
        if(getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, bounds);
            setLineStyle(graphics);
        }

        graphics.setAlpha(getAlpha());

        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);

        graphics.fillRoundRectangle(bounds, fArc.width, fArc.height);
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        if(getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawRoundRectangle(bounds, fArc.width, fArc.height);
        }

        // Image Icon
        Rectangle imageArea = new Rectangle(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4);
        getOwner().drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);
        
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
