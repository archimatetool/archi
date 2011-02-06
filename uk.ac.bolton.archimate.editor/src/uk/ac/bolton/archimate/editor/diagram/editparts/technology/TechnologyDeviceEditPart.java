/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.technology;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.technology.TechnologyDeviceFigure1;
import uk.ac.bolton.archimate.editor.diagram.figures.technology.TechnologyDeviceFigure2;
import uk.ac.bolton.archimate.editor.preferences.Preferences;

/**
 * Technology Device Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceEditPart
extends AbstractArchimateEditableTextFlowEditPart {            
    
    @Override
    protected IFigure createFigure() {
        int type = Preferences.getTechnologyDeviceFigureType();
        return type == 0 ? new TechnologyDeviceFigure1(getModel())
                         : new TechnologyDeviceFigure2(getModel());
    }

}