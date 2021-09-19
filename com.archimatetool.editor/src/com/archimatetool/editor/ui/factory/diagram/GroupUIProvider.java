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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;



/**
 * Group UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class GroupUIProvider extends AbstractGraphicalObjectUIProvider {
    
    private static Color defaultColor = new Color(210, 215, 215);

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDiagramModelGroup();
    }
    
    @Override
    public EditPart createEditPart() {
        return new GroupEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.GroupUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(400, 140);
    }
    
    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_GROUP);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUP);
    }

    @Override
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_LEFT;
    }
}
