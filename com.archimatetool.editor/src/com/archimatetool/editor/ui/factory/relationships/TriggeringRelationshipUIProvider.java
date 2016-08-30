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

import com.archimatetool.editor.diagram.editparts.connections.TriggeringConnectionEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Triggering Relationship UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TriggeringRelationshipUIProvider extends AbstractArchimateRelationshipUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getTriggeringRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TriggeringConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TriggeringRelationshipUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_TRIGGERING_RELATION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_TRIGGERING_RELATION_16);
    }
}
