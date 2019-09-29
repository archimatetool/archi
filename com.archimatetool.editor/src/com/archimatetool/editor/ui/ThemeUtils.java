/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.FrameworkUtil;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;



/**
 * Theme Utils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings({"restriction", "nls"})
public final class ThemeUtils {
    
    public static final String E4_DEFAULT_THEME_ID = "org.eclipse.e4.ui.css.theme.e4_default";
    public static final String E4_DARK_THEME_ID = "org.eclipse.e4.ui.css.theme.e4_dark";
    
    private static final String THEMEID_KEY = "themeid";
    
    private static IThemeEngine engine;

    public static IThemeEngine getThemeEngine() {
        if(engine == null) {
            MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);
            IEclipseContext context = application.getContext();
            engine = context.get(IThemeEngine.class);
        }

        // e3 method
        //Bundle bundle = FrameworkUtil.getBundle(ArchiPlugin.class);
        //BundleContext context = bundle.getBundleContext();
        //ServiceReference<IThemeManager> ref = context.getServiceReference(IThemeManager.class);
        //IThemeManager themeManager = context.getService(ref);
        //IThemeEngine engine = themeManager.getEngineForDisplay(Display.getCurrent());
        
        return engine;
    }
    
    /**
     *  Set light/dark theme as per OS
     *  We have to over-ride Eclipse setting dark/light theme automatically
     */
    public static void setDefaultTheme() {
        // If auto theme preference set
        if(Preferences.STORE.getBoolean(IPreferenceConstants.THEME_AUTO)) {
            // Dark
            if(Display.isSystemDarkTheme()) {
                getThemePreferences().put(THEMEID_KEY, E4_DARK_THEME_ID);
            }
            // Light
            else {
                getThemePreferences().put(THEMEID_KEY, E4_DEFAULT_THEME_ID);
            }
        }
        // No theme set, so we set a default one to stop Eclipse loading the dark theme by default
        // If the OS has dark theme on Mac/Linux
        else if(getThemePreferences().get(THEMEID_KEY, null) == null) {
            getThemePreferences().put(THEMEID_KEY, E4_DEFAULT_THEME_ID);
        }
    }
    
    private static IEclipsePreferences getThemePreferences() {
        return InstanceScope.INSTANCE.getNode(FrameworkUtil.getBundle(ThemeEngine.class).getSymbolicName());
    }
    
    public static String getDefaultThemeName() {
        MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);
        IEclipseContext context = application.getContext();
        return (String)context.get("cssTheme");
    }
    
    /**
     * @return true if we are using a dark theme
     */
    public static boolean isDarkTheme() {
        return getThemeEngine().getActiveTheme() != null && getThemeEngine().getActiveTheme().getId().contains("dark");
    }
}
