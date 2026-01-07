/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

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
    private static FontRegistry FontRegistry = new FontRegistry();

    public static Font SystemFontBold = JFaceResources.getFontRegistry().getBold("");
    
    public static Font SystemFontItalic = JFaceResources.getFontRegistry().getItalic("");
    
    /**
     * @param fontName
     * @return A Font for the fontName or the default user font if null or exception occurs
     */
    public static Font get(String fontName) {
        if(fontName == null) {
            return getDefaultUserViewFont();
        }
        
        if(!FontRegistry.hasValueFor(fontName)) {
            try {
                FontData fd = new FontData(fontName);
                FontRegistry.put(fontName, new FontData[] { fd });
            }
            // Can cause exception if using font name from different OS platform
            catch(Exception ex) {
                return getDefaultUserViewFont();
            }
        }
        
        return FontRegistry.get(fontName);
    }

    public static Font getDefaultUserViewFont() {
        // We don't have it, so try to set it
        if(!FontRegistry.hasValueFor(DEFAULT_VIEW_FONT_NAME)) {
            getDefaultUserViewFontData();
        }
                    
        return FontRegistry.get(DEFAULT_VIEW_FONT_NAME); 
    }
    
    public static FontData getDefaultUserViewFontData() {
        // We don't have it
        if(!FontRegistry.hasValueFor(DEFAULT_VIEW_FONT_NAME)) {
            // So check user prefs...
            String fontDetails = ArchiPlugin.getInstance().getPreferenceStore().getString(IPreferenceConstants.DEFAULT_VIEW_FONT);
            if(StringUtils.isSet(fontDetails)) {
                try {
                    // Put font details from user prefs
                    FontData fd = new FontData(fontDetails);
                    FontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });
                }
                catch(Exception ex) {
                    //ex.printStackTrace();
                    setDefaultViewOSFontData();
                }
            }
            // Not set in user prefs, so set default font data
            else {
                setDefaultViewOSFontData();
            }
        }
        
        return FontRegistry.getFontData(DEFAULT_VIEW_FONT_NAME)[0];
    }
    
    public static void setDefaultUserViewFont(FontData fd) {
        // Do this first!
        FontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });

        // Then set value as this will send property change
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.DEFAULT_VIEW_FONT, fd.toString());
    }
    
    /**
     * @return The default OS font to use in a View
     */
    public static FontData getDefaultViewOSFontData() {
        // Default
        FontData fd = new FontData("Sans", 9, SWT.NORMAL);

        // Windows
        if(PlatformUtils.isWindows()) {
            fd = new FontData("Segoe UI", 9, SWT.NORMAL);
        }
        // Linux
        else if(PlatformUtils.isLinux()) {
            fd = new FontData("Sans", 9, SWT.NORMAL);
        }
        // Mac
        else if(PlatformUtils.isMac()) {
            fd = new FontData("Lucida Grande", ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.FONT_SCALING) ? 9 : 12, SWT.NORMAL);
        }

        return fd;
    }
    
    private static void setDefaultViewOSFontData() {
        FontData fd = getDefaultViewOSFontData();
        FontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });
    }

    /**
     * @param fontDataString the FontData string
     * @return A font for the fontName that might be scaled on Mac or Windows
     */
    public static Font getScaledFont(String fontDataString) {
        return get(getScaledFontString(fontDataString));
    }

    /**
     * Return a font string scaled from 96 DPI if the current DPI is not 96.
     * This can happen on Windows if the DPI is not 96 or we are on Mac.
     * @return The adjusted font if DPI is not 96, or the same font string if it is 96 DPI
     */
    public static String getScaledFontString(String fontDataString) {
        // Don't check for DPI scaling on Linux or on Mac if preference not set. Always check on Windows.
        if(PlatformUtils.isLinux() ||
                (PlatformUtils.isMac() && !ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.FONT_SCALING))) {
            return fontDataString;
        }
        
        // Get DPI - note this is deprecated in a later version of Eclipse
        int DPI = Display.getCurrent().getDPI().y;
        
        // If DPI is 96 (Windows) we don't need to do anything so just return fontDataString
        if(DPI == 96) {
            return fontDataString;
        }
        
        // Check if fontDataString is null or empty and if so use default FontData
        if(!StringUtils.isSet(fontDataString)) {
            fontDataString = getDefaultUserViewFontData().toString();
        }

        // Scale font height accordingly for Windows and Mac.
        // Mac is always 72 DPI.
        // Windows can be a different DPI if display scaling is set as follows and swt.autoScale is not "quarter" ("integer200"):
        // 125% = 120 DPI
        // 150% = 144 DPI
        // 175% = 84 DPI
        // 225% = 108 DPI
        try {
            FontData fd = new FontData(fontDataString);    // Get FontData
            int newHeight = (fd.getHeight() * 96) / DPI;   // New height is FontData height * 96 / DPI
            fd.setHeight(newHeight);
            return fd.toString();
        }
        catch(Exception ex) {
            return fontDataString;
        }
    }
    
    /**
     * @param font
     * @return The italic variant of the given font
     */
    public static Font getItalic(Font font) {
        String fontName = font.getFontData()[0].toString();
        get(fontName); // Have to ensure base font is registered
        return FontRegistry.getItalic(fontName);
    }
    
    /**
     * @param font
     * @return The bold variant of the given font
     */
    public static Font getBold(Font font) {
        String fontName = font.getFontData()[0].toString();
        get(fontName); // Have to ensure base font is registered
        return FontRegistry.getBold(fontName);
    }
}
