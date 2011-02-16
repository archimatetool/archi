/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolylineDecoration;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;


/**
 * Used By Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class UsedByConnectionFigure extends AbstractArchimateConnectionFigure {
	
    public UsedByConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(new PolylineDecoration()); // arrow at target endpoint 
    }
    

}
