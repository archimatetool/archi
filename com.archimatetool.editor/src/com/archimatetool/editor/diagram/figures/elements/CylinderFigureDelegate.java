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


/**
 * Cylinder Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class CylinderFigureDelegate extends AbstractFigureDelegate {
    
    private final int OFFSET = 4;

    public CylinderFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();
        
        rect.width--;
        rect.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        path.addArc(rect.x, rect.y, rect.width / OFFSET, rect.height, 90, 180);
        path.lineTo((rect.x + rect.width) - (rect.width / OFFSET * 2), rect.y + rect.height);
        path.addArc((rect.x + rect.width) - (rect.width / OFFSET), rect.y, rect.width / OFFSET, rect.height, 270, 180);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.drawLine(rect.x + (rect.width / OFFSET / 2), rect.y, (rect.x + rect.width) - (rect.width / OFFSET / 2), rect.y);
        graphics.drawArc((rect.x + rect.width) - (rect.width / OFFSET), rect.y, rect.width / OFFSET, rect.height, 90, 180);
        
        // Image Icon
        getOwner().drawIconImage(graphics, rect, 0, 0, 0, 0);
        
        graphics.popState();
    }
}
