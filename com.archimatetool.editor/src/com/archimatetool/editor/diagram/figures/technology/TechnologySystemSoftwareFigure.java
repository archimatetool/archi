/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Technology System Software Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class TechnologySystemSoftwareFigure
extends AbstractTextFlowFigure {

    public TechnologySystemSoftwareFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this);
        figureDelegate.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_SYSTEM_SOFTWARE_16));
        setFigureDelegate(figureDelegate);
    }
}
