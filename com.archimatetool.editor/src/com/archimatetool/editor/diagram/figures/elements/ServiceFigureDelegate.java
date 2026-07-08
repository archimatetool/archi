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
 * @author jbsarrodie
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
        
        // Apply the offset for the fill also so it lines up with the outline
        Rectangle rect = applyLineWidthOffset(graphics);
        
        Dimension arc = getArc();
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillRoundRectangle(rect, arc.width, arc.height);
        disposeGradientPattern(graphics, gradient);

        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + arc.width / 6, rect.y + arc.height / 6, rect.width - arc.width / 3, rect.height - arc.height / 3);
        getOwner().drawIconImage(graphics, getBounds(), imageArea);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        graphics.drawRoundRectangle(rect, arc.width, arc.height);
        
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
