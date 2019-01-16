/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;



/**
 * Note UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class NoteUIProvider extends AbstractGraphicalObjectUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDiagramModelNote();
    }
    
    @Override
    public EditPart createEditPart() {
        return new NoteEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.NoteUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(185, 80);
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_NOTE);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE);
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_LEFT;
    }
}
