/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.diagram.DiagramImageUIProvider;



/**
 * Canvas Image UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockUIProvider extends DiagramImageUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelBlock();
    }
    
    @Override
    public EditPart createEditPart() {
        return new DiagramImageEditPart();
    }

    @Override
    public String getDefaultName() {
        return "Block"; //$NON-NLS-1$
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(200, 200);
    }

    @Override
    public Image getImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_BLOCK_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLOCK_16);
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(255, 255, 255);
    }
}
