/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.propertysections.UserPropertiesManagerDialog;
import com.archimatetool.model.IArchimateModel;


/**
 * Show Properties Manager
 * 
 * @author Phillip Beauvoir
 */
public class ShowPropertiesManagerHandler extends AbstractModelSelectionHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            UserPropertiesManagerDialog dialog = new UserPropertiesManagerDialog(workbenchWindow.getShell(), model);
            dialog.open();
        }
        
        return null;
    }

}
