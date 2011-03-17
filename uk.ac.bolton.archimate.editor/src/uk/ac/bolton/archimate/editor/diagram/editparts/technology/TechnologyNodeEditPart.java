/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.technology;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.technology.TechnologyNodeFigure1;
import uk.ac.bolton.archimate.editor.diagram.figures.technology.TechnologyNodeFigure2;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;

/**
 * Technology Node Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyNodeEditPart
extends AbstractArchimateEditableTextFlowEditPart {            
    
    @Override
    protected IFigure createFigure() {
        int type = Preferences.STORE.getInt(IPreferenceConstants.TECHNOLOGY_NODE_FIGURE);
        return type == 0 ? new TechnologyNodeFigure1(getModel())
                         : new TechnologyNodeFigure2(getModel());
    }

}