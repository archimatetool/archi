/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Technology Node
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyNodeFigure
extends AbstractArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected RectangleFigureDelegate fFigureDelegate2;
    
    public TechnologyNodeFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new TechnologyNodeFigureDelegate(this);
        
        fFigureDelegate2 = new RectangleFigureDelegate(this);
        fFigureDelegate2.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_NODE_16));
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        repaint(); // redraw delegate
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }
}