/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.IAgentLocation;
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
@SuppressWarnings("nls")
public class WorkbenchCleaner {
    
    static final String RESET_FILE = ".reset";
    
    static final String WORKBENCH_FILE = ".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi";
    
    // If this is set in VM arguments as -DcleanConfig=false then don't clean these
    static final String CLEAN_CONFIG = "cleanConfig";
    
    static final String[] CONFIG_FILES_TO_DELETE = {
            // Files
            ".baseConfigIniTimestamp",
            "config.ini",
            "eclipse.ini.ignored",
            
            // Folders
            "org.eclipse.core.runtime",
            "org.eclipse.equinox.app",
            "org.eclipse.equinox.simpleconfigurator",
            "org.eclipse.update",

            // "org.eclipse.osgi" Not this one as -clean launch option will delete it 
    };
    
    // This will be generated in the parent folder of the "p2" folder
    static final String ARTIFACTS_FILE = "artifacts.xml";
    
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
        // If reset file exists clean workbench.xmi file
        cleanWorkbench();
        
        // Config and P2 files
        cleanConfig();
    }
    
    private static void cleanWorkbench() throws IOException {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            File instanceLocationFolder = new File(instanceLoc.getURL().getPath());
            // If reset file exists clean workbench.xmi file
            File resetFile = new File(instanceLocationFolder, RESET_FILE);
            if(resetFile.exists()) {
                delete(resetFile);
                delete(new File(instanceLocationFolder, WORKBENCH_FILE));
            }
        }
    }
    
    /**
     * Clean the config area
     * See https://github.com/archimatetool/archi/issues/429
     */
    private static void cleanConfig() throws IOException {
        // If System Property set to false then don't clean these files
        if("false".equals(System.getProperty(CLEAN_CONFIG))) { //$NON-NLS-1$
            return;
        }
        
        // Clean config area if using dropins
        if(P2.USE_DROPINS) {
            Location configLoc = Platform.getConfigurationLocation();
            if(configLoc != null) {
                File configLocationFolder = new File(configLoc.getURL().getPath());
                File installationFolder = new File(Platform.getInstallLocation().getURL().getPath());
                
                // Don't delete anything if the config folder is in the Archi installation folder and called "configuration"
                if(configLocationFolder.getName().equals("configuration") && installationFolder.equals(configLocationFolder.getParentFile())) {
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
