/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.figures;

import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Dashed Arrow ConnectionFigure
 * 
 * @author Phillip Beauvoir
 */
public class DashedArrowConnectionFigure extends ArrowConnectionFigure {

    public DashedArrowConnectionFigure(IDiagramModelConnection connection) {
        super(connection);
    }
    
    @Override
    protected void setFigureProperties() {
        super.setFigureProperties();
        setLineStyle(SWT.LINE_CUSTOM); // We have to explitly set this otherwise dashes/dots don't show
        setLineDash(new float[] { 6, 3 });
    }
}
