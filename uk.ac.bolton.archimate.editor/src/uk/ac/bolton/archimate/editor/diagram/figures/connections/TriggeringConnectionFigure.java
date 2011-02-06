/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolygonDecoration;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;

/**
 * Triggering Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class TriggeringConnectionFigure extends AbstractDiagramConnectionFigure {
	
    public TriggeringConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(new PolygonDecoration()); 
    }
    

}
