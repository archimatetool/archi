/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.program.Program;

import com.archimatetool.editor.ArchiPlugin;



/**
 * Open Archi Data folder
 * 
 * @author Phillip Beauvoir
 */
public class OpenDataFolderHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if(isEnabled()) {
            Program.launch(ArchiPlugin.getInstance().getUserDataFolder().getAbsolutePath());
        }
        
        return null;
    }
    
    @Override
    public boolean isEnabled() {
        File folder = ArchiPlugin.getInstance().getUserDataFolder();
        return folder != null && folder.exists();
    }
}
