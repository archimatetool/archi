/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.DriverFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IArchimatePackage;



/**
 * Driver UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class DriverUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDriver();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(DriverFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.DriverUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_DRIVER);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DRIVER);
    }

    @Override
    public Color getDefaultColor() {
        return defaultMotivationColor;
    }
    
    @Override
    protected Dimension getDefaultSizeForFigureType(int figureType) {
        return super.getDefaultSizeForFigureType(figureType);
        //return figureType == 1 ? getDefaultSquareSize() : super.getDefaultSizeForFigureType(figureType);
    }
    
    @Override
    public IIconDelegate getIconDelegate() {
        return DriverFigure.getIconDelegate();
    }
}
