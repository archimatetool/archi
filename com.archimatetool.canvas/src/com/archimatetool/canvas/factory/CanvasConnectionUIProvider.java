/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.editparts.DiagramConnectionEditPart;
import com.archimatetool.editor.diagram.figures.diagram.LineConnectionFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.diagram.DiagramConnectionUIProvider;



/**
 * Canvas Diagram Connection UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasConnectionUIProvider extends DiagramConnectionUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelConnection();
    }
    
    @Override
    public EditPart createEditPart() {
        return new DiagramConnectionEditPart(LineConnectionFigure.class);
    }

    @Override
    public String getDefaultName() {
        return Messages.CanvasLineConnectionUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_CONNECTION_ARROW);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_CONNECTION_ARROW);
    }
}
