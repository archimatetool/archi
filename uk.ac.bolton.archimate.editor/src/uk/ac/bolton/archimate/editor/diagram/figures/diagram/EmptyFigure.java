/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractRectangleFigure;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Empty Figure
 * 
 * @author Phillip Beauvoir
 */
public class EmptyFigure
extends AbstractRectangleFigure {
    
    public EmptyFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public Color getFillColor() {
        return ColorConstants.white;
    }

    @Override
    public Image getImage() {
        return null;
    }
}