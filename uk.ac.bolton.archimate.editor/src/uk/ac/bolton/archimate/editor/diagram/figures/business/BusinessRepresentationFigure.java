/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Representation Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessRepresentationFigure extends AbstractTextFlowFigure {

    protected int SHADOW_OFFSET = 3;
    
    public BusinessRepresentationFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        int offset = 6;
        int curve_y = bounds.y + bounds.height - offset;
        
        if(isEnabled()) {
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            
            // Shadow fill
            Path path = new Path(null);
            path.moveTo(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET);
            path.lineTo(bounds.x + SHADOW_OFFSET, curve_y);

            path.quadTo(bounds.x + SHADOW_OFFSET + (bounds.width / 4), bounds.y + bounds.height + offset - 2,
                    bounds.x + bounds.width / 2 + SHADOW_OFFSET, curve_y);

            path.quadTo(bounds.x + bounds.width - (bounds.width / 4), curve_y - offset - 2,
                    bounds.x + bounds.width, curve_y);

            path.lineTo(bounds.x + bounds.width, bounds.y + SHADOW_OFFSET);
            graphics.fillPath(path);
            path.dispose();
            
            graphics.setAlpha(255);
        }
        else {
            setDisabledState(graphics);
        }
        
        // Main Fill
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x, curve_y - SHADOW_OFFSET);
        
        path.quadTo(bounds.x + (bounds.width / 4), bounds.y + bounds.height + offset - SHADOW_OFFSET,
                bounds.x + bounds.width / 2 + SHADOW_OFFSET, curve_y - SHADOW_OFFSET);
        
        path.quadTo(bounds.x + bounds.width - (bounds.width / 4), curve_y - offset - 2,
                bounds.x + bounds.width - SHADOW_OFFSET, curve_y - SHADOW_OFFSET);
        
        path.lineTo(bounds.x + bounds.width - SHADOW_OFFSET, bounds.y);
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPath(path);
        
        // Outline
        graphics.setForegroundColor(ColorConstants.black);
        path.lineTo(bounds.x, bounds.y);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }
}
