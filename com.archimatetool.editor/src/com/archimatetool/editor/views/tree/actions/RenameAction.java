/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.archimatetool.editor.views.tree.TreeModelViewer;
import com.archimatetool.editor.views.tree.commands.RenameCommandHandler;



/**
 * Rename Action
 * 
 * @author Phillip Beauvoir
 */
public class RenameAction extends ViewerAction {
    
    public RenameAction(TreeModelViewer selectionProvider) {
        super(selectionProvider);
        setText(Messages.RenameAction_0);
        setEnabled(false);
        setActionDefinitionId(IWorkbenchCommandConstants.FILE_RENAME); // register key binding
    }
    
    @Override
    public void run() {
        IStructuredSelection selection = getSelection();
        if(selection == null || selection.isEmpty()) {
            return;
        }

        Object element = selection.getFirstElement();
        if(RenameCommandHandler.canRename(element)) {
            ((TreeModelViewer)getSelectionProvider()).editElement(element);
        }
    }

    @Override
    public void update() {
        setEnabled(getSelection().size() == 1 && RenameCommandHandler.canRename(getSelection().getFirstElement()));
    }

}
