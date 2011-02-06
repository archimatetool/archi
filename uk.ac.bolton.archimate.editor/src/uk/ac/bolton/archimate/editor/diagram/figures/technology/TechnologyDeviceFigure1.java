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
 * Technology Device Figure
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigure1 extends AbstractEditableTextFlowFigure {

    int SHADOW_OFFSET = 2;
    int INDENT = 15;

    public TechnologyDeviceFigure1(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        Rectangle bounds = getBounds();
        
        int height_indent = bounds.height / 6;
        
        // Main Shadow
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET,
                bounds.width - SHADOW_OFFSET, bounds.height - height_indent - SHADOW_OFFSET), 30, 30);

        int[] points1 = new int[] {
                bounds.x + SHADOW_OFFSET, bounds.y + bounds.height,
                bounds.x + INDENT, bounds.y + bounds.height - height_indent - 3,
                bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 3,
                bounds.x + bounds.width, bounds.y + bounds.height
        };
        graphics.fillPolygon(points1);
        
        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y,
                bounds.width - SHADOW_OFFSET, bounds.height - height_indent - SHADOW_OFFSET), 30, 30);
    
        int[] points2 = new int[] {
                bounds.x, bounds.y + bounds.height - SHADOW_OFFSET,
                bounds.x + INDENT - SHADOW_OFFSET, bounds.y + bounds.height - height_indent - 3,
                bounds.x + bounds.width - INDENT - SHADOW_OFFSET, bounds.y + bounds.height - height_indent - 3,
                bounds.x + bounds.width - 4, bounds.y + bounds.height - SHADOW_OFFSET
        };
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);

        graphics.setBackgroundColor(ColorConstants.black);
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y,
                bounds.width - 3, bounds.height - height_indent - 3), 30, 30);
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
        
        translateFromParent(bounds);
        
        return bounds;
    }

    @Override
    protected Image getImage() {
        return null;
    }

}
