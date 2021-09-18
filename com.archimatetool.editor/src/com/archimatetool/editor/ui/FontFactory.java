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
public final class FontFactory {
    
    private static final String DEFAULT_VIEW_FONT_NAME = "defaultViewFont"; //$NON-NLS-1$
    
    /**
     * Font Registry
     */
    private static FontRegistry FontRegistry = new FontRegistry();

    /**
     * Temporary Font Registry to hold adjusted size fonts on Windows
     */
    private static FontRegistry windowsFontRegistry = new FontRegistry();
    
    public static Font SystemFontBold = JFaceResources.getFontRegistry().getBold(""); //$NON-NLS-1$
    
    public static Font SystemFontItalic = JFaceResources.getFontRegistry().getItalic(""); //$NON-NLS-1$
    
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
        FontData fd = new FontData("Sans", 9, SWT.NORMAL); //$NON-NLS-1$

        // Windows
        if(PlatformUtils.isWindows()) {
            fd = new FontData("Segoe UI", 9, SWT.NORMAL); //$NON-NLS-1$
        }
        // Linux
        else if(PlatformUtils.isLinux()) {
            fd = new FontData("Sans", 9, SWT.NORMAL); //$NON-NLS-1$
        }
        // Mac
        else if(PlatformUtils.isMac()) {
            fd = new FontData("Lucida Grande", 12, SWT.NORMAL); //$NON-NLS-1$
        }

        return fd;
    }
    
    private static void setDefaultViewOSFontData() {
        FontData fd = getDefaultViewOSFontData();
        FontRegistry.put(DEFAULT_VIEW_FONT_NAME, new FontData[] { fd });
    }

    /**
     * On Windows OS if the system DPI is not 96 DPI we need to adjust the size
     * of the font on the diagram View.
     * @param font
     * @return The adjusted font
     */
    public static Font getAdjustedWindowsFont(Font font) {
        if(font != null && PlatformUtils.isWindows()) {
            int DPI = font.getDevice().getDPI().y;
            if(DPI != 96) {
                FontData[] fd = font.getFontData();
                String fontName = fd[0].toString();
                if(!windowsFontRegistry.hasValueFor(fontName)) {
                    float factor = (float)96 / DPI;
                    int newHeight = (int)(fd[0].getHeight() * factor);
                    fd[0].setHeight(newHeight);
                    windowsFontRegistry.put(fontName, fd);
                }
                font = windowsFontRegistry.get(fontName);
            }
        }

        return font;
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
