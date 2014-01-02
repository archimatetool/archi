/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.business;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;
import com.archimatetool.editor.diagram.figures.business.BusinessProcessFigure;


/**
 * Business Process Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProcessEditPart
extends AbstractArchimateEditableTextFlowEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new BusinessProcessFigure(getModel());
    }
 
    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        switch(getModel().getType()) {
            case 1:
                return new ChopboxAnchor(getFigure());

            default:
                IRoundedRectangleFigure figureDelegate = (IRoundedRectangleFigure)((BusinessProcessFigure)getFigure()).getFigureDelegate();
                return new RoundedRectangleAnchor(getFigure(), figureDelegate.getArc());
        }
    }

}

