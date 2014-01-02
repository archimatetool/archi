/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.application;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Figure for an Application Component
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationComponentFigure
extends AbstractTextFlowFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected RectangleFigureDelegate fFigureDelegate2;
    
    
    public ApplicationComponentFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new ApplicationComponentFigureDelegate(this);
        
        fFigureDelegate2 = new RectangleFigureDelegate(this);
        fFigureDelegate2.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_COMPONENT_16));
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