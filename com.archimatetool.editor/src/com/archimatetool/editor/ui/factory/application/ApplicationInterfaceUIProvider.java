/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.application;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.application.ApplicationInterfaceEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IApplicationInterface;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInterfaceElement;



/**
 * Application Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationInterfaceUIProvider extends AbstractApplicationUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getApplicationInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ApplicationInterfaceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.ApplicationInterfaceUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.ApplicationInterfaceUIProvider_1;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_APPLICATION_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_APPLICATION_INTERFACE_16);
    }

    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof IApplicationInterface) {
            int type = ((IApplicationInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_REQUIRED_16);
            }
        }
        
        return getImage();
    }
}
