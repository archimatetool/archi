package com.archimatetool.hammer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Activitor
 * 
 * @author Phillip Beauvoir
 */
public class ArchiHammerPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.archimatetool.hammer"; //$NON-NLS-1$
    
    /**
     * The shared instance
     */
    public static ArchiHammerPlugin INSTANCE;

    public ArchiHammerPlugin() {
        INSTANCE = this;
    }

    /**
     * @return An asset file relative to this Bundle
     */
    public File getAssetFile(File file) {
        URL url = FileLocator.find(Platform.getBundle(PLUGIN_ID), new Path(file.getPath()), null);
        
        try {
            url = FileLocator.resolve(url);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return new File(url.getPath()); 
    }
}
