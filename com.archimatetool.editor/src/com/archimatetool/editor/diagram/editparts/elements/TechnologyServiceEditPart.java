/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.elements;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.figures.elements.TechnologyServiceFigure;


/**
 * Technology Service Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyServiceEditPart
extends BusinessServiceEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new TechnologyServiceFigure(getModel());
    }
 
}