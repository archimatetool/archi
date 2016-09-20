/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateElementEditPart;
import com.archimatetool.editor.diagram.figures.elements.GroupingFigure;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;



/**
 * Grouping UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class GroupingUIProvider extends AbstractArchimateElementUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getGrouping();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateElementEditPart(GroupingFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.GroupingUIProvider_0;
    }

    @Override
    public Image getImage() {
        return getImageWithUserFillColor(IArchiImages.ICON_GROUPING);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return getImageDescriptorWithUserFillColor(IArchiImages.ICON_GROUPING);
    }
    
    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 255, 255);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return new Dimension(400, 140);
    }
    
    @Override
    public Dimension getUserDefaultSize() {
        return getDefaultSize();
    }
    
    @Override
    public boolean shouldExposeFeature(EAttribute feature) {
        // Text position & alignment are fixed
        if(feature == IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION || feature == IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT) {
            return false;
        }

        return true;
    }
}
