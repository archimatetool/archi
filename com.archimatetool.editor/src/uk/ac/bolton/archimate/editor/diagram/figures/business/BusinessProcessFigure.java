/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IFigureDelegate;
import uk.ac.bolton.archimate.editor.diagram.figures.RoundedRectangleFigureDelegate;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Business Process Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProcessFigure
extends AbstractTextFlowFigure {
    
    protected RoundedRectangleFigureDelegate fFigureDelegate1;
    protected BusinessProcessFigureDelegate fFigureDelegate2;

    public BusinessProcessFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new RoundedRectangleFigureDelegate(this);
        fFigureDelegate1.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_PROCESS_16));
        
        fFigureDelegate2 = new BusinessProcessFigureDelegate(this);
    }

    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        repaint(); // redraw delegate
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = ((IDiagramModelArchimateObject)getDiagramModelObject()).getType();
        return type == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }

}
