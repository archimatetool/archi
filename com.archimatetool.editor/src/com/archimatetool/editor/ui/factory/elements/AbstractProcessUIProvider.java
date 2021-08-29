/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.gef.EditPart;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.ProcessFigure;



/**
 * Process UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractProcessUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(ProcessFigure.class);
    }

}
