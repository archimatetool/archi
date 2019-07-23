/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.p2.P2;


/**
 * Cleans and resets the Workbench state
 * 
 * @author Phillip Beauvoir
 */
public class WorkbenchCleaner {
    
    static final String RESET_FILE = ".reset"; //$NON-NLS-1$
    
    static final String WORKBENCH_FILE = ".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi"; //$NON-NLS-1$
    
    static final String[] P2_FILES_TO_DELETE = {
            "p2", //$NON-NLS-1$
            "artifacts.xml" //$NON-NLS-1$
    };

    static final String[] CONFIG_FILES_TO_IGNORE = {
            "org.eclipse.osgi", //$NON-NLS-1$
            "dev.properties" //$NON-NLS-1$
    };
    
    /**
     * Ask user if they want to reset the workbench.
     * If they do, create a marker file and restart.
     */
    public static void askResetWorkbench() {
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
     * Clean the crap out of the instance location folder, config area and workbench.xmi
     */
    public static void clean() throws IOException {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            File instanceLocationFolder = new File(instanceLoc.getURL().getPath());
            
            // Workspace files if using dropins
            cleanInstanceLocation(instanceLocationFolder);
            
            // If reset file exists clean workbench.xmi file
            cleanWorkbench(instanceLocationFolder);
        }
        
        cleanConfig();
    }
    
    private static void cleanInstanceLocation(File instanceLocationFolder) throws IOException {
        // P2 files if using dropins
        if(P2.USE_DROPINS) {
            for(String path : P2_FILES_TO_DELETE) {
                delete(new File(instanceLocationFolder, path));
            }
        }
    }
    
    private static void cleanWorkbench(File instanceLocationFolder) throws IOException {
        // If reset file exists clean workbench.xmi file
        File resetFile = new File(instanceLocationFolder, RESET_FILE);
        if(resetFile.exists()) {
            delete(resetFile);
            delete(new File(instanceLocationFolder, WORKBENCH_FILE));
        }
    }
    
    private static void cleanConfig() throws IOException {
        // Clean config area if using dropins
        if(P2.USE_DROPINS) {
            Location configLoc = Platform.getConfigurationLocation();
            if(configLoc != null) {
                File configLocationFolder = new File(configLoc.getURL().getPath());
                
                // Don't delete these files if the config location is a sub-folder of the app installation
                // As these are the root configuration files that ship with the app
                File installationFolder = new File(Platform.getInstallLocation().getURL().getPath());
                if(configLocationFolder.getParentFile().equals(installationFolder)) {
                    return;
                }
                
                List<String> whiteList = Arrays.asList(CONFIG_FILES_TO_IGNORE);
                
                for(File f : configLocationFolder.listFiles()) {
                    if(!whiteList.contains(f.getName())) {
                        delete(f);
                    }
                }
            }
        }
    }
    
    private static void delete(File f) throws IOException {
        if(f.isDirectory()) {
            deleteFolder(f);
        }
        else {
            f.delete();
        }
    }
    
    private static void deleteFolder(File folder) throws IOException {
        if(folder.exists() && folder.isDirectory()) {
            for(File file : folder.listFiles()) {
                if(file.isFile()) {
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
