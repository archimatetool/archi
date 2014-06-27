/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.technology;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.business.BusinessFunctionEditPart;
import com.archimatetool.editor.diagram.figures.technology.TechnologyFunctionFigure;


/**
 * Technology Function Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInfrastructureFunctionEditPart
extends BusinessFunctionEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new TechnologyFunctionFigure(getModel());
    }
 
}