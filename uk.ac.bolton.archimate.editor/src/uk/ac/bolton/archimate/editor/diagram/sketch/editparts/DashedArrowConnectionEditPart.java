/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AbstractDiagramConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.sketch.figures.DashedArrowConnectionFigure;


/**
 * DashedArrowConnection EditPart
 * 
 * @author Phillip Beauvoir
 */
public class DashedArrowConnectionEditPart extends AbstractDiagramConnectionEditPart {

    @Override
    protected IFigure createFigure() {
        return new DashedArrowConnectionFigure(getModel());
    }

}
