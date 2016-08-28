/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.technology;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.technology.TechnologyCommunicationNetworkEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Technology Communication Network UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyCommunicationNetworkUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getCommunicationNetwork();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyCommunicationNetworkEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyCommunicationNetworkUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_COMMUNICATION_NETWORK_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_COMMUNICATION_NETWORK_16);
    }
}
