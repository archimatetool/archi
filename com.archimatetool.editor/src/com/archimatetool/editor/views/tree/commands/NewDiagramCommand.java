/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.editor.views.tree.TreeSelectionRequest;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;



/**
 * Add New Diagram Command
 * 
 * @author Phillip Beauvoir
 */
public class NewDiagramCommand extends Command {
    
    private IFolder fFolder;
    private IDiagramModel fDiagramModel;

    public NewDiagramCommand(IFolder folder, IDiagramModel diagramModel, String label) {
        super(label);
        fFolder = folder;
        fDiagramModel = diagramModel;
    }
    
    @Override
    public void execute() {
        redo();
        
        // Edit in-place
        UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, fDiagramModel));
    }
    
    @Override
    public void undo() {
        // Close the Editor FIRST!
        EditorManager.closeDiagramEditor(fDiagramModel);

        fFolder.getElements().remove(fDiagramModel);
        
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
        fFolder.getElements().add(fDiagramModel);
        
        // Select
        UIRequestManager.INSTANCE.fireRequest(new TreeSelectionRequest(this, new StructuredSelection(fDiagramModel), true));
        
        // Open Editor
        EditorManager.openDiagramEditor(fDiagramModel, false);
    }
    
    @Override
    public void dispose() {
        fFolder = null;
        fDiagramModel = null;
    }
}
