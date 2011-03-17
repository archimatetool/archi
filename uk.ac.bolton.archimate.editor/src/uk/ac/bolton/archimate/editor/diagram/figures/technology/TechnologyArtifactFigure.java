/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractEditableTextFlowFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Technology Artifact Figure
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyArtifactFigure extends AbstractEditableTextFlowFigure {

    int FOLD_HEIGHT = 18;
    int SHADOW_OFFSET = 2;

    public TechnologyArtifactFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        Rectangle bounds = getBounds();
        
        // Main Shadow
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET + FOLD_HEIGHT, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET - FOLD_HEIGHT);

        // Fill
        int[] points1 = new int[] {
                bounds.x, bounds.y,
                bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y,
                bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + FOLD_HEIGHT,
                bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + bounds.height - SHADOW_OFFSET - 1,
                bounds.x, bounds.y + bounds.height - SHADOW_OFFSET - 1
        };
        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(points1);

        // Fold
        int[] points2 = new int[] {
                bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y,
                bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + FOLD_HEIGHT,
                bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT
        };
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
        
        // Line
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.drawPolygon(points1);
        graphics.drawPolygon(points2);
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRectangle(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
        graphics.popState();
    }

    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }

    @Override
    protected Image getImage() {
        return null;
    }

}
