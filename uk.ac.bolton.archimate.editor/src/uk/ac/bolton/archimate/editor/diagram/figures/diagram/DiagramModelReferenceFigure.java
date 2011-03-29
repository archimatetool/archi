/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.RectangleFigureDelegate;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDiagramModelReference;

/**
 * Figure for a Diagram Model Reference Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceFigure
extends AbstractTextFlowFigure {
    
    public DiagramModelReferenceFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this) {
            @Override
            public Image getImage() {
                switch(((IDiagramModelReference)getDiagramModelObject()).getReferencedModel().eClass().getClassifierID()) {
                    case IArchimatePackage.DIAGRAM_MODEL:
                        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DIAGRAM_16);
                        
                    case IArchimatePackage.SKETCH_MODEL:
                        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_SKETCH_16);

                    default:
                        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DIAGRAM_16);
                }
            }
        };
        
        setFigureDelegate(figureDelegate);
    }

    @Override
    protected void setToolTip() {
        super.setToolTip();
        if(getToolTip() != null) {
            ((ToolTipFigure)getToolTip()).setType("Type: View Reference");
        }
    }
}