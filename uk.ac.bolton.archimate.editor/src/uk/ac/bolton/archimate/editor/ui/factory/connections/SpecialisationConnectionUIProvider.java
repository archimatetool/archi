/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.connections;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.connections.SpecialisationConnectionEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Specialisation Connection UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SpecialisationConnectionUIProvider extends AbstractConnectionUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSpecialisationRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new SpecialisationConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.SpecialisationConnectionUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.SpecialisationConnectionUIProvider_1;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_SPECIALISATION_CONNECTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_SPECIALISATION_CONNECTION_16);
    }
}
