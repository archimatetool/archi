/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;


/**
 * Figure with Editable Text Label and Text Flow Figure
 * 
 * @author Phillip Beauvoir
 */
public interface IEditableTextFlowFigure {
    
    /**
     * @return The Text Control
     */
    TextFlow getTextControl();

    /**
     * @param requestLoc
     * @return True if requestLoc is in the Editable Label
     */
    boolean didClickTextControl(Point requestLoc);

    /**
     * @return The Bounds
     */
    Rectangle calculateTextControlBounds();
}
