/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;



/**
 * Delete Folder Command.
 * 
 * @author Phillip Beauvoir
 */
public class DeleteFolderCommand extends Command {
    
    private IFolderContainer fParent;
    private IFolder fFolder;
    private int fIndex;

    public DeleteFolderCommand(IFolder folder) {
        fParent = (IFolderContainer)folder.eContainer();
        fFolder = folder;
        setLabel(Messages.DeleteFolderCommand_0);
    }
    
    @Override
    public void execute() {
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        
        fIndex = fParent.getFolders().indexOf(fFolder); 
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fParent.getFolders().remove(fFolder);
        }
    }
    
    @Override
    public void undo() {
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fParent.getFolders().add(fIndex, fFolder);
        }
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fFolder = null;
    }
}
