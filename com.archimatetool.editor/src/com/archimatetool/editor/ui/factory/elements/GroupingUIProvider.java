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
import com.archimatetool.editor.diagram.figures.elements.GroupingFigure;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Grouping UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class GroupingUIProvider extends AbstractArchimateElementUIProvider {

    @Override
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
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_GROUPING);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_GROUPING);
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
    public boolean hasAlternateFigure() {
        return true;
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_LEFT;
    }
    
    @Override
    public int getDefaultTextPosition() {
        return ITextPosition.TEXT_POSITION_TOP;
    }
    
    @Override
    public boolean hasIcon() {
        return false;
    }

}
