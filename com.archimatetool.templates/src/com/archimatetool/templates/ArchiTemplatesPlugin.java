/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class ArchiTemplatesPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.archimatetool.templates"; //$NON-NLS-1$

    // The shared instance
    private static ArchiTemplatesPlugin instance;
    
    /**
     * @return the shared instance
     */
    public static ArchiTemplatesPlugin getInstance() {
        return instance;
    }

    public ArchiTemplatesPlugin() {
        instance = this;
    }

    /**
     * @return The templates folder
     */
    public File getTemplatesFolder() {
        URL url = FileLocator.find(getBundle(), new Path("$nl$/templates"), null); //$NON-NLS-1$
        try {
            url = FileLocator.resolve(url);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return new File(url.getPath()); 
    }
    
    /**
     * @return The File Location of this plugin
     */
    public File getPluginFolder() {
        try {
            URL url = FileLocator.resolve(getBundle().getEntry("/")); //$NON-NLS-1$
            return new File(url.getPath());
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
