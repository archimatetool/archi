/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.InterfaceFigure;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInterfaceElement;
import com.archimatetool.model.ITechnologyInterface;



/**
 * Technology Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInterfaceUIProvider extends AbstractArchimateElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getTechnologyInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(InterfaceFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyInterfaceUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchiImages.ICON_TECHNOLOGY_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchiImages.ICON_TECHNOLOGY_INTERFACE_16);
    }
    
    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof ITechnologyInterface) {
            int type = ((ITechnologyInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_INTERFACE_REQUIRED_16);
            }
        }
        
        return getImage();
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(201, 231, 183);
    }

    @Override
    public boolean hasAlternateFigure() {
        return true;
    }
}
