/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;



/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("nls")
public class ArchiPlugin extends AbstractUIPlugin {

    /**
     * ID of the plug-in
     */
    public static final String PLUGIN_ID = "com.archimatetool.editor";

    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    /**
     * The shared instance
     */
    public static ArchiPlugin INSTANCE;
    
    /**
     * The shared Preference store
     */
    public static IPreferenceStore PREFERENCES;

    public ArchiPlugin() {
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        
        INSTANCE = this;
        PREFERENCES = getPreferenceStore();
    }

    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        // super must be *last*
        super.stop(context);
    }
    
    /**
     * @return The User data folder use for Archi data
     */ 
    public File getUserDataFolder() {
        String path = System.getProperty("data.location");
        
        if(path != null) {
            path = path.replace("@user.home", System.getProperty("user.home"));
            
            // Check is valid
            try {
                Path p = Paths.get(path);
                return p.toFile();
            }
            catch(InvalidPathException ex) {
            }
        }
        
        // Default
        return new File(System.getProperty("user.home") + "/Documents/Archi");
    }
    
    /**
     * @return The Workspace folder
     */
    public File getWorkspaceFolder() {
        /*
         * Get Data Folder.  Try for one set by a user system property first, otherwise
         * use the workbench instance data location
         */
        String strFolder = System.getProperty("com.archimatetool.editor.workspaceFolder");
        if(strFolder != null) {
            return new File(strFolder);
        }
        
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logWarning("Instance Location is null. Using user.home");
            return new File(System.getProperty("user.home"));
        }
        else {
            URL url = instanceLoc.getURL();
            if(url != null) {
                return new File(url.getPath());
            }
            else {
                return new File(System.getProperty("user.home"));
            }
        }
    }
    
    /**
     * @return The File Location of this plugin
     */
    public File getPluginFolder() {
        if(fPluginFolder == null) {
            URL url = getBundle().getEntry("/");
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
    
    /**
     * @param key A string key beginning with "%"
     * @return A Resource String from the plugin.properties file
     */
    public String getResourceString(String key) {
        return Platform.getResourceString(Platform.getBundle(PLUGIN_ID), key);
    }
    
    /**
     * @return The plugin id
     */
    public String getId(){
        return getBundle().getSymbolicName();
    }
    
    /**
     * @return The version of this app in format 1.0.0
     */
    public String getVersion() {
        Version v = getBundle().getVersion();
        return v.getMajor() + "." + v.getMinor() + "." + v.getMicro();
    }
    
    /**
     * @return The build quailifer version
     */
    public String getBuild() {
        return getBundle().getVersion().getQualifier();
    }
}
