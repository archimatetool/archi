/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.RectangleFigureDelegate;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
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
