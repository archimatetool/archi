/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.application;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Figure for an Application Component
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationComponentFigure1 extends AbstractTextFlowFigure {
    
    protected int SHADOW_OFFSET = 2;
    protected int INDENT = 10;
    protected int TEXT_INDENT = 25;
    
    public ApplicationComponentFigure1(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.setAlpha(100);
        
        // Main Shadow
        graphics.fillRectangle(bounds.x + INDENT + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET - INDENT, bounds.height - SHADOW_OFFSET);

        // Nubs Shadow
        graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + 10 + SHADOW_OFFSET, INDENT, 12);
        graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + 30 + SHADOW_OFFSET, INDENT, 12);
        
        // Main Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(255);
        graphics.fillRectangle(bounds.x + INDENT, bounds.y, bounds.width - SHADOW_OFFSET - INDENT, bounds.height - SHADOW_OFFSET);
        
        // Outline
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawRectangle(bounds.x + INDENT, bounds.y, bounds.width - SHADOW_OFFSET - 1 - INDENT, bounds.height - SHADOW_OFFSET - 1);
        
        // Nubs Fill
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(bounds.x, bounds.y + 10, INDENT * 2 + 1, 13);
        graphics.fillRectangle(bounds.x, bounds.y + 30, INDENT * 2 + 1, 13);
        
        // Nubs Outline
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawRectangle(bounds.x, bounds.y + 10, INDENT * 2, 12);
        graphics.drawRectangle(bounds.x, bounds.y + 30, INDENT * 2, 12);
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        Rectangle bounds = getBounds().getCopy();
        graphics.drawRectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1);
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width -= 35;
        bounds.height -= 10;
        return bounds;
    }
}