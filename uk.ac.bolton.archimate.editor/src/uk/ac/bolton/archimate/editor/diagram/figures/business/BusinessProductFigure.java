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
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractRectangleFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProductFigure extends AbstractRectangleFigure {
    
    private int flangeFactor = 14;

    public BusinessProductFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(bounds.x, bounds.y, bounds.width / 2 + 1, flangeFactor);
        
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawRectangle(bounds.x, bounds.y, bounds.width / 2, flangeFactor - 1);
    }
    
    @Override
    protected Image getImage() {
        return null;
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = super.calculateTextControlBounds();
        bounds.y += flangeFactor - 4;
        bounds.height -= 10;
        return bounds;
    }
}
