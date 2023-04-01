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
     * Setting in Archi.ini for user dropins folder
     * -Dorg.eclipse.equinox.p2.reconciler.dropins.directory=%user.home%/subfolder/dropins
     */
    public static final String DROPINS_DIRECTORY = "org.eclipse.equinox.p2.reconciler.dropins.directory";

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
        File file = getUserPropertyLocation("data.location");
        return file != null ? file : new File(System.getProperty("user.home") + "/Documents/Archi");
    }
    
    /**
     * @return The User dropins folder use for Archi plug-ins or null if not set
     */ 
    public File getUserDropinsFolder() {
        // If the dropins dir is set in Archi.ini or the framework
        String dropinsDirProperty = getBundle().getBundleContext().getProperty(DROPINS_DIRECTORY);
        
        if(dropinsDirProperty != null && dropinsDirProperty.length() != 0) {
            // Perform a variable substitution if necessary of %% tokens
            dropinsDirProperty = substituteVariablesForDropinsPath(dropinsDirProperty);
            return new File(dropinsDirProperty);
        }

        // Ensure to return null if this is not set because the dropins folder
        // might be located as the "plugins" folder or an Eclipse default
        return null;
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
    
    /**
     * @return A user set System Property denoting a location or null if not set.
     *         This can be set in Archi.ini like this - -DlocationOfSomething=@user.home/path/to/dir
     */
    public File getUserPropertyLocation(String argument) {
        String path = System.getProperty(argument);
        
        if(path == null || path.length() == 0) {
            return null;
        }
        
        path = path.replace("@user.home", System.getProperty("user.home"));
        
        // Check is valid file path
        try {
            Path p = Paths.get(path);
            return p.toFile();
        }
        catch(InvalidPathException ex) {
        }
        
        return null;
    }
    
    /**
     * This is taken From org.eclipse.equinox.internal.p2.reconciler.dropins.Activator
     * When the dropins folder contains %% tokens, treat this as a system property.
     * Example - %user.home%
     */
    private String substituteVariablesForDropinsPath(String path) {
        if(path == null) {
            return path;
        }
        
        int beginIndex = path.indexOf('%');
        
        // no variable
        if(beginIndex == -1) {
            return path;
        }
        
        beginIndex++;
        
        int endIndex = path.indexOf('%', beginIndex);
        // no matching end % to indicate variable
        if(endIndex == -1) {
            return path;
        }
        
        // get the variable name and do a lookup
        String var = path.substring(beginIndex, endIndex);
        if(var.length() == 0 || var.indexOf(File.pathSeparatorChar) != -1) {
            return path;
        }
        
        var = getBundle().getBundleContext().getProperty(var);
        if(var == null) {
            return path;
        }
        
        return path.substring(0, beginIndex - 1) + var + path.substring(endIndex + 1);
    }

}
