/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.action.Action;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;



/**
 * Enable or disable the grid. It does not show or hide it.
 * 
 * @author Phillip Beauvoir
 */
public class ToggleGridEnabledAction extends Action {

    public ToggleGridEnabledAction() {
        super(Messages.ToggleGridEnabledAction_0, AS_CHECK_BOX);
        setToolTipText(Messages.ToggleGridEnabledAction_0);
        setId(SnapToGrid.PROPERTY_GRID_ENABLED);
        setActionDefinitionId(SnapToGrid.PROPERTY_GRID_ENABLED);
        setChecked(isChecked());
    }

    @Override
    public boolean isChecked() {
        return Preferences.isGridSnap();
    }

    @Override
    public void run() {
        Preferences.STORE.setValue(IPreferenceConstants.GRID_SNAP, !isChecked());
    }
}
