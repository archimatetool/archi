/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.application;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.application.ApplicationInteractionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Application Interaction UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationInteractionUIProvider extends AbstractApplicationUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getApplicationInteraction();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ApplicationInteractionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.ApplicationInteractionUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.ApplicationInteractionUIProvider_1;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_APPLICATION_INTERACTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_APPLICATION_INTERACTION_16);
    }
}
