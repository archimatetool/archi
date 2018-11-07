/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.ui.dialog.AboutDialog;


/**
 * Opens the "About" dialog
 * 
 * @author Phillip Beauvoir
 */
public class AboutHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        new AboutDialog(HandlerUtil.getActiveShellChecked(event)).open();
        return null;
    }

}
