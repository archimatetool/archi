/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * Figure with Editable Text Label
 * 
 * @author Phillip Beauvoir
 */
public interface IEditableLabelFigure extends IDiagramModelObjectFigure {
    
    /**
     * @return The Text Label
     */
    Label getLabel();

    /**
     * @param requestLoc
     * @return True if requestLoc is in the Editable Label
     */
    boolean didClickLabel(Point requestLoc);

    /**
     * @return Label Bounds
     */
    Rectangle calculateLabelBounds();
}
