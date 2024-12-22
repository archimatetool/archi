/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.Objects;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.FontDefinition;
import org.eclipse.ui.internal.themes.IThemeRegistry;
import org.eclipse.ui.internal.themes.ThemeElementDefinition;
import org.eclipse.ui.internal.themes.ThemeElementHelper;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.themes.IThemeManager;

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
    public static final String HIGH_CONTRAST_THEME_ID = "org.eclipse.e4.ui.css.theme.high-contrast";
    
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
    
    /**
     * Used in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.css.swt.theme.prefs to store current theme id
     */
    public static final String THEMEID_KEY = "themeid";
    
    private static IThemeEngine engine;
    
    /**
     * @return The Theme engine.
     * This will be null if themeEnabled=false in org.eclipse.e4.ui.workbench.renderers.swt.prefs
     * or the program argument "-cssTheme none" is set, or the workbench is not running.
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
    public static boolean isCssThemingEnabled() {
        return getThemeEngine() != null;
    }
    
    /**
     * Reset current theme
     */
    public static void resetCurrentTheme() {
        if(getThemeEngine() instanceof ThemeEngine themeEngine) {
            themeEngine.resetCurrentTheme();
        }
    }
    
    /**
     * @return The theme registry
     */
    public static IThemeRegistry getThemeRegistry() {
        return WorkbenchPlugin.getDefault().getThemeRegistry();
    }
    
    /**
     * @return The workbench ThemeManager or null if the workbench is not running
     */
    public static IThemeManager getThemeManager() {
        return PlatformUI.isWorkbenchRunning() ? PlatformUI.getWorkbench().getThemeManager() : null;
    }
    
    /**
     * Register a control's CSS class name by setting the "org.eclipse.e4.ui.css.CssClassName" data property to a CSS class name
     * @param control The control to register
     * @param className The CSS class name
     */
    public static void registerCssClassName(Control control, String className) {
        control.setData("org.eclipse.e4.ui.css.CssClassName", className);
    }
    
    /**
     * Register a control's CSS unique ID by setting the "org.eclipse.e4.ui.css.id" data property to a CSS id name
     * @param control The control to register
     * @param id The CSS id
     */
    public static void registerCssId(Control control, String id) {
        control.setData("org.eclipse.e4.ui.css.id", id);
    }

    /**
     * If the CSS theme engine is available then force applying the style to the control and its children 
     * @param control The control 
     * @param applyStylesToChildNodes if the control's children should be updated as well
     */
    public static void applyStyles(Control control, boolean applyStylesToChildNodes) {
        if(getThemeEngine() != null) {
            getThemeEngine().applyStyles(control, applyStylesToChildNodes);
        }
    }

    /**
     * @return Theme Preferences
     */
    public static IEclipsePreferences getThemePreferences() {
        // This is at .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.css.swt.theme.prefs
        return InstanceScope.INSTANCE.getNode("org.eclipse.e4.ui.css.swt.theme");
    }
    
    /**
     * @return SWT Renderer Preferences
     */
    public static IEclipsePreferences getSwtRendererPreferences() {
        // This is at .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.e4.ui.workbench.renderers.swt.prefs
        return InstanceScope.INSTANCE.getNode("org.eclipse.e4.ui.workbench.renderers.swt");
    }
    
    public static String getDefaultThemeName() {
        // This is set in the main plugin.xml file as a property so get it from there
        IEclipseContext context =  PlatformUI.getWorkbench().getService(IEclipseContext.class);
        return context.get("cssTheme") instanceof String themeName ? themeName : E4_DEFAULT_THEME_ID;
    }
    
    /**
     * @return true if we are using a dark theme
     */
    public static boolean isDarkTheme() {
        return getThemeEngine() != null && 
                getThemeEngine().getActiveTheme() != null && getThemeEngine().getActiveTheme().getId().contains("dark");
    }
    
    // ===============================================
    // Color Definitions
    // ===============================================
    
    /**
     * Set (and save) the theme color definition value for the current theme in preferences.
     * This is stored in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.workbench.prefs
     * Some of this code inspired by {@link org.eclipse.ui.internal.themes.ColorsAndFontsPreferencePage}
     */
    public static void setCurrentThemeColor(String colorDefinitionId, RGB newValue) {
        if(!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        // Same color?
        if(Objects.equals(newValue, getCurrentThemeColor(colorDefinitionId))) {
            return;
        }
        
        // Get the color definition from the registry
        ColorDefinition colorDef = getThemeRegistry().findColor(colorDefinitionId);
        if(colorDef == null) {
            return;
        }

        String preferenceKey = createPreferenceKey(colorDef);
        if(Objects.equals(newValue, getDefaultThemeColor(colorDefinitionId))) { // If it's the default, remove it
            PrefUtil.getInternalPreferenceStore().setToDefault(preferenceKey);
        }
        else {
            PrefUtil.getInternalPreferenceStore().setValue(preferenceKey, StringConverter.asString(newValue));
        }
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
        String setting = PrefUtil.getInternalPreferenceStore().getDefaultString(preferenceKey);
        if(StringUtils.isSet(setting)) {
            return StringConverter.asRGB(setting, colorDef.getValue());
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
     * If theming is disabled set the background color on the control from the colorDefinitionId
     */
    public static void setBackgroundColorIfCssThemingDisabled(Control control, String colorDefinitionId) {
        setControlPropertyIfCssThemingDisabled(control, colorDefinitionId, () -> {
            RGB rgb = getCurrentThemeColor(colorDefinitionId);
            if(rgb != null) {
                control.setBackground(new Color(rgb));
            }
        });
    }

    // ===============================================
    // Font Definitions
    // ===============================================
    
    /**
     * Set (and save) the font definition value for the current theme in preferences.
     * This is stored in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.workbench.prefs
     * Some of this code inspired by {@link org.eclipse.ui.internal.themes.ColorsAndFontsPreferencePage}
     */
    public static void setCurrentThemeFont(String fontDefinitionId, FontData fontData, FontData defaultFontData) {
        if(!PlatformUI.isWorkbenchRunning() || fontData == null) {
            return;
        }
        
        // Get the font definition from the registry
        FontDefinition fontDef = getThemeRegistry().findFont(fontDefinitionId);
        if(fontDef == null) {
            return;
        }
        
        String preferenceKey = createPreferenceKey(fontDef);
        
        // Also write a ".default" preference key so that if the user exports Archi's preferences
        // They can get both keys to add to their custom preferences file
        
        if(Objects.equals(fontData, defaultFontData)) { // If it's the default, remove it
            PrefUtil.getInternalPreferenceStore().setToDefault(preferenceKey);
            PrefUtil.getInternalPreferenceStore().setToDefault("default." + preferenceKey);
        }
        else {
            PrefUtil.getInternalPreferenceStore().setValue(preferenceKey, fontData.toString());
            PrefUtil.getInternalPreferenceStore().setValue("default." + preferenceKey, fontData.toString());
        }
    }
    
    /**
     * Get the FontData in the current theme for a font definition or null
     */
    public static FontData getCurrentThemeFontData(String fontDefinitionId) {
        FontData[] fd = getCurrentThemeFontRegistry() != null ? getCurrentThemeFontRegistry().getFontData(fontDefinitionId) : null;
        return fd != null ? fd[0] : null;
    }
    
    /**
     * Get the Font in the current theme for a font definition or null
     */
    public static Font getCurrentThemeFont(String fontDefinitionId) {
        return getCurrentThemeFontRegistry() != null ? getCurrentThemeFontRegistry().get(fontDefinitionId) : null;
    }

    /**
     * Get the current theme font data for a font definition or null
     */
    private static FontRegistry getCurrentThemeFontRegistry() {
        return getThemeManager() != null ? getThemeManager().getCurrentTheme().getFontRegistry() : null;
    }
    
    /**
     * Get the default FontData value for a font definition or null
     */
    public static FontData getDefaultThemeFontData(String fontDefinitionId) {
        FontDefinition fontDef = getThemeRegistry().findFont(fontDefinitionId);
        if(fontDef == null) {
            return null;
        }
        
        String preferenceKey = createPreferenceKey(fontDef);

        // Check if there is a default setting in custom preferences or plugin_customization.ini
        // We use our own key for this since Eclipse will have set the default value to that set in the theme's plugin.xml file
        String value = PrefUtil.getInternalPreferenceStore().getDefaultString("default." + preferenceKey);
        if(StringUtils.isSet(value)) {
            try {
                return new FontData(value);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Else check the default value set in the theme plugin.xml file, if any.
        // If not set this will return the system font
        return PreferenceConverter.getDefaultFontData(PrefUtil.getInternalPreferenceStore(), preferenceKey);
    }

    /**
     * If theming is disabled set the font on the control from the fontDefinitionId
     */
    public static void setFontIfCssThemingDisabled(Control control, String fontDefinitionId) {
        setControlPropertyIfCssThemingDisabled(control, fontDefinitionId, () -> {
            Font font = getCurrentThemeFont(fontDefinitionId);
            if(font != null) {
                control.setFont(font);
            }
        });
    }
    
    // ===============================================
    // Utils
    // ===============================================
    
    /**
     * Set a property on a control from the definitionId such as font or background color
     * and update it when the theme changes.
     */
    private static void setControlPropertyIfCssThemingDisabled(Control control, String definitionId, Runnable action) {
        // Only if CSS theming is disabled and we have a theme manager
        if(isCssThemingEnabled() || getThemeManager() == null) {
            return;
        }
        
        // Run the action
        action.run();
        
        // Listen to theme changes and run the action
        IPropertyChangeListener themeListener = event -> {
            if(event.getProperty() == definitionId) {
                action.run();
            }
        };

        getThemeManager().addPropertyChangeListener(themeListener);

        control.addDisposeListener(e -> {
            getThemeManager().removePropertyChangeListener(themeListener);
        });
    }
    
    /**
     * Create a preference key to write a ThemeElementDefinition in .metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.ui.workbench.prefs
     * This is adapted from {@link org.eclipse.ui.internal.themes.ColorsAndFontsPreferencePage#createPreferenceKey}
     */
    private static String createPreferenceKey(ThemeElementDefinition definition) {
        if(getThemeEngine() != null && (definition.isOverridden() || definition.isAddedByCss())) {
            return ThemeElementHelper.createPreferenceKey(getThemeEngine().getActiveTheme(), getThemeManager().getCurrentTheme(), definition.getId());
        }
        
        return ThemeElementHelper.createPreferenceKey(getThemeManager().getCurrentTheme(), definition.getId());
    }
}
