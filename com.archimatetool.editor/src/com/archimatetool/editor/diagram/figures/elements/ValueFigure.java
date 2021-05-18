/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.model.IIconic;




/**
 * Value Figure
 * 
 * @author Phillip Beauvoir
 */
public class ValueFigure extends AbstractTextControlContainerFigure {

    public ValueFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);

        graphics.fillOval(bounds);
        
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawOval(bounds);
        
        // Icon
        // drawIconImage(graphics, bounds);
        
        int top = 0, right = 0, left = 0, bottom = 0;
        int offset = 6;
        switch(((IIconic)getDiagramModelObject()).getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
                top = bounds.height / offset;
                left = bounds.width / offset;
                break;

            case IIconic.ICON_POSITION_TOP_RIGHT:
                top = bounds.height / offset;
                right = -(bounds.width / offset);
                break;

            case IIconic.ICON_POSITION_BOTTOM_LEFT:
                bottom = -(bounds.height / offset);
                left = bounds.width / offset;
                break;

            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                bottom = -(bounds.height / offset);
                right = -(bounds.width / offset);
                break;
        }
        drawIconImage(graphics, bounds, top, right, bottom, left);

        //Rectangle iconArea = new Rectangle(bounds.x + (bounds.width / 6), bounds.y + (bounds.height / 6),
        //        bounds.width - (bounds.width / 3), bounds.height - (bounds.height / 3));
        //drawIconImage(graphics, iconArea, 0, 0, 0, 0);

        graphics.popState();
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new EllipseAnchor(this);
    }

}
