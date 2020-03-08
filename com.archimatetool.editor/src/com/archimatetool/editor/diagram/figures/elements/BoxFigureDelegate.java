/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Box Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class BoxFigureDelegate extends AbstractFigureDelegate {

    protected static final int FOLD_HEIGHT = 14;
    
    protected int iconOffset;
    
    public BoxFigureDelegate(IDiagramModelObjectFigure owner, int iconOffset) {
        super(owner);
        this.iconOffset = iconOffset;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Outer shape
        PointList shape = new PointList();
        shape.addPoint(bounds.x, bounds.y + bounds.height - 1);
        shape.addPoint(bounds.x, bounds.y + FOLD_HEIGHT);
        shape.addPoint(bounds.x + FOLD_HEIGHT, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - FOLD_HEIGHT - 1);
        shape.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + bounds.height - 1);
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(shape);

        // Fill front rectangle
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }

        graphics.fillRectangle(bounds.x, bounds.y + FOLD_HEIGHT, bounds.width - FOLD_HEIGHT, bounds.height - FOLD_HEIGHT);

        if(gradient != null) {
            gradient.dispose();
        }

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(shape);
        graphics.drawLine(bounds.x, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - 1, bounds.y);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + - FOLD_HEIGHT + bounds.width - 1, bounds.y + bounds.height - 1);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        
        int offset = FOLD_HEIGHT + 1;
        
        int textpos = ((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition();
        int textAlignment = getOwner().getDiagramModelObject().getTextAlignment();
        
        if(textpos == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += FOLD_HEIGHT;
            
            if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                bounds.x += iconOffset;
                bounds.width = bounds.width - (iconOffset * 2) - offset;
            }
            else if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                bounds.width -= offset + iconOffset;
            }
        }
        else {
            if(textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                bounds.width -= offset;
            }
        }

        return bounds;
    }

}
