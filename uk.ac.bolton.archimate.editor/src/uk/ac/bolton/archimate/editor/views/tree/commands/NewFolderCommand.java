/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;
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
        fParent.getFolders().add(fFolder);
        
        // Fire this event so the tree view can select the element
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                ITreeModelView.PROPERTY_MODEL_ELEMENT_NEW, null, fFolder);
    }
    
    @Override
    public void undo() {
        fParent.getFolders().remove(fFolder);
        
        // Fire this event so the tree view can select a parent node
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                ITreeModelView.PROPERTY_SELECTION_CHANGED, null, fParent);
    }
}
