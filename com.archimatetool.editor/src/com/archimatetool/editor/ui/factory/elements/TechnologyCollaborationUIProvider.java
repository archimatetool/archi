/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Technology Collaboration UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyCollaborationUIProvider extends AbstractCollaborationUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getTechnologyCollaboration();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.TechnologyCollaborationUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_TECHNOLOGY_COLLABORATION);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_TECHNOLOGY_COLLABORATION);
    }
    
    @Override
    public Color getDefaultColor() {
        return defaultTechnologyColor;
    }
}
