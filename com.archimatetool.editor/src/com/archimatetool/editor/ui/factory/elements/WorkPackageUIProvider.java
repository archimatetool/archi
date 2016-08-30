/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.elements.WorkPackageEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Work Package UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class WorkPackageUIProvider extends AbstractArchimateElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getWorkPackage();
    }
    
    @Override
    public EditPart createEditPart() {
        return new WorkPackageEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.WorkPackageUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchiImages.ICON_WORKPACKAGE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchiImages.ICON_WORKPACKAGE_16);
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 224, 224);
    }
}
