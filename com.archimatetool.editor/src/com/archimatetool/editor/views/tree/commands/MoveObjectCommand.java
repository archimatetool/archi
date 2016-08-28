/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IFolder;


/**
 * Move Object Command
 * 
 * @author Phillip Beauvoir
 */
public class MoveObjectCommand extends Command {
    private IFolder fOldParent;
    private IFolder fNewParent;
    private IArchimateModelObject fObject;
    private int fOldPos;
    
    public MoveObjectCommand(IFolder newParent, IArchimateModelObject object) {
        super(Messages.MoveObjectCommand_0 + " " + object.getName()); //$NON-NLS-1$
        fOldParent = (IFolder)object.eContainer();
        fNewParent = newParent;
        fObject = object;
    }
    
    @Override
    public void execute() {
        fOldPos = fOldParent.getElements().indexOf(fObject); // do this here as its part of a compound command
        redo();
    }
    
    @Override
    public void undo() {
        fNewParent.getElements().remove(fObject);
        fOldParent.getElements().add(fOldPos, fObject);
    }
    
    @Override
    public void redo() {
        fOldParent.getElements().remove(fObject);
        fNewParent.getElements().add(fObject);
    }
    
    @Override
    public void dispose() {
        fOldParent = null;
        fNewParent = null;
        fObject = null;
    }
}