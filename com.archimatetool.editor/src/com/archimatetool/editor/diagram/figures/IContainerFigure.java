/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Translatable;



/**
 * Figure that is a container for children
 * 
 * @author Phillip Beauvoir
 */
public interface IContainerFigure extends ITargetFeedbackFigure {
    
    /**
     * @return The Figure into which childrens' Figures will be added. 
     */
    IFigure getContentPane();
    
    /**
     * Translate a mouse drop point to relative co-ords
     * @param t
     */
    void translateMousePointToRelative(Translatable t);
}
