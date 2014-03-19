/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.Application;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.tests.TestUtils;



@SuppressWarnings("nls")
public class CheckForNewVersionActionTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CheckForNewVersionActionTests.class);
    }
    
    private CheckForNewVersionAction action;
    
    @Before
    public void runOnceBeforeEachTest() {
        action = new CheckForNewVersionAction();
    }
    
    @Test
    public void testGetOnlineVersion() throws IOException {
        String newVersion = "10.1.1";
        
        File file = TestUtils.createTempFile(".txt");
        FileWriter fw = new FileWriter(file);
        fw.write(newVersion);
        fw.close();
        
        URL url = file.toURI().toURL();
        assertEquals(newVersion, action.getOnlineVersion(url));
    }

    @Test
    public void testNewVersionIsGreater() {
        String newVersion = "10.1.1";
        String thisVersion = System.getProperty(Application.APPLICATION_VERSIONID);
        assertNotNull(thisVersion);
        assertEquals(1, StringUtils.compareVersionNumbers(newVersion, thisVersion));
    }
    
}