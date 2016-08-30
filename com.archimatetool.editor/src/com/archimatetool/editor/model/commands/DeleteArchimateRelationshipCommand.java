/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.gef.commands.Command;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;



/**
 * Delete Archimate Relationship Command
 * 
 * @author Phillip Beauvoir
 */
public class DeleteArchimateRelationshipCommand extends Command {
    
    private IArchimateRelationship fRelationship;
    private int fIndex;
    private IFolder fFolder;
    
    public DeleteArchimateRelationshipCommand(IArchimateRelationship relationship) {
        fFolder = (IFolder)relationship.eContainer();
        fRelationship = relationship;
        
        setLabel(Messages.DeleteElementCommand_0 + " " + ArchiLabelProvider.INSTANCE.getLabel(fRelationship)); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        // Ensure fIndex is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        fIndex = fFolder.getElements().indexOf(fRelationship); 
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().remove(fRelationship);
        }
        
        // Disconnect source/target references
        fRelationship.disconnect();
    }
    
    @Override
    public void undo() {
        if(fIndex != -1) { // might be already be deleted from Command in CompoundCommand
            fFolder.getElements().add(fIndex, fRelationship);
        }
        
        // Reconnect source/target references
        fRelationship.reconnect();
    }
    
    @Override
    public void dispose() {
        fRelationship = null;
        fFolder = null;
    }
}
