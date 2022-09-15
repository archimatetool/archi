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
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;



/**
 * Open Archi Data folder
 * 
 * @author Phillip Beauvoir
 */
public class OpenDataFolderHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        File folder = getInstanceLocation();
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
    
    private File getInstanceLocation() {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null && instanceLoc.getURL() != null) {
            return new File(Platform.getInstanceLocation().getURL().getPath());
        }
        return null;
    }
    
    @Override
    public boolean isEnabled() {
        File folder = getInstanceLocation();
        return folder != null && folder.exists() && Desktop.getDesktop().isSupported(Action.OPEN);
    }
}
