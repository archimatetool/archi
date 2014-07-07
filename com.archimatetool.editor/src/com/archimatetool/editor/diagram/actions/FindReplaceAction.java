/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.ui.findreplace.FindReplaceDialog;



/**
 * Find/Replace Action
 * 
 * @author Phillip Beauvoir
 */
public class FindReplaceAction extends Action {
    
    private IWorkbenchWindow window;

    public FindReplaceAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ActionFactory.FIND.getId());
    }
    
    @Override
    public void run() {
        FindReplaceDialog dialog = FindReplaceDialog.getInstance(window);
        if(dialog != null) {
            dialog.open();
        }
    }

}
