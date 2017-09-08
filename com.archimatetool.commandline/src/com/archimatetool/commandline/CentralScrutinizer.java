/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;




/**
 * The main application class for standalone operation.
 * 
 * @author Phillip Beauvoir
 */
public class CentralScrutinizer implements IApplication {

    /**
     * Constructor
     */
    public CentralScrutinizer() {
    }
    
    private List<ICommandLineProvider> providers = new ArrayList<ICommandLineProvider>();

    public Object start(IApplicationContext context) throws Exception {
        
        // Collect registered command line providers
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor("com.archimatetool.commandline.commandlineProvider")) { //$NON-NLS-1$
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                ICommandLineProvider provider = (ICommandLineProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                if(id != null && provider != null) {
                    providers.add(provider);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
        
        // Get Command line arguments and run each provider
        String[] args = Platform.getApplicationArgs();
        for(ICommandLineProvider provider : providers) {
            provider.run(args);
        }
        
        return EXIT_OK;
    }

    public void stop() {
    }

}
