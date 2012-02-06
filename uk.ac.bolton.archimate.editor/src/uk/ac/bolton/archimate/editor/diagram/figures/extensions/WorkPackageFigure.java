/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.extensions;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.RoundedRectangleFigureDelegate;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 */
public class WorkPackageFigure
extends AbstractTextFlowFigure {

    public WorkPackageFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        RoundedRectangleFigureDelegate figureDelegate = new RoundedRectangleFigureDelegate(this);
        figureDelegate.setArc(10);
        setFigureDelegate(figureDelegate);
    }
}
