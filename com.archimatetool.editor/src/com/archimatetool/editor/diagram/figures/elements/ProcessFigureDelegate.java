/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Process Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ProcessFigureDelegate extends AbstractFigureDelegate {

    public ProcessFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Rectangle bounds = getBounds();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        int lineWidth = 1;
        setLineWidth(graphics, lineWidth, bounds);
        
        Pattern gradient = applyGradientPattern(graphics, bounds);

        Path path = new Path(null);
        
        float x1 = bounds.x + (bounds.width * 0.7f);
        float y1 = bounds.y + (bounds.height / 5);
        float y2 = bounds.y + bounds.height - (bounds.height / 5);
        
        float lineOffset = (float)lineWidth / 2;

        path.moveTo(bounds.x, y1);
        path.lineTo(x1, y1);
        path.lineTo(x1, bounds.y);
        path.lineTo(bounds.x + bounds.width, bounds.y + (bounds.height / 2));
        path.lineTo(x1, bounds.y + bounds.height);
        path.lineTo(x1, y2);
        path.lineTo(bounds.x, y2);
        path.lineTo(bounds.x, y1 - lineOffset);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);

        // Icon
        getOwner().drawIconImage(graphics, bounds);

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        if(((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += bounds.height / 5;
        }
        
        return bounds;
    }

}
