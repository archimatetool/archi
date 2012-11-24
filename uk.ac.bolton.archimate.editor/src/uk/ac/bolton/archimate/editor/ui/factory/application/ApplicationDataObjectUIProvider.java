/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.application;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationDataObjectEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Application DataObject UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationDataObjectUIProvider extends AbstractApplicationUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDataObject();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ApplicationDataObjectEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.ApplicationDataObjectUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_APPLICATION_DATA_OBJECT_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_APPLICATION_DATA_OBJECT_16);
    }
}
