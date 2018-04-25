/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.model.ITextPosition;

/**
 * Contract Figure
 * 
 * @author Phillip Beauvoir
 */
public class ContractFigure extends ObjectFigure {
    
    class ContractFigureDelegate extends ObjectFigureDelegate {
        ContractFigureDelegate(IDiagramModelObjectFigure owner) {
            super(owner);
        }
        
        @Override
        public void drawFigure(Graphics graphics) {
            super.drawFigure(graphics);
            
            graphics.pushState();
            
            graphics.setAlpha(getAlpha());
            
            Rectangle bounds = getBounds();
            
            // Line
            graphics.setForegroundColor(getLineColor());

            bounds.width--;
            graphics.drawLine(bounds.x, bounds.getBottom().y - TOP_MARGIN, bounds.getRight().x, bounds.getBottom().y - TOP_MARGIN);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            if(textPosition == ITextPosition.TEXT_POSITION_BOTTOM) {
                Rectangle bounds = getBounds();
                bounds.y -= TOP_MARGIN - 4;
                return bounds;
            }
            else {
                return super.calculateTextControlBounds();
            }
        }

    }

    public ContractFigure() {
        super(TEXT_FLOW_CONTROL);
        setFigureDelegate(new ContractFigureDelegate(this));
    }
    
}
