/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;

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
        // setActionDefinitionId(ArchimateEditorActionFactory.REPLACE.getCommandId()); // Ensures key binding is displayed
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
		
		// If, however, the list of selected items contains at least on Archimate Object or Connection, we can rename those.
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
		// TODO: Copy from DeleteFromModel action... 
		
		// TODO: Ask for input and output
		MessageDialog.openQuestion(	
				Display.getDefault().getActiveShell(),
				"Tickles",
				"Tickles!");
	        
		
		// TODO: Actually do something... 
		//DuplicateCommandHandler cmdHandler = new DuplicateCommandHandler(selection.toArray());
		// cmdHandler.duplicate();
	}

}
