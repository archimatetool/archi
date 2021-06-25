/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.SetConstraintObjectCommand;
import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ILockable;


/**
 * Action to set the aspect ratio of a figure to its default.
 * 
 * @author Phillip Beauvoir
 */
public class ResetAspectRatioAction extends SelectionAction {

    public static final String ID = "ResetAspectRatioAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ResetAspectRatioAction_0;

    public ResetAspectRatioAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setToolTipText(Messages.ResetAspectRatioAction_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ASPECT_RATIO));
    }

    @Override
    public void run() {
        execute(createCommand(getSelectedObjects()));
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selected = getSelectedObjects();
        
        if(selected.isEmpty()) {
            return false;
        }
        
        return createCommand(selected).canExecute();
    }

    private Command createCommand(List<?> objects) {
        CompoundCommand result = new CompoundCommand();
        
        for(Object object : objects) {
            if(object instanceof AbstractConnectedEditPart) {
                AbstractConnectedEditPart editPart = (AbstractConnectedEditPart)object;
                AbstractDiagramModelObjectFigure figure = (AbstractDiagramModelObjectFigure)editPart.getFigure();
                IDiagramModelObject dmo = editPart.getModel();
                
                // Diagram Image object with image or Iconic type with image and Fill setting
                if(!isDiagramObjectWithImage(dmo) && !isIconicWithImageFill(dmo, figure)) {
                    continue;
                }
                
                // Locked
                if(dmo instanceof ILockable && ((ILockable)dmo).isLocked()) {
                    continue;
                }
                
                IBounds modelBounds = dmo.getBounds().getCopy();
                int currentHeight = modelBounds.getHeight();
                int currentWidth = modelBounds.getWidth();
                
                // Sanity check
                if(currentHeight < 1 || currentWidth < 1) {
                    continue;
                }
                
                float currentRatio = ((float) currentHeight / (float) currentWidth);
                float otherRatio = 0;
                
                // Image object type
                if(dmo instanceof IDiagramModelImage) {
                    // This will return the image's proper size (unsized) if there is an image, else the object's size
                    Dimension size = figure.getDefaultSize();
                    
                    if(size.height == 0 || size.width == 0) {
                        continue;
                    }

                    otherRatio = ((float) size.height / (float) size.width);
                }
                // Iconic and fill type is fill and has an image
                else {
                    Rectangle imageBounds = figure.getIconicDelegate().getImage().getBounds();
                    otherRatio = ((float) imageBounds.height / (float) imageBounds.width);
                }
                
                // If the ratio is different (within tolerance)
                if(otherRatio != 0 && Math.abs(otherRatio - currentRatio) > 0.01) {
                    if(currentWidth < currentHeight) {
                        currentWidth = (int) (currentHeight / otherRatio);
                    }
                    else {
                        currentHeight = (int) (currentWidth * otherRatio);
                    }

                    modelBounds.setWidth(currentWidth);
                    modelBounds.setHeight(currentHeight);

                    Command cmd = new SetConstraintObjectCommand(dmo, modelBounds);
                    result.add(cmd);
                }
            }
        }

        return result.unwrap();
    }
    
    private boolean isIconicWithImageFill(IDiagramModelObject dmo, AbstractDiagramModelObjectFigure figure) {
        return dmo instanceof IIconic && ((IIconic)dmo).getImagePosition() == IIconic.ICON_POSITION_FILL && figure.hasIconImage();
    }
    
    private boolean isDiagramObjectWithImage(IDiagramModelObject dmo) {
        return dmo instanceof IDiagramModelImage && ((IDiagramModelImage)dmo).getImagePath() != null;
    }
}
