/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.LeafEditPart;
import com.archimatetool.editor.diagram.figures.elements.OrJunctionFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Or Junction UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class OrJunctionUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getOrJunction();
    }
    
    @Override
    public EditPart createEditPart() {
        return new LeafEditPart(OrJunctionFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.OrJunctionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_OR_JUNCTION);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_OR_JUNCTION);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return new Dimension(15, 15);
    }

    @Override
    public Dimension getUserDefaultSize() {
        return getDefaultSize();
    }

    @Override
    public Color getDefaultColor() {
        return ColorConstants.black;
    }
    
    @Override
    public boolean shouldExposeFeature(EAttribute feature) {
        return false; // Junctions don't expose UI features
    }
}
