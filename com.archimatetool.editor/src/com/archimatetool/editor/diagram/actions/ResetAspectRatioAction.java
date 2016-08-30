/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.SetConstraintObjectCommand;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.diagram.figures.diagram.DiagramImageFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelObject;
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
        
        // Quick checks
        if(selected.isEmpty()) {
            return false;
        }
        
        for(Object object : selected) {
            if(!(object instanceof EditPart)) {
                return false;
            }
        }

        Command command = createCommand(selected);
        if(command == null) {
            return false;
        }
        return command.canExecute();
    }

    private Command createCommand(List<?> objects) {
        CompoundCommand result = new CompoundCommand();
        
        for(Object object : objects) {
            if(object instanceof DiagramImageEditPart) {
                DiagramImageEditPart part = (DiagramImageEditPart)object;
                IDiagramModelObject model = part.getModel();

                // Locked
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }

                IBounds modelBounds = model.getBounds().getCopy();
                int currentHeight = modelBounds.getHeight();
                int currentWidth = modelBounds.getWidth();

                // Already set to default height and width
                if(currentHeight == -1 && currentWidth == -1) {
                    continue;
                }

                // Get original default size
                DiagramImageFigure figure = part.getFigure();
                Dimension size = figure.getDefaultSize();

                if(size.height == 0 || size.width == 0) {
                    continue;
                }

                float originalRatio = ((float) size.height / (float) size.width);
                float currentRatio = ((float) currentHeight / (float) currentWidth);

                // If the ratio is different within tolerance
                if(Math.abs(originalRatio - currentRatio) > 0.01) {
                    int width = currentWidth;
                    int height = currentHeight;

                    if(currentWidth < currentHeight) {
                        width = (int) (currentHeight / originalRatio);
                    }
                    else {
                        height = (int) (currentWidth * originalRatio);
                    }

                    modelBounds.setWidth(width);
                    modelBounds.setHeight(height);

                    Command cmd = new SetConstraintObjectCommand(model, modelBounds);
                    result.add(cmd);
                }
            }
        }

        return result.unwrap();
    }
}
