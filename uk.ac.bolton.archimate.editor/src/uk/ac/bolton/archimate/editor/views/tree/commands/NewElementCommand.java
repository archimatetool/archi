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
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IFolder;


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
        setLabel("New " + element.getName());
    }
    
    @Override
    public void execute() {
        redo();
        
        // Edit in-place
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().editElement(fElement);
        }
    }
    
    @Override
    public void undo() {
        fFolder.getElements().remove(fElement);
        
        // Select the parent node if none selected
        if(TreeModelView.INSTANCE != null && TreeModelView.INSTANCE.getViewer().getSelection().isEmpty()) {
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fFolder), true);
        }
    }
    
    @Override
    public void redo() {
        fFolder.getElements().add(fElement);
        
        // Expand and select
        if(TreeModelView.INSTANCE != null) {
            TreeModelView.INSTANCE.getViewer().expandToLevel(fFolder, 1);
            TreeModelView.INSTANCE.getViewer().setSelection(new StructuredSelection(fElement), true);
        }
    }
}
