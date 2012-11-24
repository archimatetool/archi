/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.canvas;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.canvas.model.ICanvasModel;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelBlock;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelConnection;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelImage;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelSticky;
import uk.ac.bolton.archimate.canvas.model.ICanvasPackage;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.IEditorLabelProvider;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.INameable;


/**
 * Label Provider for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasLabelProvider implements IEditorLabelProvider {
    
    public static CanvasLabelProvider INSTANCE = new CanvasLabelProvider();

    @Override
    public String getLabel(Object element) {
        if(element == null) {
            return ""; //$NON-NLS-1$
        }
        
        // Get Name
        if(element instanceof INameable) {
            String name = ((INameable)element).getName();
            if(StringUtils.isSet(name)) {
                return name;
            }
        }
        
        // Defaults for empty strings
        if(element instanceof ICanvasModelImage) {
            return Messages.CanvasLabelProvider_0;
        }
        else if(element instanceof ICanvasModelBlock) {
            return Messages.CanvasLabelProvider_1;
        }
        else if(element instanceof ICanvasModelSticky) {
            return Messages.CanvasLabelProvider_2;
        }
        else if(element instanceof ICanvasModel) {
            return Messages.CanvasLabelProvider_3;
        }
        else if(element instanceof ICanvasModelConnection) {
            return Messages.CanvasLabelProvider_4;
        }
        
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        // This first, since EClass is an EObject
        if(element instanceof EClass) {
            return getEClassImage((EClass)element);
        }
        if(element instanceof EObject) {
            return getEClassImage(((EObject)element).eClass());
        }
        
        return null;
    }

    public Image getEClassImage(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case ICanvasPackage.CANVAS_MODEL:
                return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL_16);
            case ICanvasPackage.CANVAS_MODEL_BLOCK:
                return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_BLOCK_16);
            case ICanvasPackage.CANVAS_MODEL_STICKY:
                return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_STICKY_16);
            case ICanvasPackage.CANVAS_MODEL_IMAGE:
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_LANDSCAPE_16);
            case ICanvasPackage.CANVAS_MODEL_CONNECTION:
                return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_CONNECTION_ARROW_16);
        }
        
        return null;
    }
}
