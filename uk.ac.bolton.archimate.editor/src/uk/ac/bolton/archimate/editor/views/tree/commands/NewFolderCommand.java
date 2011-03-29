/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;

import uk.ac.bolton.archimate.editor.views.tree.TreeModelView;
import uk.ac.bolton.archimate.model.IFolder;


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
        setLabel("New Folder");
    }
    
    @Override
    public void execute() {
        redo();
        
        // Edit in-place
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().editElement(fFolder);
        }
    }
    
    @Override
    public void undo() {
        fParent.getFolders().remove(fFolder);
        
        // Select the parent node if none selected
        if(TreeModelView.INSTANCE != null && TreeModelView.INSTANCE.getViewer().getSelection().isEmpty()) {
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fParent), true);
        }
    }
    
    @Override
    public void redo() {
        fParent.getFolders().add(fFolder);
        
        // Expand and select
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().expandToLevel(fParent, 1);
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fFolder), true);
        }
    }
}
