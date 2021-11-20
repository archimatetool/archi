/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.views.tree.TreeModelViewer;
import com.archimatetool.editor.views.tree.commands.DeleteCommandHandler;



/**
 * Delete Action
 * 
 * @author Phillip Beauvoir
 */
public class DeleteAction extends ViewerAction {
    
    public DeleteAction(TreeModelViewer selectionProvider) {
        super(selectionProvider);
        setText(Messages.DeleteAction_0);
        
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
        
        DeleteCommandHandler cmdHandler = new DeleteCommandHandler((TreeModelViewer)getSelectionProvider(),
                selection.toArray());

        // If the objects are referenced in a diagram then warn
        if(cmdHandler.hasDiagramReferences()) {
            if(!MessageDialog.openQuestion(
                    Display.getDefault().getActiveShell(),
                    Messages.DeleteAction_1,
                    Messages.DeleteAction_2 +
                    "\n" + //$NON-NLS-1$
                    Messages.DeleteAction_3)) {
                        return;
            }
        }
        // Else if preference is set to warn user in all cases
        else if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SHOW_WARNING_ON_DELETE_FROM_TREE)) {
            if(!MessageDialog.openQuestion(
                    Display.getDefault().getActiveShell(),
                    Messages.DeleteAction_1,
                    Messages.DeleteAction_3)) {
                        return;
            }
        }
        
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                cmdHandler.delete();
            }
        });
    }

    @Override
    public void update() {
        setEnabled(false);
        for(Object element : getSelection().toList()) {
            if(DeleteCommandHandler.canDelete(element)) { // At least one element can be deleted
                setEnabled(true);
                break;
            }
        }
    }

}
