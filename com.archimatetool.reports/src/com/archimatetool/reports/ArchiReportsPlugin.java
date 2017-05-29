/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.archimatetool.reports.html.HTMLReportExporter;
import com.archimatetool.reports.preferences.EArchiReportsTabs;
import com.archimatetool.reports.preferences.IArchiReportsPreferenceConstants;

/**
 * Activator
 * 
 * @author Phillip Beauvoir
 */
public class ArchiReportsPlugin extends AbstractUIPlugin {
    
    public static final String PLUGIN_ID = "com.archimatetool.reports"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static ArchiReportsPlugin INSTANCE;

    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    public ArchiReportsPlugin() {
        INSTANCE = this;
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        try {
            HTMLReportExporter.cleanPreviewFiles();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            super.stop(context);
        }
    }

    /**
     * @return The plugins folder
     */
    public File getTemplatesFolder() {
        return new File(getPluginFolder(), "templates"); //$NON-NLS-1$
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

    public Boolean showViewDocumentationTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.VIEWS_SHOW_DOCUMENTATION);
    }

    public Boolean showViewPropertiesTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.VIEWS_SHOW_PROPERTIES);
    }

    public Boolean showViewElementsTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.VIEWS_SHOW_ELEMENTS);
    }

    public Boolean showElementDocumentationTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.ELEMENTS_SHOW_DOCUMENTATION);
    }

    public Boolean showElementPropertiesTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.ELEMENTS_SHOW_PROPERTIES);
    }

    public Boolean showElementViewsTab() {
        return getPreferenceStore().getBoolean(IArchiReportsPreferenceConstants.ELEMENTS_SHOW_VIEWS);
    }

    public EArchiReportsTabs getDefaultElementsTab() {
        String elementsDefaultTab = getPreferenceStore().getString(IArchiReportsPreferenceConstants.ELEMENTS_DEFAULT_TAB);
        if (elementsDefaultTab == null || elementsDefaultTab.isEmpty()) {
            return null;
        } else {
            return EArchiReportsTabs.valueOf(elementsDefaultTab);
        }
    }

    public EArchiReportsTabs getDefaultViewsTab() {
        String viewsDefaultTab = getPreferenceStore().getString(IArchiReportsPreferenceConstants.VIEWS_DEFAULT_TAB);
        if (viewsDefaultTab == null || viewsDefaultTab.isEmpty()) {
            return null;
        } else {
            return EArchiReportsTabs.valueOf(viewsDefaultTab);
        }
    }
}
