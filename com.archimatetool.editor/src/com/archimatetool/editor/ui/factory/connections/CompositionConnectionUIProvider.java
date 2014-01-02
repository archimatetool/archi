/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.connections;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.connections.CompositionConnectionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Composition Connection UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CompositionConnectionUIProvider extends AbstractConnectionUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getCompositionRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new CompositionConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.CompositionConnectionUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.CompositionConnectionUIProvider_1;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_COMPOSITION_CONNECTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_COMPOSITION_CONNECTION_16);
    }
}
