/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.application;

import com.archimatetool.editor.diagram.figures.AbstractTextFlowFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IArchimateImages;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Figure for an Application Collaboration
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationCollaborationFigure
extends AbstractTextFlowFigure {
    
    public ApplicationCollaborationFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this);
        figureDelegate.setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_COLLABORATION_16));
        setFigureDelegate(figureDelegate);
    }
}