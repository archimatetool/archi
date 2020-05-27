/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;

/**
 * Figure that has text control
 * 
 * @author Phillip Beauvoir
 */
public interface ITextFigure extends IFigure {
    
    /**
     * Set the text in the figure
     * This will refresh the figure's text from diagram model object
     */
    void setText();

}
