/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;




/**
 * Service Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ServiceFigureDelegate
extends AbstractFigureDelegate
implements IRoundedRectangleFigure {

    public ServiceFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        // Reduce width and height by 1 pixel
        bounds.resize(-1, -1);

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, bounds);

        Dimension arc = getArc();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);

        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
                arc.width, arc.height);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height),
                arc.width, arc.height);
        
        // Image Icon
        Rectangle imageArea = new Rectangle(bounds.x + arc.width / 6, bounds.y + arc.height / 6, bounds.width - arc.width / 3, bounds.height - arc.height / 3);
        getOwner().drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);

        graphics.popState();
    }
    
    @Override
    public Dimension getArc() {
        Rectangle bounds = getBounds();
        return new Dimension(Math.min(bounds.height, bounds.width * 8/10), bounds.height);
    }
    
    @Override
    public void setArc(Dimension arc) {
    }
}
