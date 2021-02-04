/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;


/**
 * Abstract Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class AbstractFigureDelegate implements IFigureDelegate {
    
    private AbstractDiagramModelObjectFigure fOwner;
    
    private boolean fIsEnabled = true;
    
    protected AbstractFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        fOwner = owner;
    }
    
    protected AbstractDiagramModelObjectFigure getOwner() {
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
     * @return The owner's alpha fill transparency
     */
    protected int getAlpha() {
        return getOwner().getAlpha();
    }

    /**
     * @return The owner's alpha line transparency
     */
    protected int getLineAlpha() {
        return getOwner().getLineAlpha();
    }
    
    /**
     * @return The owner's gradient setting
     */
    protected int getGradient() {
        return getOwner().getGradient();
    }

    /**
     * Set the drawing state when disabled
     * @param graphics
     */
    protected void setDisabledState(Graphics graphics) {
        getOwner().setDisabledState(graphics);
    }
    
    /**
     * Set the line width and compensate the figure bounds width and height for this line width and translate the graphics instance
     * @param graphics The graphics instance
     * @param lineWidth The line width
     * @param bounds The bounds of the object
     */
    protected void setLineWidth(Graphics graphics, int lineWidth, Rectangle bounds) {
        getOwner().setLineWidth(graphics, lineWidth, bounds);
    }
    
    /**
     * Apply a gradient to the given Graphics instance and bounds using current fill color, alpha and gradient settings
     */
    protected Pattern applyGradientPattern(Graphics graphics, Rectangle bounds) {
        return getOwner().applyGradientPattern(graphics, bounds);
    }
    
    /**
     * Dispose the given gradient if not null
     */
    protected void disposeGradientPattern(Graphics graphics, Pattern gradient) {
        getOwner().disposeGradientPattern(graphics, gradient);
    }

}
