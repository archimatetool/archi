/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.technology;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyDeviceEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Technology Device UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDevice();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyDeviceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyDeviceUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_TECHNOLOGY_DEVICE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_TECHNOLOGY_DEVICE_16);
    }
}
