/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.p2.P2;
import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Cleans and resets the Workbench state
 * 
 * This happens on application exit.
 * It does these things:
 * 
 * (1) Deletes the workbench.xmi file if user chooses to reset the UI
 * (2) Deletes the .metadata folder if user chooses to reset the UI and all user preferences
 * (3) Deletes certain files in the config area
 * 
 * Why do we do (3)?
 * See https://github.com/archimatetool/archi/issues/429
 * Mainly because Eclipse caches the location of bundles using absolute file paths. If you move the application or dropins location it gets out of sync.
 * 
 * So we delete some key files in the config area when exiting Archi.
 * 
 * In order for (3) to work we set an option in Archi.ini:
 * 
 * -cleanConfig
 * 
 * But it means that we must not also set the "-clean" option (this cleans osgi files at startup) as this can cause problems.
 * 
 * Here's an edge case on Windows/Linux if Archi -clean is set:
 * - Archi is launched. Clean is done.
 * - Archi is launched from desktop again as a second instance. App will exit but Clean is done again.
 * - Then user uninstalls some Archi plug-ins and restarts Archi.
 * - Lots of errors because of missing cached files that were in the osgi folder but the second clean action deleted them.
 * 
 * @author Phillip Beauvoir
 */
public class WorkbenchCleaner {
    
    private static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
    private static final String WORKBENCH_FILE = ".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi"; //$NON-NLS-1$
    
    // If this is set in Program arguments then clean the config area
    private static final String CLEAN_CONFIG = "-cleanConfig"; //$NON-NLS-1$
    
    // This over-rides -cleanConfig in case we are running from the command line and don't want to edit the Archi.ini file
    private static final String NO_CLEAN_CONFIG = "-noCleanConfig"; //$NON-NLS-1$
    
    private static final String[] CONFIG_FILES_TO_DELETE = {
            // Files
            ".baseConfigIniTimestamp", //$NON-NLS-1$
            "config.ini", //$NON-NLS-1$
            "eclipse.ini.ignored", //$NON-NLS-1$
            
            // Folders
            "org.eclipse.core.runtime", //$NON-NLS-1$
            "org.eclipse.equinox.app", //$NON-NLS-1$
            "org.eclipse.equinox.simpleconfigurator", //$NON-NLS-1$
            "org.eclipse.update", //$NON-NLS-1$
            "org.eclipse.osgi" //$NON-NLS-1$
    };
    
    // This will be generated in the parent folder of the "p2" folder
    private static final String ARTIFACTS_FILE = "artifacts.xml"; //$NON-NLS-1$

    // Clean workbench on exit flag for either full or soft reset
    private static int cleanWorkBench = -1;
    
    /**
     * Ask user if they want to reset the workbench and/or preferences.
     * If they do, create a marker file and restart.
     */
    public static void askResetWorkbench() {
        int response = MessageDialog.open(MessageDialog.QUESTION,
                Display.getCurrent().getActiveShell(),
                Messages.WorkbenchCleaner_0,
                Messages.WorkbenchCleaner_1 + "\n\n" + //$NON-NLS-1$
                Messages.WorkbenchCleaner_3,
                SWT.NONE,
                Messages.WorkbenchCleaner_4,
                Messages.WorkbenchCleaner_5,
                Messages.WorkbenchCleaner_6);
        
        // Cancel
        if(response == -1 || response == 2) {
            return;
        }
        
        if(PlatformUI.getWorkbench().restart()) { // User might have cancelled on a dirty editor
            // Set this here as a flag for later when the app is closed so we can delete the files afterwards
            cleanWorkBench = response;
        }
    }
    
    static void clean(boolean isRestart) {
        cleanWorkbench();
        cleanConfigOnExit(isRestart);
    }
    
    /**
     * Clean the workbench if requested
     */
    static void cleanWorkbench() {
        // Not set
        if(cleanWorkBench == -1) {
            return;
        }
        
        File instanceLocationFolder = getLocationAsFile(Platform.getInstanceLocation());
        if(instanceLocationFolder != null) {
            // Just delete the workbench.xmi file
            if(cleanWorkBench == 0) {
                delete(new File(instanceLocationFolder, WORKBENCH_FILE));
            }
            // Delete the whole .metadata folder
            else if(cleanWorkBench == 1) {
                delete(new File(instanceLocationFolder, METADATA_FOLDER));
            }
        }
    }

    /**
     * Clean the config area on exit
     * See https://github.com/archimatetool/archi/issues/429
     */
    public static void cleanConfigOnExit(boolean isRestart) {
        // Don't clean if we are in development mode or the "-noCleanConfig" option is set or "-cleanConfig" option is not set
        List<String> appArgs = Arrays.asList(Platform.getApplicationArgs());
        
        if(Platform.inDevelopmentMode() || appArgs.contains(NO_CLEAN_CONFIG) || !appArgs.contains(CLEAN_CONFIG)) {
            return;
        }
        
        File configLocationFolder = getLocationAsFile(Platform.getConfigurationLocation());
        File installationFolder = getLocationAsFile(Platform.getInstallLocation());
        
        if(configLocationFolder == null || installationFolder == null) {
            return;
        }

        // Don't delete anything if the config folder is in the Archi installation folder and called "configuration"
        if(configLocationFolder.getName().equals("configuration") && installationFolder.equals(configLocationFolder.getParentFile())) { //$NON-NLS-1$
            return;
        }

        // Clean config area using this method if using dropins
        if(P2.USE_DROPINS) {
            File p2Folder = P2.getP2Location(); // Get this before running the shutdown hook

            Runnable runnable = () -> {
                // Delete config files
                for(String path : CONFIG_FILES_TO_DELETE) {
                    delete(new File(configLocationFolder, path));
                }

                // Delete p2 folder
                // But check it's not in the installation folder
                if(p2Folder != null && !installationFolder.equals(p2Folder.getParentFile())) {
                    delete(p2Folder);
                }

                // The "artifacts.xml" file is generated by Eclipse in the parent folder of the configuration folder
                File artifactsFile = new File(configLocationFolder.getParentFile(), ARTIFACTS_FILE);

                // But make sure it's not the one in the installation folder
                if(!installationFolder.equals(artifactsFile.getParentFile())) {
                    delete(artifactsFile);
                }
            };
            
            // Mac does not call shutdown hooks on Restart so run this now.
            // Note, not all files will be deleted in this case.
            if(PlatformUtils.isMac() && isRestart) {
                runnable.run();
            }
            // Add a shutdown hook to delete files after Java has exited
            else {
                Runtime.getRuntime().addShutdownHook(new Thread((runnable)));
            }
        }
    }
    
    /**
     * @return an osgi data Location as a File or null
     */
    private static File getLocationAsFile(Location location) {
        if(location == null || location.getURL() == null) {
            return null;
        }
        
        return new File(location.getURL().getPath());
    }
    
    private static void delete(File file) {
        if(file.isDirectory()) {
            deleteFolder(file);
        }
        else {
            file.delete();
        }
    }
    
    private static void deleteFolder(File folder) {
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isFile()) {
                        file.delete();
                    }
                    else if(file.isDirectory()) {
                        deleteFolder(file);
                    }
                }
            }
            
            folder.delete();
        }
    }

}
