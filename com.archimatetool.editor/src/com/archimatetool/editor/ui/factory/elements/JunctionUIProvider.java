/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.LeafEditPart;
import com.archimatetool.editor.diagram.figures.elements.JunctionFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Junction UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class JunctionUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getJunction();
    }
    
    @Override
    public EditPart createEditPart() {
        return new LeafEditPart(JunctionFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.JunctionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_AND_JUNCTION);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_AND_JUNCTION);
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
    public boolean shouldExposeFeature(String featureName) {
        // Junctions don't expose all UI features
        return featureName == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName() ||
                featureName == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName() ||
                        featureName == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName(); 
    }
    
    @Override
    public boolean hasIcon() {
        return false;
    }

}
