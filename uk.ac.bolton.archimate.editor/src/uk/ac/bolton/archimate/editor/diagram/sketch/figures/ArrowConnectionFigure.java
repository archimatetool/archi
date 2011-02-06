/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.figures;

import org.eclipse.draw2d.PolygonDecoration;

import uk.ac.bolton.archimate.editor.diagram.figures.connections.AbstractDiagramConnectionFigure;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * ArrowConnectionFigure
 * 
 * @author Phillip Beauvoir
 */
public class ArrowConnectionFigure extends AbstractDiagramConnectionFigure {

    public ArrowConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
    
    @Override
    protected void setFigureProperties() {
        PolygonDecoration poly = new PolygonDecoration();
        poly.setScale(10, 6);
        setTargetDecoration(poly); // arrow at target endpoint
    }
}
