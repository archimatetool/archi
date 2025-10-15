/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.gef.EditPart;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.ServiceFigure;
import com.archimatetool.editor.ui.IIconDelegate;



/**
 * Service UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractServiceUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(ServiceFigure.class);
    }
    
    @Override
    public IIconDelegate getIconDelegate() {
        return ServiceFigure.getIconDelegate();
    }
}
