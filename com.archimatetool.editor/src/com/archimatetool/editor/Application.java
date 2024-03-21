/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.PlatformUtils;




/**
 * The main application class for standalone RCP operation.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class Application implements IApplication {

    /**
     * ID of the Application
     */
    public static String ID = ArchiPlugin.PLUGIN_ID + ".app";

    /**
     * Constructor
     */
    public Application() {
    }

    @Override
    public Object start(IApplicationContext context) throws Exception {
        // If running on Windows or Linux lock the instance location so we can only launch Archi once
        if(PlatformUtils.isWindows() || PlatformUtils.isLinux()) {
            Location loc = Platform.getInstanceLocation();

            if(loc.isLocked()) {
                return EXIT_OK;
            }

            if(loc.isSet()) { // Ensure it's set to avoid IOException
                loc.lock();   // Lock it
            }
        }
        
        // Check whether we are migrating from Archi 4 to 5
        if(Archi4Migrator.check()) {
            return EXIT_RESTART;
        }
        
        /*
         * Mac item heights.
         * Read the preference setting and set it as a System Property before the workbench Display is created.
         * We can't use Archi's preferences here as it would trigger creation of the workbench Display 
         * because one of Archi's preference initialisers gets the Device zoom level which creates the default Display
         * and then it's too late to set the property. So we store it in org.eclipse.ui.workbench.prefs.
         */
        if(PlatformUtils.isMac()) {
            boolean useNativeItemHeights = InstanceScope.INSTANCE.getNode("org.eclipse.ui.workbench")
                                            .getBoolean(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, false);
            System.setProperty(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, Boolean.toString(useNativeItemHeights));
        }
        
        // Create Main Display
        Display display = PlatformUI.createDisplay();

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
     * We want to do this for an RCP standalone app but not when this is running
     * as a plugin becauae the location of the Workbench instance will be set
     * already. This has to be done before the Workbench starts. Note that the
     * launch configuration in the Eclipse IDE should have "@noDefault" as the
     * workspace for the launch configuration.
     * 
     * @param url A url to a folder
     */
    @SuppressWarnings("unused")
    private void setWorkbenchDataLocation(URL url) throws IllegalStateException, IOException {
        /*
         * Ascertain the existing location. If it is null, we can set it. When
         * run from the Eclipse IDE, setting the workspace location in the
         * launch configuration to "@noDefault" will set it as null.
         */
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logError("Instance Location is null, cannot set it in setWorkbenchDataLocation(URL)",
                    null);
        }
        else if(!instanceLoc.isSet()) {
            instanceLoc.release();
            /*
             * If this is set to true, you can't run another instance of the app
             * with this workspace open
             */
            instanceLoc.set(url, false);
        }
    }
}
