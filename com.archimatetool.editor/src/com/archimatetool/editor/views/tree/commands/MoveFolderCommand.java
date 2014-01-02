/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IFolder;


/**
 * Move Folder Command
 * 
 * @author Phillip Beauvoir
 */
public class MoveFolderCommand extends Command {
    private IFolder fOldParent;
    private IFolder fNewParent;
    private IFolder fFolder;
    private int fOldPos;
    
    public MoveFolderCommand(IFolder newParent, IFolder folder) {
        super(Messages.MoveFolderCommand_0 + " " + folder.getName()); //$NON-NLS-1$
        fOldParent = (IFolder)folder.eContainer();
        fNewParent = newParent;
        fFolder = folder;
    }
    
    @Override
    public void execute() {
        fOldPos = fOldParent.getFolders().indexOf(fFolder); // do this here as its part of a compound command
        redo();
    }
    
    @Override
    public void undo() {
        fNewParent.getFolders().remove(fFolder);
        fOldParent.getFolders().add(fOldPos, fFolder);
    }
    
    @Override
    public void redo() {
        fOldParent.getFolders().remove(fFolder);
        fNewParent.getFolders().add(fFolder);
    }
    
    @Override
    public void dispose() {
        fOldParent = null;
        fNewParent = null;
        fFolder = null;
    }
}