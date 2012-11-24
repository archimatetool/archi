/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.application;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationInterfaceEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IApplicationInterface;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IInterfaceElement;


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
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_APPLICATION_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_APPLICATION_INTERFACE_16);
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
