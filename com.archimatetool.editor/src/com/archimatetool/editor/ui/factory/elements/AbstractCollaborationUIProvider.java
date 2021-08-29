/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.CollaborationFigure;



/**
 * Collaboration UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractCollaborationUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(CollaborationFigure.class);
    }

    @Override
    protected Dimension getDefaultSizeForFigureType(int figureType) {
        return super.getDefaultSizeForFigureType(figureType);
        //return figureType == 1 ? getDefaultSizeWithMinumumWidth(90) : super.getDefaultSizeForFigureType(figureType);
    }
}
