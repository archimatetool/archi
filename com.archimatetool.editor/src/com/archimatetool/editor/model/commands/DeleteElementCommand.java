/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;



/**
 * Delete Archimate Element Command
 * 
 * @author Phillip Beauvoir
 */
public class DeleteElementCommand extends Command {
    
    private INameable fElement;
    private int fIndex;
    private IFolder fFolder;

    public DeleteElementCommand(INameable element) {
        fFolder = (IFolder)element.eContainer();
        fElement = element;
        setLabel(Messages.DeleteElementCommand_0 + " " + ArchimateLabelProvider.INSTANCE.getLabel(fElement)); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        fIndex = fFolder.getElements().indexOf(fElement); 
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().remove(fElement);
        }
    }
    
    @Override
    public void undo() {
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().add(fIndex, fElement);
        }
    }
    
    @Override
    public void dispose() {
        fElement = null;
        fFolder = null;
    }
}
