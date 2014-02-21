/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Business Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessServiceFigure
extends AbstractArchimateFigure
implements IRoundedRectangleFigure {
    
    // This figure needs to implement IRoundedRectangleFigure and return getArc() from the delegate
    // I tried it by directly getting the delegate figure in the EditPart but BusinessServiceFigureDelegate
    // Needs time to calculate its bounds

    protected BusinessServiceFigureDelegate fFigureDelegate1;
    protected RoundedRectangleFigureDelegate fFigureDelegate2;

    public BusinessServiceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);

        fFigureDelegate1 = new BusinessServiceFigureDelegate(this);
        
        fFigureDelegate2 = new RoundedRectangleFigureDelegate(this);
        fFigureDelegate2.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_SERVICE_16));
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

    @Override
    public Dimension getArc() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? fFigureDelegate1.getArc() : fFigureDelegate2.getArc();
    }

    @Override
    public void setArc(Dimension arc) {
    }
}
