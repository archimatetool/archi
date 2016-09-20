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

import com.archimatetool.editor.diagram.commands.LockObjectCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.ILockable;


/**
 * Action to Lock an Element
 * 
 * @author Phillip Beauvoir
 */
public class LockObjectAction extends SelectionAction {

    public static final String ID = "LockObjectAction"; //$NON-NLS-1$

    public LockObjectAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.LockObjectAction_0);
        setId(ID);
        setToolTipText(Messages.LockObjectAction_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LOCK));
    }

    @Override
    public void run() {
        execute(createLockCommand(getSelectedObjects()));
        updateText();
    }

    /**
     * Set Lock or Unlock based on first selected object
     */
    private boolean isToLock() {
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart) {
                EditPart part = (EditPart)object;
                if(part.getModel() instanceof ILockable) {
                    ILockable model = (ILockable)part.getModel();
                    return !model.isLocked();
                }
            }
        }
        
        return true;
    }
    
    private void updateText() {
        boolean lock = isToLock();
        setText(lock ? Messages.LockObjectAction_0 : Messages.LockObjectAction_2);
        setImageDescriptor(lock ? IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LOCK) :
            IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_UNLOCK));
    }

    @Override
    protected boolean calculateEnabled() {
        updateText();
        
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

        Command command = createLockCommand(selected);
        if(command == null) {
            return false;
        }
        
        return command.canExecute();
    }

    private Command createLockCommand(List<?> objects) {
        CompoundCommand command = new CompoundCommand();
        boolean lock = isToLock();
        
        for(Object object : objects) {
            if(object instanceof EditPart) {
                EditPart part = (EditPart)object;
                if(part.getModel() instanceof ILockable) {
                    ILockable model = (ILockable)part.getModel();
                    if(model.isLocked() != lock) {
                        Command cmd = new LockObjectCommand(model, lock);
                        command.add(cmd);
                    }
                }
            }
        }

        return command.unwrap();
    }
}
