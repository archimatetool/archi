/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;



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
    public static String ID = ArchimateEditorPlugin.PLUGIN_ID + ".app"; //$NON-NLS-1$
    
	/**
	 * Constructor
	 */
	public Application() {
	}
	
	public Object start(IApplicationContext context) throws Exception {
	    /*
	     * Platform specific startup if user launches app twice or from .archimate file on the desktop
	     */
	    IPlatformLauncher launcher = ArchimateEditorPlugin.INSTANCE.getPlatformLauncher();
	    if(launcher != null) {
	        launcher.startup();
	        
            /*
             * If the application is already open (Windows), exit
             */
	        if(launcher.shouldApplicationExitEarly()) {
                return EXIT_OK;
            }
	    }
	    
	    // Create Main Display
	    Display display = PlatformUI.createDisplay();
	    
	    // Tell the Launcher if needed (Mac)
	    if(launcher != null) {
	        launcher.displayCreated(display);
	    }
	    	    
	    try {
	        int code = PlatformUI.createAndRunWorkbench(display, new ArchimateEditorWorkbenchAdvisor());
	        // Exit the application with an appropriate return code
	        return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
	    }
	    finally {
	        display.dispose();
	    }
	}
	
	
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
