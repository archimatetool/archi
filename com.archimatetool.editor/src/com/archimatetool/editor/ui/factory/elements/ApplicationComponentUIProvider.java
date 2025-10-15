/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.ApplicationComponentFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IArchimatePackage;



/**
 * Application Component UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationComponentUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getApplicationComponent();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(ApplicationComponentFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.ApplicationComponentUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_APPLICATION_COMPONENT);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_APPLICATION_COMPONENT);
    }
    
    @Override
    public Color getDefaultColor() {
        return defaultApplicationColor;
    }

    @Override
    public IIconDelegate getIconDelegate() {
        return ApplicationComponentFigure.getIconDelegate();
    }
}
