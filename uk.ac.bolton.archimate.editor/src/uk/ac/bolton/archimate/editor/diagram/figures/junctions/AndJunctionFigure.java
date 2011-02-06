/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.junctions;

import org.eclipse.draw2d.ColorConstants;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;


/**
 * And Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class AndJunctionFigure extends OrJunctionFigure {
    
    public AndJunctionFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        setBackgroundColor(ColorConstants.black);
    }

}
