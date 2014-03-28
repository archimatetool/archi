/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.actions.ArchimateEditorActionFactory;

/**
 * Replace action
 * 
 * This action allows for the user to rename all elements in the current
 * selection, based on a simple string replace.
 * 
 * @author madsdyd
 * 
 */
public class ReplaceAction extends ViewerAction implements UpdateAction {

	public ReplaceAction(ISelectionProvider selectionProvider) {
		super(selectionProvider);
		setText(Messages.ReplaceAction_0);
		setId(ArchimateEditorActionFactory.REPLACE.getId());

		setActionDefinitionId(ArchimateEditorActionFactory.REPLACE.getCommandId()); // Ensures key binding is displayed
		setEnabled(false);
	}

	@Override
	public void run() {
		IStructuredSelection selection = getSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}

		// TODO: Ask for input and output
		MessageDialog.openQuestion(	
				Display.getDefault().getActiveShell(),
				"Tickles",
				"Tickles!");
	        
		
		// TODO: Actually do something... 
		//DuplicateCommandHandler cmdHandler = new DuplicateCommandHandler(selection.toArray());
		// cmdHandler.duplicate();
	}

	@Override
	public void update(IStructuredSelection selection) {
		// TODO: Fix this.
		// setEnabled(DuplicateCommandHandler.canDuplicate(selection));
		setEnabled( true );
	}

	@Override
	public void update() {
		// TODO: Fix this
		setEnabled( true );
		// TODO Auto-generated method stub
		
	}

}
