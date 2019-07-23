/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * @author Phillip Beauvoir
 */
public class ManagePluginsActionHandler extends AbstractHandler {
    
    private Shell shell;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        shell = HandlerUtil.getActiveShellChecked(event);
        
        if(P2.USE_DROPINS) {
            new DropinsPluginDialog(shell).open();
        }
        else {
            new P2PluginDialog(shell).open();
        }
        
        return null;
    }
    
}
