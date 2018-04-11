/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.views.tree.commands.DuplicateCommandHandler;



/**
 * Duplicate Action
 * 
 * @author Phillip Beauvoir
 */
public class DuplicateAction extends ViewerAction {
    
    public DuplicateAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.DuplicateAction_0);
        
        setActionDefinitionId(ArchiActionFactory.DUPLICATE.getCommandId()); // Ensures key binding is displayed
        setEnabled(false);
    }
    
    @Override
    public void run() {
        IStructuredSelection selection = getSelection();
        if(selection == null || selection.isEmpty()) {
            return;
        }
        
        DuplicateCommandHandler cmdHandler = new DuplicateCommandHandler(selection.toArray());
        cmdHandler.duplicate();
    }

    @Override
    public void update() {
        setEnabled(DuplicateCommandHandler.canDuplicate(getSelection()));
    }

}
