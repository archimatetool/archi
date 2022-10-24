/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.io.File;
import java.io.IOException;

import org.eclipse.equinox.p2.core.IAgentLocation;

/**
 * Utility P2 class
 * 
 * @author Phillip Beauvoir
 */
public class P2 {

    // Whether to use the dropins system
    public static final boolean USE_DROPINS = true;
    
    /**
     * Get the location of the "p2" folder as set in "eclipse.p2.data.area" in Archi.ini, or if not set there, in config.ini
     * Found a clue in https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/tree/plugins/org.eclipse.oomph.p2.core/src/org/eclipse/oomph/p2/internal/core/ProvisioningAgentProvider.java
     */
    public static File getP2Location() {
        // We can either get the IAgentLocation as a public static field
        @SuppressWarnings("restriction")
        IAgentLocation agentDataLocation = org.eclipse.equinox.internal.p2.core.Activator.agentDataLocation;
        if(agentDataLocation != null) {
            // Normalise the uri in case it has ".." in the path. If it does File#equals(File) doesn't work
            try {
                return new File(agentDataLocation.getRootLocation()).getCanonicalFile();
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
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
}
