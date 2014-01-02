/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.application;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.application.ApplicationServiceFigure;


/**
 * Application Service Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationServiceEditPart
extends AbstractArchimateEditableTextFlowEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new ApplicationServiceFigure(getModel());
    }
 
    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        return new RoundedRectangleAnchor(getFigure());
    }
}