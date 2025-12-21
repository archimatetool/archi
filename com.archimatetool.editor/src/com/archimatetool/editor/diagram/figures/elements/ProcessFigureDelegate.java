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
        
        Rectangle rect = getBounds();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        Pattern gradient = applyGradientPattern(graphics, rect);

        Path path = new Path(null);
        
        float x1 = rect.x + (rect.width * 0.7f);
        float y1 = rect.y + (rect.height / 5);
        float y2 = rect.y + rect.height - (rect.height / 5);
        
        float lineOffset = (float)getLineWidth() / 2;

        path.moveTo(rect.x, y1);
        path.lineTo(x1, y1);
        path.lineTo(x1, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + (rect.height / 2));
        path.lineTo(x1, rect.y + rect.height);
        path.lineTo(x1, y2);
        path.lineTo(rect.x, y2);
        path.lineTo(rect.x, y1 - lineOffset);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Icon
        // getOwner().drawIconImage(graphics, bounds);
        getOwner().drawIconImage(graphics, rect,
                rect.height / 5 + 1, (int)-(rect.width * 0.2f), -(rect.height / 5 + 1), 0);

        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds();
        
        if(((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += rect.height / 5;
        }
        
        return rect;
    }

}
