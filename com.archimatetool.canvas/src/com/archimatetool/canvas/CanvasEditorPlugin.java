/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.archimatetool.canvas.factory.CanvasBlockUIProvider;
import com.archimatetool.canvas.factory.CanvasImageUIProvider;
import com.archimatetool.canvas.factory.CanvasStickyUIProvider;
import com.archimatetool.editor.ui.factory.ElementUIFactory;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.archimatetool.canvas"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static CanvasEditorPlugin INSTANCE;
    
    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    public CanvasEditorPlugin() {
        INSTANCE = this;
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        
        // Register UI Providers
        ElementUIFactory.INSTANCE.registerProvider(new CanvasImageUIProvider());
        ElementUIFactory.INSTANCE.registerProvider(new CanvasBlockUIProvider());
        ElementUIFactory.INSTANCE.registerProvider(new CanvasStickyUIProvider());
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
