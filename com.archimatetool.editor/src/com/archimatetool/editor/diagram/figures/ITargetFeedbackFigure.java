/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;



/**
 * Figure that shows target feedback
 * 
 * @author Phillip Beauvoir
 */
public interface ITargetFeedbackFigure extends IFigure {
    
    /**
     * Show some feedback
     */
    void showTargetFeedback();

    /**
     * Erase some feedback
     */
    void eraseTargetFeedback();
}
