/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.bolton.archimate.model.IDiagramModelObject;



/**
 * Abstract Figure
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractRoundedRectangleFigure extends AbstractEditableTextFlowFigure {

    protected int ARC = 20;
    protected int SHADOW_OFFSET = 2;
    protected int TEXT_INDENT = 20;
    
    protected AbstractRoundedRectangleFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET),
                ARC, ARC);

        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET), ARC, ARC);
        
        // Outline
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1),
                ARC, ARC);

        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRoundRectangle(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1),
                ARC, ARC);
        graphics.popState();
    }
    
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width = bounds.width - (TEXT_INDENT * 2);
        bounds.height -= 10;
        return bounds;
    }
    
    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - TEXT_INDENT - 1, bounds.y + 5);
    }
}
