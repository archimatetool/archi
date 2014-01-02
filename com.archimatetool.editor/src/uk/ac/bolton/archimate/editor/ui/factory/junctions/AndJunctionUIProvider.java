/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.junctions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.junctions.AndJunctionEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.factory.AbstractElementUIProvider;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * And Junction UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class AndJunctionUIProvider extends AbstractElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getAndJunction();
    }
    
    @Override
    public EditPart createEditPart() {
        return new AndJunctionEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.AndJunctionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_JUNCTION_AND_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_JUNCTION_AND_16);
    }

    @Override
    public Color getDefaultColor() {
        return ColorConstants.black;
    }
}
