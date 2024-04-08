/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;

import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.tests.TestUtils;

@SuppressWarnings("nls")
public class DropinsPluginHandlerTests {
    
    @Test
    public void testGetPluginNameJar() {
        String name = "org.opengroup.archimate.xmlexchange_0.5.0.201411181508.jar";
        assertEquals("org.opengroup.archimate.xmlexchange", new DropinsPluginHandler().getPluginName(name));
    }
    
    @Test
    public void testGetPluginNameFolder() {
        String name = "org.opengroup.archimate.xmlexchange_0.5.0.201411181508";
        assertEquals("org.opengroup.archimate.xmlexchange", new DropinsPluginHandler().getPluginName(name));
    }

    @Test
    public void testGetPluginVersionJar() {
        String name = "org.opengroup.archimate.xmlexchange_0.5.0.201411181508.jar";
        assertEquals("0.5.0.201411181508", new DropinsPluginHandler().getPluginVersion(name));
    }
    
    @Test
    public void testGetPluginVersionFolder() {
        String name = "org.opengroup.archimate.xmlexchange_0.5.0.201411181508";
        assertEquals("0.5.0.201411181508", new DropinsPluginHandler().getPluginVersion(name));
    }
    
    @Test
    public void testIsPluginZipFile() throws IOException {
        DropinsPluginHandler handler = new DropinsPluginHandler();
        
        File tmpZipFile = TestUtils.createTempFile(".zip");
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        ZipUtils.addStringToZip("something", "aFile.jar", zOut);
        zOut.flush();
        zOut.close();
        
        assertFalse(handler.isPluginZipFile(tmpZipFile));
        
        out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        zOut = new ZipOutputStream(out);
        ZipUtils.addStringToZip("", DropinsPluginHandler.MAGIC_ENTRY, zOut);
        zOut.flush();
        zOut.close();
        
        assertTrue(handler.isPluginZipFile(tmpZipFile));
    }

}