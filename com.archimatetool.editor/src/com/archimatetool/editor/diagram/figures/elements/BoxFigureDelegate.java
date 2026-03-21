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
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Box Figure Delegate
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class BoxFigureDelegate extends AbstractFigureDelegate {

    private static final int EDGE_SIZE = 14;
    
    public BoxFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();
        
        // Fill the whole figure
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        
        Path fillPath = new Path(null);
        fillPath.moveTo(rect.x, rect.y + EDGE_SIZE);
        fillPath.lineTo(rect.x + EDGE_SIZE, rect.y);
        fillPath.lineTo(rect.x + rect.width, rect.y);
        fillPath.lineTo(rect.x + rect.width, rect.y + rect.height - EDGE_SIZE);
        fillPath.lineTo(rect.x + rect.width - EDGE_SIZE, rect.y + rect.height);
        fillPath.lineTo(rect.x, rect.y + rect.height);
        graphics.fillPath(fillPath);
        fillPath.dispose();
        
        // Fill front rectangle with gradient (if any)
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        //graphics.fillRectangle(rect.x, rect.y + EDGE_SIZE, rect.width - EDGE_SIZE, rect.height - EDGE_SIZE);
        disposeGradientPattern(graphics, gradient);

        // Image icon
        Rectangle imageArea = new Rectangle(rect.x, rect.y + EDGE_SIZE, rect.width - EDGE_SIZE, rect.height - EDGE_SIZE);
        getOwner().drawIconImage(graphics, getBounds(), imageArea);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        Path linePath1 = new Path(null);
        linePath1.moveTo(rect.x, rect.y + EDGE_SIZE);
        linePath1.lineTo(rect.x + EDGE_SIZE, rect.y);
        linePath1.lineTo(rect.x + rect.width, rect.y);
        linePath1.lineTo(rect.x + rect.width, rect.y + rect.height - EDGE_SIZE);
        linePath1.lineTo(rect.x + rect.width - EDGE_SIZE, rect.y + rect.height);
        linePath1.lineTo(rect.x, rect.y + rect.height);
        linePath1.close();
        FigureUtils.drawPath(graphics, linePath1, getLineWidth());
        linePath1.dispose();
        
        int lineWidth = getLineWidth();
        float half = lineWidth / 2.0f;
        
        graphics.setLineWidth(lineWidth);
        Path linePath2 = new Path(null);
        
        linePath2.moveTo(rect.x + lineWidth, rect.y + EDGE_SIZE + half);
        linePath2.lineTo(rect.x + rect.width - EDGE_SIZE, rect.y + EDGE_SIZE + half);
        linePath2.lineTo(rect.x + rect.width, rect.y);
        
        linePath2.moveTo(rect.x + rect.width - EDGE_SIZE, rect.y + EDGE_SIZE);
        linePath2.lineTo(rect.x + rect.width - EDGE_SIZE, rect.y + rect.height - lineWidth);
        
        graphics.drawPath(linePath2);
        linePath2.dispose();
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds();
        
        int textPosition = ((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition();
        int textAlignment = getOwner().getDiagramModelObject().getTextAlignment();
        int iconOffset = getOwner().isIconVisible() ? getOwner().getIconOffset() : 0;
        int edgeOffset = EDGE_SIZE + 1;
        
        // Text position
        switch(textPosition) {
            case ITextPosition.TEXT_POSITION_TOP:
                rect.y += EDGE_SIZE;
                break;

            case ITextPosition.TEXT_POSITION_CENTRE:
                rect.y += EDGE_SIZE / 2;
                break;
            
            default:
                break;
        }
        
        // Text alignment
        switch(textAlignment) {
            case ITextAlignment.TEXT_ALIGNMENT_CENTER:
                if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                    rect.x += iconOffset;
                    rect.width = rect.width - (iconOffset * 2) - edgeOffset;
                }
                else {
                    rect.width -= edgeOffset;
                }
                break;

            case ITextAlignment.TEXT_ALIGNMENT_RIGHT:
                if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                    rect.width -= iconOffset + edgeOffset / 2;
                }
                else {
                    rect.width -= edgeOffset;
                }
                break;

            default:
                break;
        }

        return rect;
    }
}
