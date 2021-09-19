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
import com.archimatetool.editor.diagram.figures.elements.MaterialFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Material UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class MaterialUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getMaterial();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(MaterialFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.MaterialUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_MATERIAL);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_MATERIAL);
    }
    
    @Override
    public Color getDefaultColor() {
        return defaultTechnologyColor;
    }
    
    @Override
    protected Dimension getDefaultSizeForFigureType(int figureType) {
        return super.getDefaultSizeForFigureType(figureType);
        //return figureType == 1 ? getDefaultSquareSize() : super.getDefaultSizeForFigureType(figureType);
    }
}
