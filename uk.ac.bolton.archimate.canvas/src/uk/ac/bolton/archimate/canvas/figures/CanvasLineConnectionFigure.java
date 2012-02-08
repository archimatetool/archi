/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.figures;

import uk.ac.bolton.archimate.canvas.model.ICanvasModelConnection;
import uk.ac.bolton.archimate.editor.diagram.figures.diagram.LineConnectionFigure;

/**
 * Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasLineConnectionFigure
extends LineConnectionFigure {

    public CanvasLineConnectionFigure(ICanvasModelConnection connection) {
        super(connection);
    }
    
}
