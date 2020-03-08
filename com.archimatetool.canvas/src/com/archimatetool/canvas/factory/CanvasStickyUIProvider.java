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
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.canvas.editparts.CanvasStickyEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextPosition;



/**
 * Canvas Sticky UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasStickyUIProvider extends AbstractGraphicalObjectUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelSticky();
    }
    
    @Override
    public EditPart createEditPart() {
        return new CanvasStickyEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.CanvasStickyUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(75, 63);
    }

    @Override
    public Image getImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_STICKY);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_STICKY);
    }
    
    @Override
    public boolean shouldExposeFeature(String featureName) {
        if(featureName == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName() ||
                featureName == IDiagramModelObject.FEATURE_GRADIENT) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int getDefaultTextPosition() {
        return ITextPosition.TEXT_POSITION_CENTRE;
    }
}
