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

import com.archimatetool.editor.diagram.editparts.technology.TechnologyFunctionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Technology Function UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyFunctionUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getTechnologyFunction();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyFunctionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyFunctionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_FUNCTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_INFRASTRUCTURE_FUNCTION_16);
    }
}
