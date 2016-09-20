/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Diagram Image UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageUIProvider extends AbstractGraphicalObjectUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDiagramModelImage();
    }
    
    @Override
    public EditPart createEditPart() {
        return new DiagramImageEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.DiagramImageUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(200, 150);
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LANDSCAPE);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LANDSCAPE);
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 255, 255);
    }
    
    @Override
    public boolean shouldExposeFeature(EAttribute feature) {
        return feature == IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR;
    }

}
