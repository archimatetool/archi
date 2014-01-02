/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;



/**
 * Figure Delegate that takes care of drawing the figure
 * 
 * @author Phillip Beauvoir
 */
public interface IFigureDelegate {
    
    /**
     * Draw the figure
     * @param graphics
     */
    void drawFigure(Graphics graphics);
    
    /**
     * @return The bounds of the Text Control or null if none
     */
    Rectangle calculateTextControlBounds();
    
    /**
     * Draw any target feedback, if any
     * @param graphics
     */
    void drawTargetFeedback(Graphics graphics);
    
    /**
     * Set whether to draw the figure in an enabled state
     * @param value
     */
    void setEnabled(boolean value);
    
    /**
     * @return The enabled state
     */
    boolean isEnabled();
}
