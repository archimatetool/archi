/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.application;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.editparts.business.BusinessInterfaceEditPart;
import com.archimatetool.editor.diagram.figures.application.ApplicationInterfaceFigure;


/**
 * Application Interface Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationInterfaceEditPart
extends BusinessInterfaceEditPart {            
    
    @Override
    protected IFigure createFigure() {
        return new ApplicationInterfaceFigure(getModel());
    }
}