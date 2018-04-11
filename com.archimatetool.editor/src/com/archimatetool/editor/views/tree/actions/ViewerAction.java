/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;



/**
 * Abstract Viewer Action
 * 
 * @author Phillip Beauvoir
 */
public abstract class ViewerAction extends Action implements IViewerAction {
    
    private ISelectionProvider fSelectionProvider;

    public ViewerAction(ISelectionProvider selectionProvider) {
        fSelectionProvider = selectionProvider;
    }
    
    protected ISelectionProvider getSelectionProvider() {
        return fSelectionProvider;
    }
    
    protected IStructuredSelection getSelection() {
        return (IStructuredSelection)getSelectionProvider().getSelection();
    }
    
    @Override
    public void update() {
        // do nothing
    }

}
