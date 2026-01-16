/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;

/**
 * Interface to act as callback for Figure operations such as paint
 * 
 * @author Phillip Beauvoir
 */
public interface IFigureCallback {
    
    enum FigureEvent {
        BEFORE_PAINT, // The IFigure is about to be painted in its paintFigure(Graphics) method
        AFTER_PAINT   // Future extension
    }
    
    /**
     * The IFigure is about to be painted in its paintFigure(Graphics) method
     * @param figure The figure that is about to be painted
     */
    void onFigureEvent(IFigure figure, FigureEvent event);
    
}
