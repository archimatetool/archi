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

import com.archimatetool.editor.diagram.editparts.technology.TechnologyArtifactEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Technology Artifact UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyArtifactUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getArtifact();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologyArtifactEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologyArtifactUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_ARTIFACT_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchimateImages.ICON_TECHNOLOGY_ARTIFACT_16);
    }
}
