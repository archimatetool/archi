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
 * Toggle the Visibility of the Grid.
 * 
 * @author Phillip Beauvoir
 */
public class ToggleGridVisibleAction extends Action {

    public ToggleGridVisibleAction() {
        super(Messages.ToggleGridVisibleAction_0, AS_CHECK_BOX);
        setToolTipText(Messages.ToggleGridVisibleAction_0);
        setId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setActionDefinitionId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setChecked(isChecked());
    }

    @Override
    public boolean isChecked() {
        return ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.GRID_VISIBLE);
    }

    @Override
    public void run() {
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.GRID_VISIBLE, !isChecked());
    }
}
