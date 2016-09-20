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

import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.canvas.editparts.CanvasDiagramPart;
import com.archimatetool.canvas.figures.CanvasModelGraphicsIcon;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.IGraphicsIcon;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.editor.ui.factory.IDiagramModelUIProvider;



/**
 * Canvas Model UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelUIProvider extends AbstractObjectUIProvider implements IDiagramModelUIProvider {
    
    private IGraphicsIcon graphicsIcon = new CanvasModelGraphicsIcon();

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModel();
    }
    
    @Override
    public EditPart createEditPart() {
        return new CanvasDiagramPart();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.CanvasModelUIProvider_0;
    }

    @Override
    public Image getImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_MODEL);
    }
    
    @Override
    public IGraphicsIcon getGraphicsIcon() {
        return graphicsIcon;
    }
}
