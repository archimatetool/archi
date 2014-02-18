/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Technology Device
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigure
extends AbstractArchimateFigure {
    
    protected TechnologyDeviceFigureDelegate1 fFigureDelegate1;
    protected TechnologyDeviceFigureDelegate2 fFigureDelegate2;
    
    public TechnologyDeviceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new TechnologyDeviceFigureDelegate1(this);
        
        fFigureDelegate2 = new TechnologyDeviceFigureDelegate2(this);
        fFigureDelegate2.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DEVICE_16));
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