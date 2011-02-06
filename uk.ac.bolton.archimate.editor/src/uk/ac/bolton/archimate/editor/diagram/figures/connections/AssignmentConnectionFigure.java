/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;

/**
 * Assignment Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AssignmentConnectionFigure extends AbstractDiagramConnectionFigure {
	
    public AssignmentConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setSourceDecoration(new EndPoint());
        setTargetDecoration(new EndPoint()); 
    }
    

    class EndPoint extends PolygonDecoration {
        EndPoint() {
            setScale(2, 1.3);
            
            PointList decorationPointList = new PointList();
            decorationPointList.addPoint(0, 0);
            decorationPointList.addPoint(0, -1);
            decorationPointList.addPoint(-1, -1);
            decorationPointList.addPoint(-1, 0);
            decorationPointList.addPoint(-1, 1);
            decorationPointList.addPoint(0, 1);
            setTemplate(decorationPointList);
        }
    }
}
