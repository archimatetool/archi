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
        
        Rectangle rect = getBounds();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);

        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);

        Dimension arc = getArc();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, rect);

        graphics.fillRoundRectangle(new Rectangle(rect.x, rect.y, rect.width, rect.height),
                arc.width, arc.height);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRoundRectangle(new Rectangle(rect.x, rect.y, rect.width, rect.height),
                arc.width, arc.height);
        
        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + arc.width / 6, rect.y + arc.height / 6, rect.width - arc.width / 3, rect.height - arc.height / 3);
        getOwner().drawIconImage(graphics, rect, imageArea, 0, 0, 0, 0);

        graphics.popState();
    }
    
    @Override
    public Dimension getArc() {
        Rectangle rect = getBounds();
        return new Dimension(Math.min(rect.height, rect.width * 8/10), rect.height);
    }
    
    @Override
    public void setArc(Dimension arc) {
    }
}
