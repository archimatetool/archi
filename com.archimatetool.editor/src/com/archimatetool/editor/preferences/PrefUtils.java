/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Utils to access internal Eclipse Preferences before the Workbench starts.
 * We can set and store preferences before the Workbench starts.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class PrefUtils {
    
    /**
     * Initialise this now before the workbench starts
     */
    public static void init() {
        IEclipsePreferences defaultWorkBenchUIPrefs = getDefaultUIWorkBenchPrefs();
        if(defaultWorkBenchUIPrefs != null) {
            // We use Eclipse's org.eclipse.ui.internal.handlers.FullScreenHandler which shows
            // a popup with a message to tell you this and a "do not show again" checkbox.
            // This is hard to see and unnecessary, so set the default preference to true now so it doesn't show.
            defaultWorkBenchUIPrefs.putBoolean("org.eclipse.ui.window.fullscreenmode.donotshowinfoagain", true);
        }
        
        // Mac item heights.
        // Read the preference setting and set it as a System Property before the workbench Display is created.
        // We access it via the InstanceScope as that doesn't trigger Archi's PreferenceInitializer.
        // Archi's PreferenceInitializer triggers the creation of the workbench Display by getting the Device zoom level which in turn creates the default Display
        // and then it's too late to set the property.
        if(PlatformUtils.isMac()) {
            boolean useNativeItemHeights = InstanceScope.INSTANCE.getNode("com.archimatetool.editor")
                                            .getBoolean(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, false);
            System.setProperty(IPreferenceConstants.MAC_ITEM_HEIGHT_PROPERTY_KEY, Boolean.toString(useNativeItemHeights));
        }
        
        // Set Eclipse theming default enabled to true to counteract possible future regressions
        // See https://github.com/eclipse-platform/eclipse.platform.ui/issues/629
        // See https://github.com/eclipse-platform/eclipse.platform.ui/pull/630
        IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode("org.eclipse.e4.ui.workbench.renderers.swt");
        preferences.putBoolean("themeEnabled", true);
    }
    
    public static IEclipsePreferences getDefaultUIWorkBenchPrefs() {
        return DefaultScope.INSTANCE.getNode("org.eclipse.ui.workbench");
    }
    
    public static IEclipsePreferences getInstanceUIWorkBenchPrefs() {
        return InstanceScope.INSTANCE.getNode("org.eclipse.ui.workbench");
    }
    
    public static void savePrefs() {
        IEclipsePreferences workBenchUIPrefs = getInstanceUIWorkBenchPrefs();
        if(workBenchUIPrefs != null) {
            try {
                workBenchUIPrefs.flush();
            }
            catch(BackingStoreException ex) {
                ex.printStackTrace();
            }
        }
    }
}
