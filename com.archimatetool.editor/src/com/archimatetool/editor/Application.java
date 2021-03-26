/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.utils.PlatformUtils;




/**
 * The main application class for standalone RCP operation.
 * 
 * @author Phillip Beauvoir
 */
public class Application
implements IApplication {

    /**
     * ID of the Application
     */
    public static String ID = ArchiPlugin.PLUGIN_ID + ".app"; //$NON-NLS-1$
    
	/**
	 * Constructor
	 */
	public Application() {
	}
	
	@Override
    public Object start(IApplicationContext context) throws Exception {
        // If running on Linux lock the instance location so we can only launch Archi once
        if(PlatformUtils.isLinux()) {
            Location loc = Platform.getInstanceLocation();
            
            if(loc.isLocked()) {
                return EXIT_OK;
            }            
            
            if(loc.isSet()) { // Ensure it's set to avoid IOException
                loc.lock(); // Lock it
            }
        }

        // Platform specific launcher check
        IPlatformLauncher launcher = ArchiPlugin.INSTANCE.getPlatformLauncher();
        if(launcher != null) {
            launcher.startup();
            
            // If the application is already open (Windows), exit
            if(launcher.shouldApplicationExitEarly()) {
                return EXIT_OK;
            }
	    }
	    
	    // Create Main Display
	    Display display = PlatformUI.createDisplay();
	    
	    // Tell the Launcher that the display has been created
	    if(launcher != null) {
	        launcher.displayCreated(display);
	    }
	    
	    // Hook into opening documents from the desktop
	    OpenDocumentHandler.getInstance().hook(display);
	    	    
	    try {
	        // Create and Run the Workbench
	        int returnCode = PlatformUI.createAndRunWorkbench(display, new ArchiWorkbenchAdvisor());

	        // Was it a restart?
	        boolean restart = returnCode == PlatformUI.RETURN_RESTART;
	        
	        // Clean Workbench on exit/restart
	        WorkbenchCleaner.clean(restart);
            
	        if(restart) {
                return EXIT_RESTART;
            }
	        
	        return EXIT_OK;
	    }
	    finally {
	        display.dispose();
	    }
	}
	
	
    @Override
    public void stop() {
    
    }

    /**
	 * Set the file location of the data store.<p>
	 * We want to do this for an RCP standalone app but not when this is running as a plugin
	 * becauae the location of the Workbench instance will be set already.
	 * This has to be done before the Workbench starts.
	 * Note that the launch configuration in the Eclipse IDE should have "@noDefault"
     * as the workspace for the launch configuration.
	 * 
	 * @param url A url to a folder
     * @throws IOException 
     * @throws IllegalStateException 
	 */
	@SuppressWarnings("unused")
    private void setWorkbenchDataLocation(URL url) throws IllegalStateException, IOException {
        /*
         * Ascertain the existing location. If it is null, we can set it.
         * When run from the Eclipse IDE, setting the workspace location in the launch configuration
         * to "@noDefault" will set it as null.
         */
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logError(
                    "Instance Location is null, cannot set it in setWorkbenchDataLocation(URL)", //$NON-NLS-1$
                    null);
        }
        else if(!instanceLoc.isSet()) {
            instanceLoc.release();
            /*
             * If this is set to true, you can't run another instance
             * of the app with this workspace open
             */ 
            instanceLoc.set(url, false);  
        }
	}
}
