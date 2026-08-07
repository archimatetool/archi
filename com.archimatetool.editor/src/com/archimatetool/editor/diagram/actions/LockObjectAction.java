/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

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
        execute(createLockCommand());
        updateText();
    }

    @Override
    protected boolean calculateEnabled() {
        updateText();
        return createLockCommand().canExecute();
    }

    /**
     * Get Lock or Unlock based on first selected object
     */
    private boolean isToLock() {
        for(EditPart editPart : getSelectedEditParts()) {
            if(editPart.getModel() instanceof ILockable lockable) {
                return !lockable.isLocked();
            }
        }
        
        return true;
    }
    
    private void updateText() {
        boolean lock = isToLock();
        setText(lock ? Messages.LockObjectAction_0 : Messages.LockObjectAction_2);
        setImageDescriptor(lock ? IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LOCK) : IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_UNLOCK));
    }

    private Command createLockCommand() {
        CompoundCommand compoundCommand = new CompoundCommand();
        
        boolean lock = isToLock();
        
        for(EditPart editPart : getSelectedEditParts()) {
            if(editPart.getModel() instanceof ILockable lockable) {
                Command cmd = new LockObjectCommand(lockable, lock);
                if(cmd.canExecute()) {
                    compoundCommand.add(cmd);
                }
            }
        }

        return compoundCommand.unwrap();
    }
}
