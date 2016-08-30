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

import com.archimatetool.editor.diagram.editparts.connections.SpecializationConnectionEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Specialization Relationship UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SpecializationRelationshipUIProvider extends AbstractArchimateRelationshipUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSpecializationRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new SpecializationConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.SpecializationRelationshipUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_SPECIALIZATION_RELATION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SPECIALIZATION_RELATION_16);
    }
}
