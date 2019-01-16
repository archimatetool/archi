/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.sketch;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.sketch.editparts.StickyEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;



/**
 * Sketch Sticky UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SketchStickyUIProvider extends AbstractGraphicalObjectUIProvider {

    @Override
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
    public Dimension getDefaultSize() {
        return new Dimension(135, 70);
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_STICKY);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_STICKY);
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_LEFT;
    }
}
