/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.ui.factory.sketch;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.sketch.editparts.StickyEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.factory.AbstractElementUIProvider;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Sketch Sticky UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SketchStickyUIProvider extends AbstractElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSketchModelSticky();
    }
    
    @Override
    public EditPart createEditPart() {
        return new StickyEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.SketchStickyUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_STICKY_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_STICKY_16);
    }
}
