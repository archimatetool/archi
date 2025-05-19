/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;



/**
 * Figure that supports selected status
 * 
 * @author Phillip Beauvoir
 */
public interface ISelectableFigure extends IFigure {
    
    /**
     * Set selected status on this figure
     * @param selected
     */
    default void setSelected(boolean selected) {
    }
}
