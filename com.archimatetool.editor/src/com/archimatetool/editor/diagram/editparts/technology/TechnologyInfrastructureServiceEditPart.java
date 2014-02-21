/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.technology;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.business.BusinessServiceEditPart;
import com.archimatetool.editor.diagram.figures.technology.TechnologyInfrastructureServiceFigure;


/**
 * Technology Infrastructure Service Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInfrastructureServiceEditPart
extends BusinessServiceEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new TechnologyInfrastructureServiceFigure(getModel());
    }
 
}