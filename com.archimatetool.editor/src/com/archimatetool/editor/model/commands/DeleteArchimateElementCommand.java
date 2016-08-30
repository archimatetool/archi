/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IFolder;



/**
 * Delete Archimate Element Command
 * 
 * @author Phillip Beauvoir
 */
public class DeleteArchimateElementCommand extends Command {
    
    private IArchimateElement fArchimateElement;
    private int fIndex;
    private IFolder fFolder;

    public DeleteArchimateElementCommand(IArchimateElement element) {
        fFolder = (IFolder)element.eContainer();
        fArchimateElement = element;
        setLabel(Messages.DeleteElementCommand_0 + " " + ArchiLabelProvider.INSTANCE.getLabel(fArchimateElement)); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        fIndex = fFolder.getElements().indexOf(fArchimateElement); 
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().remove(fArchimateElement);
        }
    }
    
    @Override
    public void undo() {
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().add(fIndex, fArchimateElement);
        }
    }
    
    @Override
    public void dispose() {
        fArchimateElement = null;
        fFolder = null;
    }
}
