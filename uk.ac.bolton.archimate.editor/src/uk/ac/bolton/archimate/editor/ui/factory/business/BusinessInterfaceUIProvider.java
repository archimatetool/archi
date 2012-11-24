/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.business;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessInterfaceEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBusinessInterface;
import uk.ac.bolton.archimate.model.IInterfaceElement;


/**
 * Business Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceUIProvider extends AbstractBusinessUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getBusinessInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new BusinessInterfaceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.BusinessInterfaceUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.BusinessInterfaceUIProvider_1;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_BUSINESS_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_BUSINESS_INTERFACE_16);
    }
    
    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof IBusinessInterface) {
            int type = ((IBusinessInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_REQUIRED_16);
            }
        }
        
        return getImage();
    }
}
