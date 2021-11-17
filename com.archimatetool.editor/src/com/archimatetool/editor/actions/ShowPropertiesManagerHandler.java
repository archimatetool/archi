/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.propertysections.UserPropertiesManagerDialog;
import com.archimatetool.model.IArchimateModel;


/**
 * Show Properties Manager
 * 
 * @author Phillip Beauvoir
 */
public class ShowPropertiesManagerHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            UserPropertiesManagerDialog dialog = new UserPropertiesManagerDialog(HandlerUtil.getActiveShell(event), model);
            dialog.open();
        }
        
        return null;
    }

}
