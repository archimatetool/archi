/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.business;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import com.archimatetool.editor.diagram.figures.business.BusinessValueFigure;


/**
 * Business Value Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class BusinessValueEditPart
extends AbstractArchimateEditableTextFlowEditPart {
    
    @Override
    protected IFigure createFigure() {
        return new BusinessValueFigure(getModel());
    }
 
    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        return new EllipseAnchor(getFigure());
    }
}