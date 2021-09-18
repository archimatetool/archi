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
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.jasperreports.preferences.IJasperPreferenceConstants;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.archimatetool.jasperreports"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static JasperReportsPlugin INSTANCE;

    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    public JasperReportsPlugin() {
        INSTANCE = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        
        // Add default folder
        getDefaultUserTemplatesFolder();
    }
    
    /**
     * @return User-set JR user templates folder
     */
    public File getUserTemplatesFolder() {
        String s = getPreferenceStore().getString(IJasperPreferenceConstants.JASPER_USER_REPORTS_FOLDER);
        if(StringUtils.isSetAfterTrim(s)) {
            File f = new File(s);
            f.mkdirs();
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
        File folder = new File(ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.USER_DATA_FOLDER), "jasper-reports"); //$NON-NLS-1$
        folder.mkdirs();
        return folder;
    }

    /**
     * @return The Jasper Reports folder
     */
    public File getJasperReportsFolder() {
        URL url = FileLocator.find(getBundle(), new Path("$nl$/reports"), null); //$NON-NLS-1$
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
