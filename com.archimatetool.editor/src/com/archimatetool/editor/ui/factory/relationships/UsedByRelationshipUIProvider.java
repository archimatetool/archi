/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.relationships;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.connections.UsedByConnectionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * UsedBy Relationship UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class UsedByRelationshipUIProvider extends AbstractRelationshipUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getUsedByRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new UsedByConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.UsedByRelationshipUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_USED_BY_CONNECTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_USED_BY_CONNECTION_16);
    }
}
