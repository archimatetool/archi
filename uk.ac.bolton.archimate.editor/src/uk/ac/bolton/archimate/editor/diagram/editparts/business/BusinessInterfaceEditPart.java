/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.business;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessInterfaceFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessInterfaceFigure2;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;

/**
 * Business Interface Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceEditPart
extends AbstractArchimateEditableTextFlowEditPart {
    
    private ConnectionAnchor fAnchor;
    
    @Override
    protected IFigure createFigure() {
        int type = Preferences.STORE.getInt(IPreferenceConstants.BUSINESS_INTERFACE_FIGURE);
        return type == 0 ? new BusinessInterfaceFigure(getModel()) : new BusinessInterfaceFigure2(getModel());
    }
    
    @Override
    protected ConnectionAnchor getConnectionAnchor() {
        if(fAnchor == null) {
            int type = Preferences.STORE.getInt(IPreferenceConstants.BUSINESS_INTERFACE_FIGURE);
            if(type == 0) {
                fAnchor = new ChopboxAnchor(getFigure());
            }
            else {
                fAnchor = new EllipseAnchor(((IContainerFigure)getFigure()).getContentPane());
            }
        }
        return fAnchor;
    }

}