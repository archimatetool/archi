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
import org.eclipse.swt.SWT;


/**
 * Ellipse Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class EllipseFigureDelegate implements IFigureDelegate {
    
    protected int SHADOW_OFFSET = 2;
    
    protected IDiagramModelObjectFigure fOwner;
    
    public EllipseFigureDelegate(IDiagramModelObjectFigure owner) {
        fOwner = owner;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        Rectangle bounds = fOwner.getBounds().getCopy();

        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillOval(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));

        graphics.setAlpha(255);
        graphics.setBackgroundColor(fOwner.getFillColor());
        graphics.fillOval(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));

        // Outline
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawOval(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
    }
    
    @Override
    public void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = fOwner.getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawOval(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = fOwner.getBounds().getCopy();
        bounds.y += 10;
        return bounds;
    }
}
