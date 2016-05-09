/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.canvas.editparts.CanvasBlockEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractElementUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Canvas Block UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockUIProvider extends AbstractElementUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelBlock();
    }
    
    @Override
    public EditPart createEditPart() {
        return new CanvasBlockEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.CanvasBlockUIProvider_0;
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
    public boolean shouldExposeFeature(EObject instance, EAttribute feature) {
        if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR) {
            return false;
        }
        
        return super.shouldExposeFeature(instance, feature);
    }
}
