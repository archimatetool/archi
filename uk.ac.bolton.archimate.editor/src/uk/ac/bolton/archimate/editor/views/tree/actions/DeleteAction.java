/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.views.tree.commands.DeleteCommandHandler;


/**
 * Delete Action
 * 
 * @author Phillip Beauvoir
 */
public class DeleteAction extends ViewerAction {
    
    public DeleteAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText("&Delete");
        
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setActionDefinitionId("org.eclipse.ui.edit.delete"); // Ensures key binding is displayed //$NON-NLS-1$
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        setEnabled(false);
    }
    
    @Override
    public void run() {
        IStructuredSelection selection = getSelection();
        if(selection == null || selection.isEmpty()) {
            return;
        }
        
        DeleteCommandHandler cmdHandler = new DeleteCommandHandler(getSelectionProvider(), selection.toArray());

        /*
         * If the objects are referenced in a diagram warn user
         */
        if(cmdHandler.hasDiagramReferences()) {
            if(!MessageDialog.openQuestion(
                    Display.getDefault().getActiveShell(),
                    "Delete",
                    "One or more of the elements you have selected is referenced on one or more Views.\n\n" +
                    "Are you sure you want to delete the selected element(s)?")) {
                        return;
            }
        }
        
        cmdHandler.delete();
    }

    @Override
    public void update(IStructuredSelection selection) {
        setEnabled(false);
        for(Object element : selection.toList()) {
            if(DeleteCommandHandler.canDelete(element)) { // At least one element can be deleted
                setEnabled(true);
                break;
            }
        }
    }

}
