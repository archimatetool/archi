/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.junit.jupiter.api.Test;


@SuppressWarnings("nls")
public class FontFactoryTests {

    @Test
    public void testGetItalic() {
        Font font = new Font(null, "Sans", 16, SWT.NORMAL);
        Font italic = FontFactory.getItalic(font);
        FontData fd = italic.getFontData()[0];
        assertEquals(16, fd.getHeight());
        assertEquals(SWT.ITALIC, fd.getStyle());
        font.dispose();
    }
    
    @Test
    public void testGetBold() {
        Font font = new Font(null, "Sans", 16, SWT.NORMAL);
        Font bold = FontFactory.getBold(font);
        FontData fd = bold.getFontData()[0];
        assertEquals(16, fd.getHeight());
        assertEquals(SWT.BOLD, fd.getStyle());
        font.dispose();
    }
}
