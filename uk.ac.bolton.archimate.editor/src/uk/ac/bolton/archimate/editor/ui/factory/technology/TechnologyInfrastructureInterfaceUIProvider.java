/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.technology;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyInfrastructureInterfaceEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IInfrastructureInterface;
import uk.ac.bolton.archimate.model.IInterfaceElement;


/**
 * Technology Infrastructure Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyInfrastructureInterfaceUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getInfrastructureInterface();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyInfrastructureInterfaceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyInfrastructureInterfaceUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_INTERFACE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_INTERFACE_16);
    }
    
    @Override
    public Image getImage(EObject instance) {
        // Interface Element Types
        if(instance instanceof IInfrastructureInterface) {
            int type = ((IInfrastructureInterface)instance).getInterfaceType();
            if(type == IInterfaceElement.REQUIRED) {
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_INTERFACE_REQUIRED_16);
            }
        }
        
        return getImage();
    }

}
