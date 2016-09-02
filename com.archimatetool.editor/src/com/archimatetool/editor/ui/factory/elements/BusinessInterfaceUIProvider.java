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
import com.archimatetool.model.IBusinessInterface;
import com.archimatetool.model.IInterfaceElement;



/**
 * Business Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceUIProvider extends AbstractArchimateElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getBusinessInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(InterfaceFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.BusinessInterfaceUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchiImages.ICON_BUSINESS_INTERFACE);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchiImages.ICON_BUSINESS_INTERFACE);
    }
    
    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof IBusinessInterface) {
            int type = ((IBusinessInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_INTERFACE_REQUIRED);
            }
        }
        
        return getImage();
    }
    
    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 255, 181);
    }

    @Override
    public boolean hasAlternateFigure() {
        return true;
    }
}
