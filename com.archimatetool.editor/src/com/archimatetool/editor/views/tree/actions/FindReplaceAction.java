/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.ui.findreplace.FindReplaceDialog;
import com.archimatetool.editor.views.tree.TreeModelView;



/**
 * Find/Replace Action
 * 
 * @author Phillip Beauvoir
 */
public class FindReplaceAction extends Action {
    
    private IWorkbenchWindow window;

    public FindReplaceAction(TreeModelView view) {
        window = view.getViewSite().getWorkbenchWindow();
    }
    
    @Override
    public void run() {
        FindReplaceDialog dialog = FindReplaceDialog.getInstance(window);
        if(dialog != null) {
            dialog.open();
        }
    }

}
