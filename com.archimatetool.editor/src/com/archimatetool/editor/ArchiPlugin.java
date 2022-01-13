/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

import com.archimatetool.editor.preferences.IPreferenceConstants;



/**
 * The activator class controls the plug-in life cycle
 */
public class ArchiPlugin extends AbstractUIPlugin {

    /**
     * ID of the plug-in
     */
    public static final String PLUGIN_ID = "com.archimatetool.editor"; //$NON-NLS-1$

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

    /**
     * The constructor.
     */
    public ArchiPlugin() {
        INSTANCE = this;
        PREFERENCES = getPreferenceStore();
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
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
     * @return The Platform specific launcher, if any, to launch application from file in OS
     */
    public IPlatformLauncher getPlatformLauncher() {
        Bundle bundle = getBundle();
        try {
            Class<?> clazz = bundle.loadClass("com.archimatetool.editor.PlatformLauncher"); //$NON-NLS-1$
            if(IPlatformLauncher.class.isAssignableFrom(clazz)) {
                return ((IPlatformLauncher) clazz.getDeclaredConstructor().newInstance());
            }
        } catch(Exception e) {
        }
        
        return null;
    }
    
    /**
     * @return The User data folder
     */ 
    public File getUserDataFolder() {
        String path = PREFERENCES.getString(IPreferenceConstants.USER_DATA_FOLDER);
        return new File(path);
    }
    
    /**
     * @return The Workspace folder
     */
    public File getWorkspaceFolder() {
        /*
         * Get Data Folder.  Try for one set by a user system property first, otherwise
         * use the workbench instance data location
         */
        String strFolder = System.getProperty("com.archimatetool.editor.workspaceFolder"); //$NON-NLS-1$
        if(strFolder != null) {
            return new File(strFolder);
        }
        
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc == null) {
            Logger.logWarning("Instance Location is null. Using user.home"); //$NON-NLS-1$
            return new File(System.getProperty("user.home")); //$NON-NLS-1$
        }
        else {
            URL url = instanceLoc.getURL();
            if(url != null) {
                return new File(url.getPath());
            }
            else {
                return new File(System.getProperty("user.home")); //$NON-NLS-1$
            }
        }
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
        return v.getMajor() + "." + v.getMinor() + "." + v.getMicro(); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * @return The build quailifer version
     */
    public String getBuild() {
        return getBundle().getVersion().getQualifier();
    }
}
