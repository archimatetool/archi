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

    /**
     * The shared instance
     */
    public static ArchiTemplatesPlugin INSTANCE;
    
    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    public ArchiTemplatesPlugin() {
        INSTANCE = this;
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
        if(fPluginFolder == null) {
            URL url = getBundle().getEntry("/"); //$NON-NLS-1$
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            fPluginFolder = new File(url.getPath());
        }
        
        return fPluginFolder;
    }

}
