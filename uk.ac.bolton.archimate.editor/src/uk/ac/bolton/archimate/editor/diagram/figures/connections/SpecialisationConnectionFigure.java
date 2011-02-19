/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;


/**
 * Specialisation Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class SpecialisationConnectionFigure extends AbstractArchimateConnectionFigure {
	
    public SpecialisationConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setScale(10, 7);
        decoration.setBackgroundColor(ColorConstants.white);
        setTargetDecoration(decoration);
    }
    

}
