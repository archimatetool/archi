/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.IFigureDelegate;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Figure for a Technology Device
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigure
extends AbstractTextFlowFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;
    
    public TechnologyDeviceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fFigureDelegate1 = new TechnologyDeviceFigureDelegate1(this);
        fFigureDelegate2 = new TechnologyDeviceFigureDelegate2(this);
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