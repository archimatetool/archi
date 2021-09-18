/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;



/**
 * Toggle the Snap Guide Lines
 * 
 * @author Phillip Beauvoir
 */
public class ToggleSnapToAlignmentGuidesAction extends Action {

	public ToggleSnapToAlignmentGuidesAction() {
		super(Messages.ToggleSnapToAlignmentGuidesAction_0, AS_CHECK_BOX);
		setToolTipText(getText());
		setId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
		setActionDefinitionId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
		setChecked(isChecked());
	}

	@Override
    public boolean isChecked() {
	    return ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.GRID_SHOW_GUIDELINES);
	}

	@Override
    public void run() {
	    ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.GRID_SHOW_GUIDELINES, !isChecked());
	}
}
