/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.extensions;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.technology.TechnologyDeviceFigureDelegate2;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Plateau Figure
 * 
 * @author Phillip Beauvoir
 */
public class PlateauFigure
extends AbstractTextFlowFigure {
    
    public PlateauFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        TechnologyDeviceFigureDelegate2 figureDelegate = new TechnologyDeviceFigureDelegate2(this);
        figureDelegate.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_PLATEAU_16));
        setFigureDelegate(figureDelegate);
    }
}
