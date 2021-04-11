/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.tools.ProfilesManagerDialog;
import com.archimatetool.model.IArchimateModel;


/**
 * Show Profiles Manager
 * 
 * @author Phillip Beauvoir
 */
public class ShowProfilesManagerHandler extends AbstractModelSelectionHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            ProfilesManagerDialog dialog = new ProfilesManagerDialog(workbenchWindow.getShell(), model);
            dialog.open();
        }
        
        return null;
    }

}
