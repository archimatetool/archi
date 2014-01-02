/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.SetConstraintObjectCommand;
import com.archimatetool.editor.ui.IArchimateImages;
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

    public static final String ID = "DefaultEditPartSizeAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.DefaultEditPartSizeAction_0;

    public DefaultEditPartSizeAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setToolTipText(Messages.DefaultEditPartSizeAction_1);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DEFAULT_SIZE));
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
            if(object instanceof EditPart) {
                EditPart part = (EditPart)object;
                if(part.getModel() instanceof IDiagramModelObject) {
                    IDiagramModelObject model = (IDiagramModelObject)part.getModel();

                    if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                        continue;
                    }
                    
                    IBounds bounds = model.getBounds().getCopy();

                    if(bounds.getWidth() != -1 || bounds.getHeight() != -1) {
                        bounds.setWidth(-1);
                        bounds.setHeight(-1);
                        Command cmd = new SetConstraintObjectCommand(model, bounds);
                        command.add(cmd);
                    }
                }
            }
        }

        return command.unwrap();
    }
}
