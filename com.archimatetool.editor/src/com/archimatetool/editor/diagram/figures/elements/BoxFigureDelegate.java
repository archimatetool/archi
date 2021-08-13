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
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Box Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class BoxFigureDelegate extends AbstractFigureDelegate {

    private static final int EDGE_SIZE = 14;
    
    public BoxFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is consttained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));

        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y + EDGE_SIZE);
        path.lineTo(bounds.x + EDGE_SIZE, bounds.y);
        path.lineTo(bounds.x + bounds.width, bounds.y);
        path.lineTo(bounds.x + bounds.width, bounds.y + bounds.height - EDGE_SIZE);
        path.lineTo(bounds.x + bounds.width - EDGE_SIZE, bounds.y + bounds.height);
        path.lineTo(bounds.x, bounds.y + bounds.height);
        graphics.fillPath(path);
        path.dispose();
        
        // Fill front rectangle
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);

        graphics.fillRectangle(bounds.x, bounds.y + EDGE_SIZE, bounds.width - EDGE_SIZE, bounds.height - EDGE_SIZE);

        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        path = new Path(null);
        
        path.moveTo(bounds.x, bounds.y + EDGE_SIZE);
        path.lineTo(bounds.x + EDGE_SIZE, bounds.y);
        path.lineTo(bounds.x + bounds.width, bounds.y);
        path.lineTo(bounds.x + bounds.width, bounds.y + bounds.height - EDGE_SIZE);
        path.lineTo(bounds.x + bounds.width - EDGE_SIZE, bounds.y + bounds.height);
        path.lineTo(bounds.x, bounds.y + bounds.height);
        path.lineTo(bounds.x, bounds.y + EDGE_SIZE);
        path.lineTo(bounds.x + bounds.width - EDGE_SIZE, bounds.y + EDGE_SIZE);
        path.lineTo(bounds.x + bounds.width, bounds.y);
        path.moveTo(bounds.x + bounds.width - EDGE_SIZE, bounds.y + EDGE_SIZE);
        path.lineTo(bounds.x + bounds.width - EDGE_SIZE, bounds.y + bounds.height);
        
        graphics.drawPath(path);
        path.dispose();

        // Image icon
        Rectangle imageArea = new Rectangle(bounds.x, bounds.y + EDGE_SIZE, bounds.width - EDGE_SIZE, bounds.height - EDGE_SIZE);
        getOwner().drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);

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
