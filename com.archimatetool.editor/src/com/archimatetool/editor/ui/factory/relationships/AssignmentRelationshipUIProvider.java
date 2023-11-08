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

import com.archimatetool.editor.diagram.editparts.ArchimateRelationshipEditPart;
import com.archimatetool.editor.diagram.figures.connections.AssignmentConnectionFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Assignment Relationship UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class AssignmentRelationshipUIProvider extends AbstractArchimateRelationshipUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getAssignmentRelationship();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateRelationshipEditPart(AssignmentConnectionFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.AssignmentRelationshipUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ASSIGNMENT_RELATION);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ASSIGNMENT_RELATION);
    }
    
    @Override
    public String getKeyChord() {
    	return "rs";
    }
}
