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

import com.archimatetool.editor.diagram.editparts.connections.AssignmentConnectionEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Assignment Relationship UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class AssignmentRelationshipUIProvider extends AbstractRelationshipUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getAssignmentRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new AssignmentConnectionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.AssignmentRelationshipUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_ASSIGNMENT_CONNECTION_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_ASSIGNMENT_CONNECTION_16);
    }
}
