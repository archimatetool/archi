/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.SetConstraintObjectCommand;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelObject;
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
        
        for(Object object : selected) {
            if(!(object instanceof EditPart)) {
                return false;
            }
        }

        Command command = createDefaultSizeCommand(selected);
        if(command == null) {
            return false;
        }
        return command.canExecute();
    }

    private Command createDefaultSizeCommand(List<?> objects) {
        CompoundCommand command = new CompoundCommand();
        
        for(Object object : objects) {
            if(object instanceof GraphicalEditPart) {
                GraphicalEditPart part = (GraphicalEditPart)object;
                if(part.getModel() instanceof IDiagramModelObject) {
                    IDiagramModelObject model = (IDiagramModelObject)part.getModel();

                    if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                        continue;
                    }
                    
                    IFigure figure = part.getFigure();
                    if(figure instanceof IDiagramModelObjectFigure) {
                        Dimension defaultSize = ((IDiagramModelObjectFigure)figure).getDefaultSize();
                        IBounds bounds = model.getBounds().getCopy();
                        if(bounds.getWidth() != defaultSize.width || bounds.getHeight() != defaultSize.height) {
                            bounds.setWidth(defaultSize.width);
                            bounds.setHeight(defaultSize.height);
                            Command cmd = new SetConstraintObjectCommand(model, bounds);
                            command.add(cmd);
                        }
                    }
                }
            }
        }

        return command.unwrap();
    }
}
