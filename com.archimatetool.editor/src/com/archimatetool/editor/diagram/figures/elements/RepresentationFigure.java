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

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.model.ITextPosition;




/**
 * Representation Figure
 * 
 * @author Phillip Beauvoir
 */
public class RepresentationFigure extends AbstractTextControlContainerFigure {
    
    protected static final int TOP_MARGIN = 12;

    public RepresentationFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        int lineWidth = 1;
        setLineWidth(graphics, lineWidth, bounds);

        int offset = 6;
        int curve_y = bounds.y + bounds.height - offset;
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Main Fill
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x, curve_y - 1);
        
        path.quadTo(bounds.x + (bounds.width / 4), bounds.y + bounds.height + offset,
                bounds.x + bounds.width / 2 + 1, curve_y);
        
        path.quadTo(bounds.x + bounds.width - (bounds.width / 4), curve_y - offset - 1,
                bounds.x + bounds.width, curve_y);
        
        path.lineTo(bounds.x + bounds.width, bounds.y);
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        float lineOffset = (float)lineWidth / 2;
        path.lineTo(bounds.x - lineOffset, bounds.y);
        graphics.drawPath(path);
        path.dispose();
        
        // Line
        graphics.drawLine(bounds.x, bounds.y + TOP_MARGIN, bounds.x + bounds.width, bounds.y + TOP_MARGIN);
        
        // Icon
        drawIconImage(graphics, bounds);

        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += TOP_MARGIN - 4;
        }
        
        return bounds;
    }
}
