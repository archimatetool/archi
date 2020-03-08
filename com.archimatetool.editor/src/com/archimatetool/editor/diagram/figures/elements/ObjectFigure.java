/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.model.IDiagramModelObject;
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
            
            graphics.setAlpha(getAlpha());
            
            if(!isEnabled()) {
                setDisabledState(graphics);
            }
            
            // Main Fill
            graphics.setBackgroundColor(getFillColor());
            
            Pattern gradient = null;
            if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
                gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
                graphics.setBackgroundPattern(gradient);
            }
            
            graphics.fillRectangle(bounds);
            
            if(gradient != null) {
                gradient.dispose();
            }

            // Outline
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());

            bounds.width--;
            bounds.height--;
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
