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
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ILockable;


/**
 * Action to set the size of an Edit Part to default.
 * 
 * This is based on the org.eclipse.gef.ui.actions.MatchSizeAction
 * 
 * @author Phillip Beauvoir
 */
public class DefaultEditPartSizeAction extends SelectionAction {

    public static final String ID = "com.archimatetool.action.defaultSize"; //$NON-NLS-1$
    public static final String TEXT = Messages.DefaultEditPartSizeAction_0;

    public DefaultEditPartSizeAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID);
        setToolTipText(Messages.DefaultEditPartSizeAction_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DEFAULT_SIZE));
    }

    @Override
    public void run() {
        execute(createDefaultSizeCommand(getSelectedObjects()));
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selected = getSelectedObjects();
        
        // Quick checks
        if(selected.isEmpty()) {
            return false;
        }
        
        return createDefaultSizeCommand(selected).canExecute();
    }

    private Command createDefaultSizeCommand(List<?> objects) {
        CompoundCommand command = new CompoundCommand();
        
        for(Object object : objects) {
            if(object instanceof AbstractConnectedEditPart) {
                AbstractConnectedEditPart editPart = (AbstractConnectedEditPart)object;
                IDiagramModelObject model = editPart.getModel();
                
                // Locked
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                
                AbstractDiagramModelObjectFigure figure = (AbstractDiagramModelObjectFigure)editPart.getFigure();
                IBounds bounds = model.getBounds().getCopy();

                // If the object has an icon image and set to image fill and isn't a container with childrem
                if(model instanceof IIconic
                        && ((IIconic)model).getImagePosition() == IIconic.ICON_POSITION_FILL
                        && figure.hasIconImage()
                        && !(model instanceof IDiagramModelContainer && !((IDiagramModelContainer)model).getChildren().isEmpty())) {
                    Rectangle imageBounds = figure.getIconicDelegate().getImage().getBounds();
                    bounds.setWidth(imageBounds.width);
                    bounds.setHeight(imageBounds.height);
                }
                else {
                    Dimension defaultSize = ((IDiagramModelObjectFigure)figure).getDefaultSize();
                    bounds.setWidth(defaultSize.width);
                    bounds.setHeight(defaultSize.height);
                }
                
                if(bounds.getWidth() != model.getBounds().getWidth() || bounds.getHeight() != model.getBounds().getHeight()) {
                    Command cmd = new SetConstraintObjectCommand(model, bounds);
                    command.add(cmd);
                }
            }
        }

        return command.unwrap();
    }
}
