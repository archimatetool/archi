/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.views.tree.commands.SortFolderCommand;
import com.archimatetool.model.IFolder;



/**
 * Sort Folder Action
 * 
 * @author Phillip Beauvoir
 */
public class SortFolderAction extends ViewerAction {
    
    public SortFolderAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.SortFolderAction_0);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_SORT_16));
    }
    
    @Override
    public void run() {
        Object selected = getSelection().getFirstElement();
        
        if(selected instanceof IFolder) {
            IFolder folder = (IFolder)selected;
            Command cmd = new SortFolderCommand(folder);
            CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
            commandStack.execute(cmd);
        }
    }

    @Override
    public void update(IStructuredSelection selection) {
        Object selected = selection.getFirstElement();
        
        if(selected instanceof IFolder) {
            setEnabled(((IFolder)selected).getElements().size() > 1);
        }
        else {
            setEnabled(false);
        }
    }

}
