/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;


/**
 * Abstract Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class AbstractFigureDelegate implements IFigureDelegate {
    
    private IDiagramModelObjectFigure fOwner;
    
    private boolean fIsEnabled;
    
    protected AbstractFigureDelegate(IDiagramModelObjectFigure owner) {
        fOwner = owner;
    }
    
    protected IDiagramModelObjectFigure getOwner() {
        return fOwner;
    }

    @Override
    public void drawFigure(Graphics graphics) {
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        return null;
    }

    @Override
    public void drawTargetFeedback(Graphics graphics) {
    }

    @Override
    public void setEnabled(boolean value) {
        fIsEnabled = value;
    }
    
    @Override
    public boolean isEnabled() {
        return fIsEnabled;
    }
    
    /**
     * @return A copy of the owner's bounds
     */
    protected Rectangle getBounds() {
        return getOwner().getBounds().getCopy();
    }
    
    /**
     * @return The owner's fill color
     */
    protected Color getFillColor() {
        return getOwner().getFillColor();
    }
    
    /**
     * @return The owner's line color
     */
    protected Color getLineColor() {
        return getOwner().getLineColor();
    }

    /**
     * Set the drawing state when disabled
     * @param graphics
     */
    protected void setDisabledState(Graphics graphics) {
        graphics.setAlpha(100);
        graphics.setLineStyle(SWT.LINE_DOT);
    }
}
