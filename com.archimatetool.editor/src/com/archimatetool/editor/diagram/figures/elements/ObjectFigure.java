/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.ITextPosition;




/**
 * Object Figure
 * 
 * @author Phillip Beauvoir
 */
public class ObjectFigure extends AbstractTextControlContainerFigure {
    
    protected static final int TOP_MARGIN = 12;
    
    class ObjectFigureDelegate extends RectangleFigureDelegate {
        ObjectFigureDelegate(IDiagramModelObjectFigure owner) {
            super(owner);
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
            
            // Main Fill
            graphics.setBackgroundColor(getFillColor());
            
            Pattern gradient = applyGradientPattern(graphics, bounds);
            
            graphics.fillRectangle(bounds);
            
            disposeGradientPattern(graphics, gradient);

            // Icon
            ((AbstractDiagramModelObjectFigure)getOwner()).drawIconImage(graphics, bounds);

            // Outline
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());

            graphics.drawLine(bounds.x, bounds.y + TOP_MARGIN, bounds.x + bounds.width, bounds.y + TOP_MARGIN);
            graphics.drawRectangle(bounds);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            Rectangle bounds = getBounds();
            
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            
            if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                bounds.y += TOP_MARGIN - 4;
            }
            
            return bounds;
        }
    }
    
    
    public ObjectFigure() {
        this(TEXT_FLOW_CONTROL);
        setFigureDelegate(new ObjectFigureDelegate(this));
    }

    public ObjectFigure(int textFlowControl) {
        super(TEXT_FLOW_CONTROL);
    }
}
