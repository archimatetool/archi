/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.RepresentationFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Representation UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class RepresentationUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getRepresentation();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(RepresentationFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.RepresentationUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_REPRESENTATION);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_REPRESENTATION);
    }
    
    @Override
    public Color getDefaultColor() {
        return defaultBusinessColor;
    }
    
    @Override
    public boolean hasIcon() {
        return false;
    }

    @Override
    public boolean hasAlternateFigure() {
        return false;
    }
}
