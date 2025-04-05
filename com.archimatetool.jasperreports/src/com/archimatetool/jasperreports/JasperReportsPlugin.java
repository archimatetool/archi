/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.jasperreports.preferences.IJasperPreferenceConstants;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class JasperReportsPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.archimatetool.jasperreports";

    // The shared instance
    private static JasperReportsPlugin instance;

    /**
     * @return the shared instance
     */
    public static JasperReportsPlugin getInstance() {
        return instance;
    }

    public JasperReportsPlugin() {
        instance = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        
        // Create user templates folder
        getUserTemplatesFolder().mkdirs();
    }
    
    /**
     * @return User-set JR user templates folder
     */
    public File getUserTemplatesFolder() {
        String s = getPreferenceStore().getString(IJasperPreferenceConstants.JASPER_USER_REPORTS_FOLDER);
        if(StringUtils.isSetAfterTrim(s)) {
            File f = new File(s);
            if(f.exists() && f.isDirectory()) {
                return f;
            }
        }
        
        return getDefaultUserTemplatesFolder();
    }

    /**
     * @return Default JR user templates folder
     */
    public File getDefaultUserTemplatesFolder() {
        return new File(ArchiPlugin.getInstance().getUserDataFolder(), "jasper-reports");
    }

    /**
     * @return The Jasper Reports folder
     */
    public File getJasperReportsFolder() {
        URL url = FileLocator.find(getBundle(), new Path("$nl$/reports"), null);
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
            URL url = FileLocator.resolve(getBundle().getEntry("/"));
            return new File(url.getPath());
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
