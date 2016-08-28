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
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Folder UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class FolderUIProvider extends AbstractObjectUIProvider {
    
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getFolder();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.FolderUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ECLIPSE_IMAGE_FOLDER);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_FOLDER);
    }
}
