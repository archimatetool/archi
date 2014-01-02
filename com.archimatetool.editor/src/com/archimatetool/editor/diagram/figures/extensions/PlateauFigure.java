/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.extensions;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.technology.TechnologyDeviceFigureDelegate2;
import com.archimatetool.editor.ui.IArchimateImages;

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
