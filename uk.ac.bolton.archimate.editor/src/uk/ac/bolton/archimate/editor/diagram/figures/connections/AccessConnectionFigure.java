/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;

/**
 * Access Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AccessConnectionFigure extends AbstractDiagramConnectionFigure {
	
	public AccessConnectionFigure(IDiagramModelConnection connection) {
	    super(connection);
	}
	
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(new PolylineDecoration()); // arrow at target endpoint
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(new float[] { 4 });
    }
    

}
