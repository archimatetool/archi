/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Utils to access internal Eclipse Preferences before the Workbench starts.
 * We can set and store preferences before the Workbench starts.
 * 
 * 
 * DefaultScope settings don't persist to disk and so flush() is not needed.
 * InstanceScope and ConfigurationScope do persist to disk and flush() is needed if setting these prefs from elsewhere.
 * When we set prefs in init() ther'e no need to call flush() as these prefs are loaded in at startup each time.
 * A side effect of this is that these startup prefs will be persisted to disk if flush() is called elsewhere.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PrefUtils {
    
    // IDs of Eclipse plug-ins for their preference nodes
    public static final String ORG_ECLIPSE_UI = "org.eclipse.ui";
    public static final String ORG_ECLIPSE_UI_WORKBENCH = "org.eclipse.ui.workbench";
    public static final String ORG_ECLIPSE_E4_UI_CSS_SWT_THEME = "org.eclipse.e4.ui.css.swt.theme";
    public static final String ORG_ECLIPSE_E4_UI_WORKBENCH_RENDERERS_SWT = "org.eclipse.e4.ui.workbench.renderers.swt";

    /**
     * Initialise this now before the workbench starts
     */
    public static void init() {
        // We use Eclipse's org.eclipse.ui.internal.handlers.FullScreenHandler which shows
        // a popup with a message to tell you this and a "do not show again" checkbox.
        // This is hard to see and unnecessary, so set the default preference to true now so it doesn't show.
        getDefaultPrefs(ORG_ECLIPSE_UI_WORKBENCH).putBoolean("org.eclipse.ui.window.fullscreenmode.donotshowinfoagain", true);
        
        // Set Eclipse theming default enabled to true to counteract possible future regressions
        // See https://github.com/eclipse-platform/eclipse.platform.ui/issues/629
        // See https://github.com/eclipse-platform/eclipse.platform.ui/pull/630
        getInstancePrefs(ORG_ECLIPSE_E4_UI_WORKBENCH_RENDERERS_SWT).putBoolean("themeEnabled", true);
        
        // Windows specific
        if(PlatformUtils.isWindows()) {
            // On Windows 11, in later versions of SWT an ugly selection indicator is drawn on menu items that have images with style AS_RADIO_BUTTON or AS_CHECK_BOX
            // We don't want this, so set this property. @see org.eclipse.swt.widgets.MenuItem
            System.setProperty("org.eclipse.swt.internal.win32.menu.customSelectionImage", "0");
        }

        // Mac specific
        if(PlatformUtils.isMac()) {
            // Mac item heights.
            // Read the preference setting and set it as a System Property *before* the workbench Display is created.
            // We access it via the InstanceScope as that doesn't trigger Archi's PreferenceInitializer.
            // Archi's PreferenceInitializer triggers the creation of the workbench Display by getting the Device zoom level which in turn creates the default Display
            // and then it's too late to set the property.
            boolean useNativeItemHeights = getInstancePrefs(ArchiPlugin.PLUGIN_ID).getBoolean(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, false);
            System.setProperty(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, Boolean.toString(useNativeItemHeights));
        }
    }
    
    public static IEclipsePreferences getDefaultPrefs(String pluginId) {
        return DefaultScope.INSTANCE.getNode(pluginId);
    }
    
    public static IEclipsePreferences getInstancePrefs(String pluginId) {
        return InstanceScope.INSTANCE.getNode(pluginId);
    }
    
    public static IEclipsePreferences getConfigurationPrefs(String pluginId) {
        return ConfigurationScope.INSTANCE.getNode(pluginId);
    }
    
    public static void saveInstancePrefs(String pluginId) {
        savePrefs(getInstancePrefs(pluginId));
    }
    
    public static void saveConfigurationPrefs(String pluginId) {
        savePrefs(getConfigurationPrefs(pluginId));
    }

    public static void savePrefs(IEclipsePreferences prefs) {
        if(prefs != null) {
            try {
                prefs.flush();
            }
            catch(BackingStoreException ex) {
                Logger.error("Could not save prefs", ex);
            }
        }
    }
}
