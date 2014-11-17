/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.editparts.CanvasLineConnectionEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.factory.diagram.LineConnectionUIProvider;



/**
 * Canvas Line Connection UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasLineConnectionUIProvider extends LineConnectionUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelConnection();
    }
    
    @Override
    public EditPart createEditPart() {
        return new CanvasLineConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.CanvasLineConnectionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_CONNECTION_ARROW_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_CONNECTION_ARROW_16);
    }
}
