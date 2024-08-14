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
    
    // Property to set if we will check to adjust font scaling
    public static final String ADJUST_FONT_SIZE_PROPERTY = "com.archimatetool.adjustFontSize";
    
    // Whether to check to adjust font size on Windows non 96 DPI or if property set
    private static final boolean CHECK_ADJUST_FONT_SIZE = PlatformUtils.isWindows() || "true".equals(System.getProperty(ADJUST_FONT_SIZE_PROPERTY));
    
    private static final String DEFAULT_VIEW_FONT_NAME = "defaultViewFont"; //$NON-NLS-1$
    
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
            String fontDetails = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.DEFAULT_VIEW_FONT);
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
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.DEFAULT_VIEW_FONT, fd.toString());
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
            fd = new FontData("Lucida Grande", 12, SWT.NORMAL);
        }

        return fd;
    }
    
    private static void setDefaultViewOSFontData() {
        FontData fd = getDefaultViewOSFontData();
        FontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });
    }

    /**
     * Return a font scaled from 96 DPI if the current DPI is not 96
     * This can happen on Windows if the DPI is not 96 or on Mac if we set a property.
     * @return The adjusted font if DPI is not 96, or the same font if it is 96 DPI, or null if font is null
     */
    public static Font getScaledFont96DPI(Font font) {
        if(font == null || !CHECK_ADJUST_FONT_SIZE) {
            return font;
        }
        
        int DPI = font.getDevice().getDPI().y;
        
        if(DPI != 96) {
            FontData[] fd = font.getFontData();
            
            float factor = (float)96 / DPI;
            int newHeight = (int)(fd[0].getHeight() * factor);
            
            fd[0].setHeight(newHeight);
            String fontName = fd[0].toString();
            
            if(!FontRegistry.hasValueFor(fontName)) {
                FontRegistry.put(fontName, fd);
            }
            
            font = FontRegistry.get(fontName);
        }

        return font;
    }
    
    /**
     * Return a FontData string with adjusted font height if the FontData string is not for the current platform.
     * @param fontDataString The FontData string
     * @return The Font Data String for the current platform or the same string if there is no change.
     */
    public static String getPlatformDependentFontString(String fontDataString) {
        if(StringUtils.isSet(fontDataString) && ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.PLATFORM_FONT_SCALING)) {
            float factor = 0;
            
            // On Mac but fontdata contains Windows or Linux
            if(PlatformUtils.isMac() && (fontDataString.contains("|WINDOWS|") || fontDataString.contains("|GTK|"))) {
                factor = (float)96 / 72;
            }
            // On Windows or Linux but font came from Mac
            else if(!PlatformUtils.isMac() && fontDataString.contains("|COCOA|")) {
                factor = (float)72 / 96;
            }
            
            if(factor != 0) {
                try {
                    FontData fd = new FontData(fontDataString);
                    int newHeight = Math.round(fd.getHeight() * factor); // round up in this case
                    fd.setHeight(newHeight);
                    fontDataString = fd.toString();
                }
                catch(Exception ex) {
                }
            }
        }
        
        return fontDataString;
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
