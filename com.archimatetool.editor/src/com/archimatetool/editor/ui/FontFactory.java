/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;




/**
 * Font Factory
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class FontFactory {
    
    private static final String DEFAULT_VIEW_FONT_NAME = "defaultViewFont";
    
    /**
     * Font Registry
     */
    private static FontRegistry fontRegistry = new FontRegistry();

    /**
     * @deprecated since 5.8.0 use JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT)
     */
    public static Font SystemFontBold = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
    
    /**
     * @deprecated since 5.8.0 use JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT)
     */
    public static Font SystemFontItalic = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
    
    /**
     * Cache of scaled font data strings
     */
    private static Map<String, String> scaledFonts = new HashMap<>();
    
    /**
     * DPI scale factor for Mac
     */
    private static final int MAC_DPI = 72;
    
    /**
     * Current DPI for Windows
     */
    private static final int WINDOWS_DPI = Display.getCurrent().getDPI().y;
    
    /**
     * Nominal DPI on Windows and Linux
     */
    private static final int DPI_96 = 96;
    
    /**
     * Scale fonts on Windows if current DPI is not 96
     */
    private static final boolean SCALE_FONTS_WINDOWS = PlatformUtils.isWindows() && WINDOWS_DPI != DPI_96;
    
    /**
     * @deprecated since 5.8.0 use {@link #get(String, Font)
     */
    public static Font get(String fontDataString) {
        return get(fontDataString, JFaceResources.getFontRegistry().defaultFont());
    }
    
    /**
     * Return a Font derived from the FontData string
     * @param fontDataString The FontData string
     * @param defaultFont The default font to fall back to if fontDataString is null or malformed or an exception occurs
     * Return the new Font or defaultFont if fontDataString is null or malformed or an exception occurs
     * @since 5.8.0
     */
    public static Font get(String fontDataString, Font defaultFont) {
        if(fontDataString == null || fontDataString.isBlank()) {
            return defaultFont;
        }
        
        // Not in the font registry
        if(!fontRegistry.hasValueFor(fontDataString)) {
            try {
                FontData fd = new FontData(fontDataString);
                fontRegistry.put(fontDataString, new FontData[] { fd });
            }
            // Exception if using font name from different OS platform or malformed or empty string
            catch(Exception ex) {
                return defaultFont;
            }
        }
        
        return fontRegistry.get(fontDataString);
    }

    /**
     * Return a font to use for figures and connections in a View (diagram) based on fontDataString.
     * If on Mac the font will be scaled up if the preference for font scaling is set.
     * If fontDataString is null, empty or malformed the font from {@link #getDefaultUserViewFont()} is returned.
     * @param fontDataString the FontData string
     * @return The font for the fontName. The font height will be scaled up on Mac if the preference for font scaling is set.
     * @since 5.8.0
     */
    public static Font getViewFont(String fontDataString) {
        // Scale Font height on Windows if not DPI 96 and on Mac if scaling preference is set
        if(SCALE_FONTS_WINDOWS || (PlatformUtils.isMac() && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.FONT_SCALING))) {
            // A null fontDataString signifies "default" so get the default user font data string so we can scale that up
            if(fontDataString == null || fontDataString.isBlank()) {
                fontDataString = getDefaultUserViewFontData().toString();
            }
            // Return scaled font data string
            fontDataString = getScaledFontDataString(fontDataString, PlatformUtils.isWindows() ? WINDOWS_DPI : MAC_DPI);
        }
        
        return get(fontDataString, getDefaultUserViewFont());
    }

    /**
     * @return The default Font to use in diagrams (Views)
     */
    public static Font getDefaultUserViewFont() {
        registerDefaultUserViewFontData(); // Ensure it's registered in registry
        return fontRegistry.get(DEFAULT_VIEW_FONT_NAME); 
    }
    
    /**
     * @return The default FontData to use in diagrams (Views)
     */
    public static FontData getDefaultUserViewFontData() {
        registerDefaultUserViewFontData(); // Ensure it's registered in registry
        return fontRegistry.getFontData(DEFAULT_VIEW_FONT_NAME)[0]; 
    }
    
    /**
     * Set the default FontData to use in diagrams (Views)
     * @param fd The FontData to set
     */
    public static void setDefaultUserViewFontData(FontData fd) {
        // Register it first before updating preferences
        fontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });

        // Then set the new value in preferences as this will send a property change to listeners
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.DEFAULT_VIEW_FONT, fd.toString());
    }
    
    /**
     * @return The default font to use in diagrams (Views) for each OS
     */
    public static FontData getDefaultViewOSFontData() {
        // Windows
        if(PlatformUtils.isWindows()) {
            return new FontData("Segoe UI", 9, SWT.NORMAL);
        }
        // Mac
        else if(PlatformUtils.isMac()) {
            return getDefaultViewOSFontData(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.FONT_SCALING));
        }
        // Linux
        else if(PlatformUtils.isLinux()) {
            return new FontData("Sans", 9, SWT.NORMAL);
        }

        // Default
        return new FontData("Sans", 9, SWT.NORMAL);
    }
    
    /**
     * @param useScaling If true return the scaled height value of FontData
     * @return The default font to use in diagrams (Views) for each OS
     */
    public static FontData getDefaultViewOSFontData(boolean useScaling) {
        // If useScaling is true use 9 points height, else 12
        if(PlatformUtils.isMac()) {
            return new FontData("Lucida Grande", useScaling ? 9 : 12, SWT.NORMAL);
        }
        
        return getDefaultViewOSFontData();
    }
    
    /**
     * @param font The font
     * @return The italic variant of the given font
     */
    public static Font getItalic(Font font) {
        FontData[] fd = font.getFontData();
        String key = fd[0].toString();
        fontRegistry.put(key, fd); // Ensure the base font is registered first
        return fontRegistry.getItalic(key);
    }
    
    /**
     * @param font The font
     * @return The bold variant of the given font
     */
    public static Font getBold(Font font) {
        FontData[] fd = font.getFontData();
        String key = fd[0].toString();
        fontRegistry.put(key, fd); // Ensure the base font is registered first
        return fontRegistry.getBold(key);
    }
    
    /**
     * Register the default User View FontData in the registry.
     * This will be either the user preference or the OS font data.
     * This is the font used for diagram objects and connections
     */
    private static void registerDefaultUserViewFontData() {
        if(fontRegistry.hasValueFor(DEFAULT_VIEW_FONT_NAME)) {
            return;
        }
        
        FontData fd = null;

        // Check User Prefs...
        String fontString = ArchiPlugin.getInstance().getPreferenceStore().getString(IPreferenceConstants.DEFAULT_VIEW_FONT);
        
        // Set in User Prefs
        if(StringUtils.isSet(fontString)) {
            try {
                fd = new FontData(fontString);
            }
            catch(Exception ex) {
            }
        }
        
        // Not set in preferences or an exception occcurred so register the default view OS FontData
        if(fd == null) {
            fd = getDefaultViewOSFontData();
        }
        
        fontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });
    }
    
    /**
     * @return a FontData string scaled from DPI.
     */
    private static String getScaledFontDataString(String fontDataString, int dpi) {
        if(fontDataString == null || dpi == DPI_96) { // No point in scaling 96 / 96
            return fontDataString;
        }
        
        return scaledFonts.computeIfAbsent(fontDataString, key -> {
            try {
                FontData fd = new FontData(fontDataString);    // Create FontData
                int newHeight = (fd.getHeight() * DPI_96) / dpi;   // New height is FontData height * 96 / DPI
                fd.setHeight(newHeight);
                return fd.toString();
            }
            catch(Exception ex) { // Can happen if string is malformed
                return fontDataString;
            }
        });
    }
}
