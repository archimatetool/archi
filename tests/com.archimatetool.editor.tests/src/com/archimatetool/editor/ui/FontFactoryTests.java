/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.tests.TestUtils;



@SuppressWarnings("nls")
public class FontFactoryTests {
    
    /*
     * Note for these tests - don't dispose of a font returned from FontFactory if it is accessed in another test.
     */
    
    @BeforeAll
    public static void runOnceBeforeAllTests() {
        // FontRegistry expects to see a non null Display.getCurrent()
        TestUtils.ensureDefaultDisplay();
    }
    
    @Test
    public void getFont_IsUserSet() {
        Font template = new Font(null, "Arial", 10, SWT.NORMAL);
        String fontDataString = template.getFontData()[0].toString();
        
        // Should return same font data as template
        Font font = FontFactory.get(fontDataString, JFaceResources.getDefaultFont());
        assertEquals(template.getFontData()[0], font.getFontData()[0]);
        
        template.dispose();
        font.dispose();
    }
    
    @Test
    public void getFont_IsDefault() {
        // Should return default font
        String fontDataString = null;
        Font font = FontFactory.get(fontDataString, JFaceResources.getDefaultFont());
        assertEquals(JFaceResources.getDefaultFont(), font);
        
        // Should return default font
        fontDataString = "";
        font = FontFactory.get(fontDataString, JFaceResources.getDefaultFont());
        assertEquals(JFaceResources.getDefaultFont(), font);
        
        // Should return default font
        fontDataString = "Bogus|98|Stuff";
        font = FontFactory.get(fontDataString, JFaceResources.getDefaultFont());
        assertEquals(JFaceResources.getDefaultFont(), font);
    }
    
    @Test
    public void getViewFont() {
        // If Windows DPI == 96, else OK on Mac and Linux
        assumeTrue(!PlatformUtils.isWindows() || Display.getCurrent().getDPI().y == 96);
        
        Font template = new Font(null, "Arial", 11, SWT.NORMAL);
        String fontDataString = template.getFontData()[0].toString();
        
        // Should return same font data as template
        Font font = FontFactory.getViewFont(fontDataString);
        assertEquals(template.getFontData()[0], font.getFontData()[0]);
        
        template.dispose();
        font.dispose();
    }
    
    @Test
    public void getViewFont_Null() {
        // If Windows DPI == 96, else OK on Mac and Linux
        assumeTrue(!PlatformUtils.isWindows() || Display.getCurrent().getDPI().y == 96);
        
        // If null, should return default view user font
        Font viewFont = FontFactory.getViewFont(null); // Don't dispose this
        assertEquals(FontFactory.getDefaultUserViewFont(), viewFont);
    }
    
    @Test
    public void getDefaultUserViewFont() {
        // Should by default be OS Font
        assertEquals(FontFactory.getDefaultViewOSFontData(), FontFactory.getDefaultUserViewFont().getFontData()[0]);
    }

    @Test
    public void getDefaultUserViewFontData() {
        // Should by default be OS FontData
        assertEquals(FontFactory.getDefaultViewOSFontData(), FontFactory.getDefaultUserViewFontData());
    }
    
    @Test
    public void setDefaultUserViewFontData() {
        // Save this
        FontData oldFontData = FontFactory.getDefaultUserViewFontData();
        
        Font template = new Font(null, "Arial", 12, SWT.NORMAL);
        FontFactory.setDefaultUserViewFontData(template.getFontData()[0]);
        assertEquals(template.getFontData()[0], FontFactory.getDefaultUserViewFontData());
        
        // Restore for other tests
        FontFactory.setDefaultUserViewFontData(oldFontData);
        
        template.dispose();
    }
    
    @Test
    public void getDefaultViewOSFontData() {
        assertNotNull(FontFactory.getDefaultViewOSFontData());
    }
    
    @Test
    @EnabledOnOs(OS.MAC)
    public void getDefaultViewOSFontData_Mac_With_Preference() {
        // No scaling 12 height
        FontData fd = FontFactory.getDefaultViewOSFontData();
        assertEquals(12, fd.height);
        
        // With scaling on font height should be 9
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.FONT_SCALING, true);
        fd = FontFactory.getDefaultViewOSFontData();
        assertEquals(9, fd.height);
        
        // Restore this
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.FONT_SCALING, false);
    }

    @Test
    public void getDefaultViewOSFontData_Scaling_False() {
        assertEquals(FontFactory.getDefaultViewOSFontData(), FontFactory.getDefaultViewOSFontData(false));
    }

    @Test
    @EnabledOnOs(OS.MAC)
    public void getDefaultViewOSFontData_Scaling_Mac() {
        // No scaling 12 height
        FontData fd = FontFactory.getDefaultViewOSFontData(false);
        assertEquals(12, fd.height);
        
        // With scaling on font height should be 9
        fd = FontFactory.getDefaultViewOSFontData(true);
        assertEquals(9, fd.height);
    }

    @Test
    public void getItalic() {
        Font font = new Font(null, "Sans", 10, SWT.NORMAL);
        Font italic = FontFactory.getItalic(font);
        FontData fd = italic.getFontData()[0];
        assertEquals(10, fd.getHeight());
        assertEquals(SWT.ITALIC, fd.getStyle());
        
        font.dispose();
        italic.dispose();
    }
    
    @Test
    public void getBold() {
        Font font = new Font(null, "Sans", 11, SWT.NORMAL);
        Font bold = FontFactory.getBold(font);
        FontData fd = bold.getFontData()[0];
        assertEquals(11, fd.getHeight());
        assertEquals(SWT.BOLD, fd.getStyle());
        
        font.dispose();
        bold.dispose();
    }
    
    @Test
    public void getBoldItalic() {
        Font font = new Font(null, "Sans", 12, SWT.NORMAL);
        Font bold = FontFactory.getBold(font);
        Font bolditalic = FontFactory.getItalic(bold);
        
        FontData fd = bolditalic.getFontData()[0];
        assertEquals(12, fd.getHeight());
        assertEquals(SWT.BOLD | SWT.ITALIC, fd.getStyle());
        
        font.dispose();
        bold.dispose();
        bolditalic.dispose();
    }

}
