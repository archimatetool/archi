/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractEllipseFigure;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Alternative Figure for a Business Interface
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceFigure2 extends AbstractEllipseFigure {
    
    static Dimension DEFAULT_SIZE = new Dimension(60, 60);
    
    public BusinessInterfaceFigure2(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }
    
    @Override
    protected Image getImage() {
        return null;
    }
}