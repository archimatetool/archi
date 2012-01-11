/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.views.tree.commands.NewFolderCommand;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * New Folder Action
 * 
 * @author Phillip Beauvoir
 */
public class NewFolderAction extends ViewerAction {
    
    public NewFolderAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText("Folder");
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_FOLDER));
    }
    
    @Override
    public void run() {
        Object selected = getSelection().getFirstElement();
        
        if(selected instanceof IFolder) {
            IFolder parent = (IFolder)selected;
            
            // Create a new Folder, set its name
            IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
            folder.setName("New Folder");
            folder.setType(FolderType.USER);
            
            // Execute Command
            Command cmd = new NewFolderCommand(parent, folder);
            CommandStack commandStack = (CommandStack)parent.getAdapter(CommandStack.class);
            commandStack.execute(cmd);
        }
    }

    @Override
    public void update(IStructuredSelection selection) {
        Object selected = selection.getFirstElement();
        setEnabled(selected instanceof IFolder);
    }
}
