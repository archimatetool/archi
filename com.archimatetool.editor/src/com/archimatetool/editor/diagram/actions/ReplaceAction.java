/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.ReplaceCommand;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.INameable;

/**
 * Replace action
 * 
 * This action allows for the user to rename all elements in the current
 * selection, based on a simple string replace.
 * 
 * TODO: I have no clue if this is the right place to make this class...
 * 
 * @author Mads Bondo Dydensborg
 * 
 */
public class ReplaceAction extends SelectionAction {

	public static final String ID = "ReplaceAction"; //$NON-NLS-1$
	public static final String TEXT = Messages.ReplaceAction_0;

	public ReplaceAction(IWorkbenchPart part) {
		super(part);
		setText(TEXT);
		setId(ID);
		// setId(ArchimateEditorActionFactory.REPLACE.getId());
		// setActionDefinitionId(ArchimateEditorActionFactory.REPLACE.getCommandId());
		// // Ensures key binding is displayed
		// setEnabled(false);
	}

	/**
	 * Figure out if this action is enabled.
	 * 
	 * TODO: Modelled after the DeleteFromModelAction. No clue if it is correct.
	 * 
	 * @return true if the action should be enabled, false otherwise.
	 */
	@Override
	protected boolean calculateEnabled() {
		List<?> list = getSelectedObjects();

		// Obviously, if nothing is selected, this command is not eabled.
		if (list.isEmpty()) {
			return false;
		}

		// If, however, the list of selected items contains at least on
		// Archimate Object or Connection, we can rename those.
		// TODO: We may need to restrict this?
		for (Object object : list) {
			if (object instanceof EditPart) {
				Object model = ((EditPart) object).getModel();
				if (model instanceof IDiagramModelArchimateObject
						|| model instanceof IDiagramModelArchimateConnection) {
					return true;
				}
			}
		}

		// Nothing found => disable this action.
		return false;
	}

	@Override
	public void run() {
		// Get the selected objects, figure out the to and from patterns, create
		// and execute a compound command to execute them
		List<?> selection = getSelectedObjects();

		// TODO: Ask for input and output
		MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
				"Tickles", "Tickles!");
		// TODO: Modelled after the FontColorAction - no idea if appropiate.
		String from = "from";
		String to = "to";

		// TODO: Check if dialog was OK.
		if (true) {
			execute(createCommand(selection, from, to));
		}

		// TODO: Actually do something...
		// DuplicateCommandHandler cmdHandler = new
		// DuplicateCommandHandler(selection.toArray());
		// cmdHandler.duplicate();
	}

	/**
	 * Is this object valid to change for this operation?
	 * 
	 * TODO: Modelled after FontColorAction - not sure if relevant. Also, this
	 * seems to honour "isLocked". Not sure if it should, but seems proper.
	 * 
	 * @param object
	 *            The object to check for
	 * @return true if valid for this action, false otherwise.
	 */
	private boolean isValidEditPart(Object object) {
		if (object instanceof EditPart
				&& ((EditPart) object).getModel() instanceof INameable) {
			Object model = ((EditPart) object).getModel();
			if (model instanceof ILockable) {
				return !((ILockable) model).isLocked();
			}
			return true;
		}
		return false;
	}

	/**
	 * Create a compound command that does all the changing of the names
	 * 
	 * TODO: Modelled after FontColorAction.
	 * 
	 * @param selection
	 *            The selection to change names in
	 * @param from
	 *            The string (potentially Regular Expression) to search for and
	 *            replace with
	 * @param to
	 *            The string to substitute for matches to from
	 * @return
	 */
	private Command createCommand(List<?> selection, String from, String to) {
		CompoundCommand result = new CompoundCommand(Messages.ReplaceAction_1);

		for (Object object : selection) {
			if (isValidEditPart(object)) {
				EditPart editPart = (EditPart) object;
				String currentName = ((INameable) editPart.getModel())
						.getName();
				String newName = currentName.replaceAll(from, to);

				Command cmd = new ReplaceCommand(
						(INameable) editPart.getModel(), newName);
				if (cmd.canExecute()) {
					result.add(cmd);
				}
			}
		}

		return result.unwrap();
	}

}
