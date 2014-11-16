/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.factory.AbstractElementUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Archimate Model UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelUIProvider extends AbstractElementUIProvider {
    
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getArchimateModel();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.ArchimateModelUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_MODELS_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_MODELS_16);
    }
}
