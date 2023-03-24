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
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.IThemeRegistry;
import org.eclipse.ui.internal.themes.ThemeElementHelper;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.themes.IThemeManager;
import org.osgi.framework.FrameworkUtil;

import com.archimatetool.editor.utils.StringUtils;



/**
 * Theme Utils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings({"restriction", "nls"})
public final class ThemeUtils {
    
    public static final String E4_DEFAULT_THEME_ID = "org.eclipse.e4.ui.css.theme.e4_default";
    public static final String E4_DARK_THEME_ID = "org.eclipse.e4.ui.css.theme.e4_dark";
    
    /**
     * SWT CSS Tabs are square by default in Eclipse 4.16 and greater
     * Stored in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.workbench.renderers.swt.prefs
     */
    public static final String USE_ROUND_TABS = "USE_ROUND_TABS";
    
    /**
     * Whether the theme engine is in use or not
     * Stored in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.workbench.renderers.swt.prefs
     */
    public static final String THEME_ENABLED = "themeEnabled";
    
    private static IThemeEngine engine;
    
    /**
     * @return The Theme engine. This will be null if THEME_ENABLED is false or the workbench is not running
     */
    public static IThemeEngine getThemeEngine() {
        if(engine == null && PlatformUI.isWorkbenchRunning()) {
            engine = PlatformUI.getWorkbench().getService(IThemeEngine.class);
        }

        return engine;
    }
    
    /**
     * @return true if the CSS Theme engine is running. False if theming is disabled.
     */
    public static boolean isCSSThemingEnabled() {
        return getThemeEngine() != null;
    }
    
    /**
     * Reset current theme
     */
    public static void resetCurrentTheme() {
        if(getThemeEngine() instanceof ThemeEngine) {
            ((ThemeEngine)getThemeEngine()).resetCurrentTheme();
        }
    }
    
    /**
     * @return The theme registry
     */
    public static IThemeRegistry getThemeRegistry() {
        return WorkbenchPlugin.getDefault().getThemeRegistry();
    }
    
    /**
     * @return The workbench ThemeManager
     */
    public static IThemeManager getThemeManager() {
        return PlatformUI.isWorkbenchRunning() ? PlatformUI.getWorkbench().getThemeManager() : null;
    }
    
    /**
     * Set theme color definition value for current theme.
     * Some of this code inspired by {@link org.eclipse.ui.internal.themes.ColorsAndFontsPreferencePage}
     */
    public static void setCurrentThemeColor(String colorDefinitionId, RGB newValue) {
        if(!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        // Same color?
        if(newValue.equals(getCurrentThemeColor(colorDefinitionId))) {
            return;
        }
        
        // Get the color definition from the registry
        ColorDefinition colorDef = getThemeRegistry().findColor(colorDefinitionId);
        if(colorDef == null) {
            return;
        }

        // Write color rgb to workbench preference file at .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.workbench.prefs
        String preferenceKey = createPreferenceKey(colorDef);
        if(preferenceKey != null) {
            PrefUtil.getInternalPreferenceStore().setValue(preferenceKey, StringConverter.asString(newValue));
            PrefUtil.savePrefs();
        }
        
        // Set theme color in theme manager
        getThemeManager().getCurrentTheme().getColorRegistry().put(colorDefinitionId, newValue);
    }
    
    /**
     * Get the current theme color for a color definition
     */
    public static RGB getCurrentThemeColor(String colorDefinitionId) {
        return getThemeManager() != null ? getThemeManager().getCurrentTheme().getColorRegistry().getRGB(colorDefinitionId) : null;
    }
    
    /**
     * Get the default RGB color value for a color definition.
     */
    public static RGB getDefaultThemeColor(String colorDefinitionId) {
        ColorDefinition colorDef = getThemeRegistry().findColor(colorDefinitionId);
        if(colorDef == null) {
            return null;
        }
        
        // Check if there is a default set in preferences or plugin_customization.ini
        String preferenceKey = createPreferenceKey(colorDef);
        if(preferenceKey != null) {
            String setting = PrefUtil.getInternalPreferenceStore().getDefaultString(preferenceKey);
            if(StringUtils.isSet(setting)) {
                return StringConverter.asRGB(setting, colorDef.getValue());
            }
        }
        
        // Return ColorDefinition value
        return colorDef.getValue();
    }
    
    /**
     * Get the label name of a color definition
     */
    public static String getColorDefinitionName(String colorDefinitionId) {
        ColorDefinition colorDef = getThemeRegistry().findColor(colorDefinitionId);
        return colorDef != null ? colorDef.getName() : null;
    }
    
    /**
     * Create a preference key to write a ColorDefinition in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.workbench.prefs
     * This code is based on that in {@link org.eclipse.ui.internal.themes.ColorsAndFontsPreferencePage}
     */
    private static String createPreferenceKey(ColorDefinition colorDef) {
        if(getThemeEngine() != null && (colorDef.isOverridden() || colorDef.isAddedByCss())) {
            return ThemeElementHelper.createPreferenceKey(getThemeEngine().getActiveTheme(), getThemeManager().getCurrentTheme(), colorDef.getId());
        }
        
        return ThemeElementHelper.createPreferenceKey(getThemeManager().getCurrentTheme(), colorDef.getId());
    }
    
    /**
     * @return Theme Preferences
     */
    public static IEclipsePreferences getThemePreferences() {
        // This is at .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.css.swt.theme.prefs
        return InstanceScope.INSTANCE.getNode(FrameworkUtil.getBundle(ThemeEngine.class).getSymbolicName());
    }
    
    /**
     * @return SWT Renderer Preferences
     */
    public static IEclipsePreferences getSwtRendererPreferences() {
        // This is at .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.workbench.renderers.swt.prefs
        return InstanceScope.INSTANCE.getNode("org.eclipse.e4.ui.workbench.renderers.swt");
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
        return getThemeEngine() != null && 
                getThemeEngine().getActiveTheme() != null && getThemeEngine().getActiveTheme().getId().contains("dark");
    }
}
