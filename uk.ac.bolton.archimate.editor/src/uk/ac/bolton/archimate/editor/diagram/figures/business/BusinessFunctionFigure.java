/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractRoundedRectangleFigure;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessFunctionFigure extends AbstractRoundedRectangleFigure {

    public BusinessFunctionFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_FUNCTION_16);
    }
}
