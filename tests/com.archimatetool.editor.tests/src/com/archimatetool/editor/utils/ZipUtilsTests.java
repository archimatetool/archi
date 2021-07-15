/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.tests.TestUtils;


/**
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ZipUtilsTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ZipUtilsTests.class);
    }
    
    private File testZipFile = new File(TestSupport.getTestDataFolder(), "zip/test.zip");
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() {
    }
    
    @Before
    public void runBeforeEachTest() {
    }
    
    @After
    public void runAfterEachTest() throws IOException {
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }

    // ---------------------------------------------------------------------------------------------
    // TESTS GO HERE 
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testIsZipFile() throws IOException {
        assertTrue(ZipUtils.isZipFile(testZipFile));
        
        File otherFile = new File(TestSupport.getTestDataFolder(), "img/img1.png");
        assertFalse(ZipUtils.isZipFile(otherFile));
    }
    
    @Test
    public void testAddFolderToZip() throws IOException {
        // Zip file
        File tmpZipFile = TestUtils.createTempFile(".zip");

        File srcFolder = new File(TestSupport.getTestDataFolder(), "filetest");
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);

        ZipUtils.addFolderToZip(srcFolder, zOut, null, null);

        zOut.flush();
        zOut.close();
        
        // Extract them to see if they are there
        File tmpOutFolder = TestUtils.createTempFolder("ziptest");
        ZipUtils.unpackZip(tmpZipFile, tmpOutFolder);

        // Compare them
        TestSupport.checkSourceAndTargetFolderSame(srcFolder, tmpOutFolder);
        assertTrue(true);
    }
    
    @Test
    public void testAddFileToZip() throws IOException {
        // Zip file
        File tmpZipFile = TestUtils.createTempFile(".zip");
        
        // File to add
        File srcFile = new File(TestSupport.getTestDataFolder(), "filetest/readme.txt");
        
        // Add it
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        ZipUtils.addFileToZip(srcFile, srcFile.getName(), zOut);
        
        zOut.flush();
        zOut.close();
        
        // Extract it to see if it's there
        File tmpOutFolder = TestUtils.createTempFolder("ziptest");
        ZipUtils.unpackZip(tmpZipFile, tmpOutFolder);
        File outFile = new File(tmpOutFolder, srcFile.getName());
        
        // Compare file
        TestSupport.checkSourceAndTargetFileSame(srcFile, outFile);
        assertTrue(true);
    }
    
    @Test
    public void testAddImageToZip() throws IOException {
        // Zip file
        File tmpZipFile = TestUtils.createTempFile(".zip");
        
        // Image to add
        Image image = new Image(Display.getCurrent(), 16, 16);
        
        // Add it
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);

        ZipUtils.addImageToZip(image, "img.png", zOut, SWT.IMAGE_PNG, null);
        
        image.dispose();
        
        zOut.flush();
        zOut.close();

        // Extract it to see if it's there
        File tmpOutFolder = TestUtils.createTempFolder("ziptest");
        ZipUtils.unpackZip(tmpZipFile, tmpOutFolder);
        File outFile = new File(tmpOutFolder, "img.png");
        
        assertTrue(outFile.exists());

        // Image size will vary according to platform and device scale
        assertTrue(outFile.length() > 70);
    }
    
    @Test
    public void testAddStringToZip() throws IOException {
        // Zip file
        File tmpZipFile = TestUtils.createTempFile(".zip");
        
        // Add Entry
        String text = "Phillipus is cool!";
        String entryName = "sub/phillipus.text";

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        ZipUtils.addStringToZip(text, entryName, zOut);
        
        zOut.flush();
        zOut.close();
        
        String textReturned = ZipUtils.extractZipEntry(tmpZipFile, entryName);
        assertEquals(text, textReturned);
    }
    
    @Test
    public void testHasZipEntry() throws IOException {
        String entry = "Archisurance.archimate";
        boolean result = ZipUtils.hasZipEntry(testZipFile, entry);
        assertTrue(result);

        entry = "bogus.txt";
        result = ZipUtils.hasZipEntry(testZipFile, entry);
        assertFalse(result);
    }    
    
    @Test
    public void testExtractZipEntry_String() throws IOException {
        String entry = "space dir/aFile.txt";
        String text = ZipUtils.extractZipEntry(testZipFile, entry);
        assertEquals("This is a file", text);
    }
    
    @Test
    public void testExtractZipEntry_File() throws Exception {
        String entry = "space dir/aFile.txt";
        File fileTemp = TestUtils.createTempFile(".txt");
        ZipUtils.extractZipEntry(testZipFile, entry, fileTemp);
        // Check it's OK by checking its file length
        assertEquals(14, fileTemp.length());
    }

    @Test
    public void testGetZipFileEntryNames() throws Exception {
        List<String> entries = ZipUtils.getZipFileEntryNames(testZipFile);
        
        assertEquals(5, entries.size());
        
        assertEquals("Archisurance.archimate", entries.get(0));
        assertEquals("space dir/aFile.txt", entries.get(1));
        assertEquals("space dir/New Text Document.txt", entries.get(2));
        assertEquals("A File.txt", entries.get(3));
        assertEquals("Another File.txt", entries.get(4));
    }

    @Test
    public void testUnpackZipFile() throws Exception {
        // Get a temp folder, but we don't want to create it, because we want to test
        // that the routine will create it
        File folderTemp = new File(TestUtils.getMainTempFolder(), "ziptest");
        
        // Unzip
        ZipUtils.unpackZip(testZipFile, folderTemp);
        
        // Test output folder exists
        assertTrue("Zip Output folder not created: " + folderTemp, folderTemp.exists());
        
        // Compare files
        File file = new File(folderTemp, "Archisurance.archimate");
        assertEquals(105642, file.length());
        
        file = new File(folderTemp, "space dir/aFile.txt");
        assertEquals(14, file.length());
        
        file = new File(folderTemp, "space dir/New Text Document.txt");
        assertEquals(6, file.length());
        
        file = new File(folderTemp, "A File.txt");
        assertEquals(6, file.length());
        
        file = new File(folderTemp, "Another File.txt");
        assertEquals(12, file.length());
    }

    /**
     * Bogus zip file not existing should throw Exception
     */
    @Test (expected=FileNotFoundException.class)
    public void testUnpackZipFile_Exception() throws IOException {
        ZipUtils.unpackZip(new File("bogus_file.zip"), TestUtils.createTempFolder("ziptest"));
    }

    /**
     * Test for cases where a zip entry has "../../file" which would unpack and possibly over-write a file
     * in a parent folder.
     */
    @Test (expected=IOException.class)
    public void testUnpackZipFileTriesToUnzipInParentRelativeFolder() throws Exception {
        // Create a Zip file
        File tmpZipFile = TestUtils.createTempFile(".zip");
        
        // Add afile
        File srcFile = new File(TestSupport.getTestDataFolder(), "filetest/readme.txt");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpZipFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        // But add it with a malicious path
        ZipUtils.addFileToZip(srcFile, "../../" + srcFile.getName(), zOut);
        zOut.flush();
        zOut.close();

        // Now unpack it and expect an exception
        File folderTemp = new File(TestUtils.getMainTempFolder(), "ziptest");
        ZipUtils.unpackZip(tmpZipFile, folderTemp);
    }
}
