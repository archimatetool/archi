/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.application;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.application.ApplicationComponentFigure1;
import uk.ac.bolton.archimate.editor.diagram.figures.application.ApplicationComponentFigure2;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;

/**
 * Application Component Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationComponentEditPart
extends AbstractArchimateEditableTextFlowEditPart {
    
    @Override
    protected IFigure createFigure() {
        int type = Preferences.STORE.getInt(IPreferenceConstants.APPLICATION_COMPONENT_FIGURE);
        return type == 0 ? new ApplicationComponentFigure1(getModel())
                         : new ApplicationComponentFigure2(getModel());
    }
    
}