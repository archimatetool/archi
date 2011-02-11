/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;

import uk.ac.bolton.archimate.editor.ui.EditorManager;
import uk.ac.bolton.archimate.editor.views.tree.TreeModelView;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;


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
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().editElement(fDiagramModel);
        }
    }
    
    @Override
    public void undo() {
        // Close the Editor FIRST!
        EditorManager.closeDiagramEditor(fDiagramModel);

        fFolder.getElements().remove(fDiagramModel);
        
        // Select the parent node
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fFolder), true);
        }
    }
    
    @Override
    public void redo() {
        fFolder.getElements().add(fDiagramModel);
        
        // Expand and select
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().expandToLevel(fFolder, 1);
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fDiagramModel), true);
        }
        
        // Open Editor
        EditorManager.openDiagramEditor(fDiagramModel);
    }
    
    @Override
    public void dispose() {
        fFolder = null;
        fDiagramModel = null;
    }
}
