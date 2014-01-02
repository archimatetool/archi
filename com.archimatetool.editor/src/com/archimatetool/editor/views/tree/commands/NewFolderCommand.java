/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.editor.views.tree.TreeSelectionRequest;
import com.archimatetool.model.IFolder;



/**
 * New Folder Command
 * 
 * @author Phillip Beauvoir
 */
public class NewFolderCommand extends Command {
    
    private IFolder fParent;
    private IFolder fFolder;

    public NewFolderCommand(IFolder parent, IFolder folder) {
        fParent = parent;
        fFolder = folder;
        setLabel(Messages.NewFolderCommand_0);
    }
    
    @Override
    public void execute() {
        redo();
        
        // Edit in-place
        UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, fFolder));
    }
    
    @Override
    public void undo() {
        fParent.getFolders().remove(fFolder);
        
        // Select the parent node if no node is selected (this happens when the node is deleted)
        TreeSelectionRequest request = new TreeSelectionRequest(this, new StructuredSelection(fParent), true) {
            @Override
            public boolean shouldSelect(Viewer viewer) {
                return viewer.getSelection().isEmpty();
            }
        };
        UIRequestManager.INSTANCE.fireRequest(request);
    }
    
    @Override
    public void redo() {
        fParent.getFolders().add(fFolder);
        
        // Select
        UIRequestManager.INSTANCE.fireRequest(new TreeSelectionRequest(this, new StructuredSelection(fFolder), true));
    }
}
