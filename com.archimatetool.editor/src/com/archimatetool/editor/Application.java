/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.preferences.PrefUtils;
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
        
        // Initialise internal preferences
        PrefUtils.init();
        
        // Create Display
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
}
