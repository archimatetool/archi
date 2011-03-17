/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Ellipse Figure
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractEllipseFigure extends AbstractEditableTextFlowFigure implements HandleBounds {
    
    protected int SHADOW_OFFSET = 2;
    
    protected AbstractEllipseFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        Rectangle bounds = getHandleBounds().getCopy();

        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillOval(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));

        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillOval(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));

        // Outline
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawOval(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getHandleBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawOval(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.y += 10;
        return bounds;
    }
    
    @Override
    public Rectangle getHandleBounds() {
        Rectangle bounds = getMainFigure().getBounds().getCopy();
        translateToParent(bounds);
        return bounds;
    }
}
