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
        
        Rectangle rect = getBounds();

        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, rect);
            setLineStyle(graphics);
        }

        graphics.setAlpha(getAlpha());

        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);

        graphics.fillRoundRectangle(rect, fArc.width, fArc.height);
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        if(drawOutline) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawRoundRectangle(rect, fArc.width, fArc.height);
        }

        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + 2, rect.y + 2, rect.width - 4, rect.height - 4);
        getOwner().drawIconImage(graphics, rect, imageArea, 0, 0, 0, 0);
        
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
