/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.technology;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.business.BusinessInterfaceEditPart;
import com.archimatetool.editor.diagram.figures.technology.TechnologyInfrastructureInterfaceFigure;


/**
 * Technology Infrastructure Interface Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInfrastructureInterfaceEditPart
extends BusinessInterfaceEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new TechnologyInfrastructureInterfaceFigure(getModel());
    }
}