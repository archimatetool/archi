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
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IFolder;



/**
 * Add Archimate Element Command
 * 
 * @author Phillip Beauvoir
 */
public class NewElementCommand extends Command {
    
    private IFolder fFolder;
    private IArchimateElement fElement;

    public NewElementCommand(IFolder folder, IArchimateElement element) {
        fFolder = folder;
        fElement = element;
        setLabel(Messages.NewElementCommand_0 + " " + element.getName()); //$NON-NLS-1$
    }
    
    @Override
    public void execute() {
        redo();
        
        // Edit in-place
        UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, fElement));
    }
    
    @Override
    public void undo() {
        fFolder.getElements().remove(fElement);
        
        // Select the parent node if no node is selected (this happens when the node is deleted)
        TreeSelectionRequest request = new TreeSelectionRequest(this, new StructuredSelection(fFolder), true) {
            @Override
            public boolean shouldSelect(Viewer viewer) {
                return viewer.getSelection().isEmpty();
            }
        };
        UIRequestManager.INSTANCE.fireRequest(request);
    }
    
    @Override
    public void redo() {
        fFolder.getElements().add(fElement);
        
        // Select
        UIRequestManager.INSTANCE.fireRequest(new TreeSelectionRequest(this, new StructuredSelection(fElement), true));
    }
}
