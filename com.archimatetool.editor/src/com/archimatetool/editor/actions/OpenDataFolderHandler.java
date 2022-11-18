/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ArchiPlugin;



/**
 * Open Archi Data folder
 * 
 * @author Phillip Beauvoir
 */
public class OpenDataFolderHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        File folder = ArchiPlugin.INSTANCE.getUserDataFolder();
        if(folder != null) {
            try {
                Desktop.getDesktop().open(folder);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return null;
    }
    
    @Override
    public boolean isEnabled() {
        File folder = ArchiPlugin.INSTANCE.getUserDataFolder();
        return folder != null && folder.exists() && Desktop.getDesktop().isSupported(Action.OPEN);
    }
}
