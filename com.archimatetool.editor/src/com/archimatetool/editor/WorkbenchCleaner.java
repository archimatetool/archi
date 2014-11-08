/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


/**
 * Cleans and resets the Workbench state
 * 
 * @author Phillip Beauvoir
 */
public class WorkbenchCleaner {
    
    static final String RESET_FILE = ".reset"; //$NON-NLS-1$
    static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$

    /**
     * Ask user if they want to reset the workbench.
     * If they do, create a marker file and restart.
     */
    public static void reset() {
        boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                Messages.WorkbenchCleaner_0,
                Messages.WorkbenchCleaner_1);
        if(!result) {
            return;
        }
        
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            try {
                if(PlatformUI.getWorkbench().restart()) { // User might have cancelled on a dirty editor
                    File resetFile = new File(instanceLoc.getURL().getPath(), RESET_FILE);
                    resetFile.createNewFile();
                }
            }
            catch(Exception ex) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.WorkbenchCleaner_2, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Check if the marker file exists to clean.
     * If it does, delete the marker file, the .metadata folder and the .config folder
     * @throws IOException
     */
    public static void checkForReset() throws IOException {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            File resetFile = new File(instanceLoc.getURL().getPath(), RESET_FILE);
            if(resetFile.exists()) {
                // delete reset file
                resetFile.delete();
                
                // delete .metadata folder
                deleteFolder(new File(instanceLoc.getURL().getPath(), METADATA_FOLDER));
                
                // delete .config folder
                Location configLoc = Platform.getConfigurationLocation();
                if(configLoc != null) {
                    deleteFolder(new File(configLoc.getURL().getPath()));
                }
            }
        }
    }
    
    private static void deleteFolder(File folder) throws IOException {
        if(folder.exists() && folder.isDirectory()) {
            for(File file : folder.listFiles()) {
                // Don't delete user prefs
                if(file.isFile() && !file.getName().startsWith("com.archimatetool")) { //$NON-NLS-1$
                    file.delete();
                }
                else if(file.isDirectory()) {
                    deleteFolder(file);
                }
            }
            
            folder.delete();
        }
    }

}
