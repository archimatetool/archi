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
        if(isEnabled()) {
            try {
                Desktop.getDesktop().open(ArchiPlugin.INSTANCE.getUserDataFolder());
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
        // Check AWT Desktop is supported. Some Linux Wayland versions don't support it or need X11. 
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.OPEN) && folder != null && folder.exists();
    }
}
