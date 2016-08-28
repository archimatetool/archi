/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.technology;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.technology.TechnologyInterfaceEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInterfaceElement;
import com.archimatetool.model.ITechnologyInterface;



/**
 * Technology Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInterfaceUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getTechnologyInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyInterfaceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyInterfaceUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_INTERFACE_16);
    }
    
    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof ITechnologyInterface) {
            int type = ((ITechnologyInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_REQUIRED_16);
            }
        }
        
        return getImage();
    }

}
