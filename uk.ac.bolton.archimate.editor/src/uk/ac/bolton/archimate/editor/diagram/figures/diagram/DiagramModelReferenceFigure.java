/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractRectangleFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Figure for a Business Actor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceFigure
extends AbstractRectangleFigure {
    
    public DiagramModelReferenceFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DIAGRAM_16);
    }
    
    @Override
    protected void setToolTip() {
        super.setToolTip();
        if(getToolTip() != null) {
            ((ToolTipFigure)getToolTip()).setType("Type: View Reference");
        }
    }

}