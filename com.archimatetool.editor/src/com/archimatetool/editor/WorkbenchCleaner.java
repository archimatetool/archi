/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.p2.P2;


/**
 * Cleans and resets the Workbench state
 * 
 * @author Phillip Beauvoir
 */
public class WorkbenchCleaner {
    
    private static final String SOFT_RESET_FILE = ".softreset"; //$NON-NLS-1$
    private static final String HARD_RESET_FILE = ".hardreset"; //$NON-NLS-1$
    
    private static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
    private static final String WORKBENCH_FILE = ".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi"; //$NON-NLS-1$
    
    // If this is set in VM arguments as "-DcleanConfig=false" then don't clean the config area
    private static final String CLEAN_CONFIG = "cleanConfig"; //$NON-NLS-1$
    
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

            // "org.eclipse.osgi" Not this one as -clean launch option will delete it 
    };
    
    // This will be generated in the parent folder of the "p2" folder
    private static final String ARTIFACTS_FILE = "artifacts.xml"; //$NON-NLS-1$
    
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
        
        // Hard reset
        boolean hardReset = response == 1;
        
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            try {
                if(PlatformUI.getWorkbench().restart()) { // User might have cancelled on a dirty editor
                    File resetFile = new File(instanceLoc.getURL().getPath(), hardReset ? HARD_RESET_FILE : SOFT_RESET_FILE);
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
     * Clean the workbench and the config area
     */
    public static void clean(IApplicationContext context) throws IOException {
        // If reset file exists clean the workbench
        cleanWorkbench();
        
        // Is the "-clean" (osgi.clean) option set?
        // We should only clean the config files if it is set, else the "org.eclipse.osgi" folder will be re-populated with new bundle folders each launch
        String osgiClean = context.getBrandingBundle().getBundleContext().getProperty("osgi.clean"); //$NON-NLS-1$
        
        // Delete config and P2 files if running as a deployed product
        // And the "-clean" option is set
        // And "-DcleanConfig=false" is not set
        if(!Platform.inDevelopmentMode() && osgiClean != null && !"false".equals(System.getProperty(CLEAN_CONFIG))) { //$NON-NLS-1$
            cleanConfig();
        }
    }
    
    private static void cleanWorkbench() throws IOException {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            File instanceLocationFolder = new File(instanceLoc.getURL().getPath());
            
            // If hard reset file exists delete the .metadata folder
            File hardResetFile = new File(instanceLocationFolder, HARD_RESET_FILE);
            if(hardResetFile.exists()) {
                delete(hardResetFile);
                delete(new File(instanceLocationFolder, METADATA_FOLDER));
                return;
            }
            
            // If soft reset file exists delete the workbench.xmi file
            File softResetFile = new File(instanceLocationFolder, SOFT_RESET_FILE);
            if(softResetFile.exists()) {
                delete(softResetFile);
                delete(new File(instanceLocationFolder, WORKBENCH_FILE));
            }
        }
    }
    
    /**
     * Clean the config area
     * See https://github.com/archimatetool/archi/issues/429
     */
    private static void cleanConfig() throws IOException {
        // Clean config area if using dropins
        if(P2.USE_DROPINS) {
            Location configLoc = Platform.getConfigurationLocation();
            if(configLoc != null) {
                File configLocationFolder = new File(configLoc.getURL().getPath());
                File installationFolder = new File(Platform.getInstallLocation().getURL().getPath());
                
                // Don't delete anything if the config folder is in the Archi installation folder and called "configuration"
                if(configLocationFolder.getName().equals("configuration") && installationFolder.equals(configLocationFolder.getParentFile())) { //$NON-NLS-1$
                    return;
                }
                
                // Delete config files
                for(String path : CONFIG_FILES_TO_DELETE) {
                    delete(new File(configLocationFolder, path));
                }
                
                // Delete p2 folder
                File p2Folder = getP2Location();
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
            }
        }
    }
    
    /**
     * Get the location of the "p2" folder as set in "eclipse.p2.data.area" in Archi.ini, or if not set there, in config.ini
     * Found a clue in https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/tree/plugins/org.eclipse.oomph.p2.core/src/org/eclipse/oomph/p2/internal/core/ProvisioningAgentProvider.java
     */
    private static File getP2Location() throws IOException {
        // We can either get the IAgentLocation as a public static field
        @SuppressWarnings("restriction")
        IAgentLocation agentDataLocation = org.eclipse.equinox.internal.p2.core.Activator.agentDataLocation;
        if(agentDataLocation != null) {
            // Normalise the uri in case it has ".." in the path. If it does File#equals(File) doesn't work
            return new File(agentDataLocation.getRootLocation()).getCanonicalFile();
            // Alternate method
            //return Paths.get(agentDataLocation.getRootLocation()).normalize().toFile();
        }
        
        // Or like this...
//        @SuppressWarnings("restriction")
//        BundleContext context = org.eclipse.equinox.internal.p2.core.Activator.getContext();
//        ServiceReference<IAgentLocation> serviceReference = context.getServiceReference(IAgentLocation.class);
//        IAgentLocation agentDataLocation = context.getService(serviceReference);
//        context.ungetService(serviceReference); // have to do this I think
//        if(agentDataLocation != null) {
//            return new File(agentDataLocation.getRootLocation()).getCanonicalFile();
//        }
        
        return null;
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
