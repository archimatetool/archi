/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Realisation Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class RealisationConnectionFigure extends AbstractDiagramConnectionFigure {
	
    public RealisationConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setScale(10, 7);
        decoration.setBackgroundColor(ColorConstants.white);
        setTargetDecoration(decoration);
        
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(new float[] { 4 });
    }
    

}
