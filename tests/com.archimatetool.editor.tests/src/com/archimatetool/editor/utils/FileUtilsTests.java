/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;



/**
 * FileUtilsTests
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FileUtilsTests {

    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FileUtilsTests.class);
    }
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() throws IOException {
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }
    
    @Before
    public void runBeforeEachTest() {
    }
    
    @After
    public void runAfterEachTest() {
    }
    
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    /**
     * getFileExtension() works and is lower case
     */
    @Test
    public void getFileExtension1() {
        File file = new File("test/file.TXT");
        String ext = FileUtils.getFileExtension(file);
        assertEquals("Wrong file extension", ".txt", ext);
    }
    
    /**
     * getFileExtension() works on no extension
     */
    @Test
    public void getFileExtension2() {
        File file = new File("test/file");
        String ext = FileUtils.getFileExtension(file);
        assertEquals("Wrong file extension", "", ext);
    }
    
    // ---------------------------------------------------------------------------------------------

    /**
     * getFileNameWithoutExtension() works and is correct case
     */
    @Test
    public void getFileNameWithoutExtension1() {
        File file = new File("test/File.txt");
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("Wrong file extension", "File", name);
    }
    
    /**
     * getFileNameWithoutExtension() works on no extension and is correct case
     */
    @Test
    public void getFileNameWithoutExtension2() {
        File file = new File("test/File");
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("Wrong file extension", "File", name);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Don't copy to same folder
     */
    @Test
    public void copyFolder_SameFolder() {
        try {
            File folderSrc = TestUtils.createTempFolder("src");
            FileUtils.copyFolder(folderSrc, folderSrc);
            // Shouldn't reach here
            fail("Should have thrown an Exception");
        }
        catch(IOException ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Don't copy to sub folder
     */
    @Test
    public void copyFolder_SubFolder() {
        try {
            File folderSrc = TestUtils.createTempFolder("src");
            File folderDest = TestUtils.createTempFolder("src/dest");
            FileUtils.copyFolder(folderSrc, folderDest);
            // Shouldn't reach here
            fail("Should have thrown an Exception");
        }
        catch(IOException ex) {
            assertTrue(true);
        }
    }

    /**
     * Copy Folder works OK
     */
    @Test
    public void copyFolder_FilesValid() throws Exception {
        File folderSrc = TestSupport.getTestDataFolder();
        File folderTgt = TestUtils.createTempFolder("tgt");
        FileUtils.copyFolder(folderSrc, folderTgt);
        
        TestSupport.checkSourceAndTargetFolderSame(folderSrc, folderTgt);
        assertTrue(true);
    }

    /**
     * Copy Folder rejects non-existing folders
     */
    @Test
    public void copyFolder_NotExists() {
        File folderSrc = new File("absolutely_bogus/");
        File folderTgt = TestUtils.createTempFolder("tgt");
        try {
            FileUtils.copyFolder(folderSrc, folderTgt);
            // Shouldn't reach here
            fail("Should have thrown an Exception");
        } catch(Exception ex) {
            assertTrue(true);
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Don't copy to same file
     */
    @Test
    public void copyFile_SameFile() {
        try {
            File fileSrc = TestUtils.createTempFile(".txt");
            FileUtils.copyFile(fileSrc, fileSrc, false);
            // Shouldn't reach here
            fail("Should have thrown an Exception");
        }
        catch(IOException ex) {
            assertTrue(true);
        }
    }
    
    /**
     * Copy File works OK
     */
    @Test
    public void copyFile_FileValid() throws Exception {
        File fileSrc = new File(TestSupport.getTestDataFolder(), "filetest/readme.txt");
        File fileTgt = TestUtils.createTempFile(".txt");
        FileUtils.copyFile(fileSrc, fileTgt, false);
        
        TestSupport.checkSourceAndTargetFileSame(fileSrc, fileTgt);
        assertTrue(true);
    }

    /**
     * Copy File rejects non-existing folders
     */
    @Test
    public void copyFile_NotExists() throws Exception {
        File fileSrc = new File("absolutely_bogus.txt");
        File fileTgt = TestUtils.createTempFile(".txt");
        try {
            FileUtils.copyFile(fileSrc, fileTgt, false);
            // Shouldn't reach here
            fail("Should have thrown an Exception");
        } catch(Exception ex) {
            assertTrue(true);
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void moveFile() throws Exception {
        File folderSrc = TestUtils.createTempFolder("src");
        File srcFile = new File(folderSrc, "temp.xml");
        
        File folderTgt = TestUtils.createTempFolder("tgt");
        File tgtFile = new File(folderTgt, "temp.xml");
        
        FileUtils.copyFile(new File(TestSupport.getTestDataFolder(), "filetest/readme.txt"), srcFile, false);
        assertTrue("Test Source File should exist", srcFile.exists());
        
        FileUtils.moveFile(srcFile, tgtFile);
        assertFalse("Source File should not exist", srcFile.exists());
        assertTrue("Target File should exist", tgtFile.exists());
    }
    
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void deleteFolder() throws Exception {
        File folder = TestUtils.createTempFolder("delete_folder");
        FileUtils.copyFolder(new File(TestSupport.getTestDataFolder(), "filetest/testfolder"), folder, null);
        FileUtils.deleteFolder(folder);
        assertFalse("Deleted Folder should not exist", folder.exists());
    }
    
    /**
     * Shouldn't be able to delete a single file
     */
    @Test
    public void deleteFolder_IfFile() throws Exception {
        File file = TestUtils.createTempFile(".del");
        assertTrue("Test File should exist", file.exists());
        
        FileUtils.deleteFolder(file);
        assertTrue("File should exist", file.exists());
    }
    
    /**
     * DeleteFolder ignores non-existing folders
     */
    @Test
    public void deleteFolder_NotExists() {
        File folderSrc = new File("/aFolder/absolutely_bogus/");
        try {
            FileUtils.deleteFolder(folderSrc);
            assertTrue(true);
        } catch(IOException ex) {
            fail("Shouldn't throw Exception");
        }
    }

    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testSortFiles() throws Exception {
        File folder = TestUtils.createTempFolder("sort_folder");
        
        File file1 = new File(folder, "d.txt"); file1.createNewFile();
        File file2 = new File(folder, "a.txt"); file2.createNewFile();
        File file3 = new File(folder, "b.txt"); file3.createNewFile();
        File file4 = new File(folder, "c.txt"); file4.createNewFile();
        
        File folder1 = new File(folder, "/d"); folder1.mkdir();
        File folder2 = new File(folder, "/a"); folder2.mkdir();
        File folder3 = new File(folder, "/b"); folder3.mkdir();
        File folder4 = new File(folder, "/c"); folder4.mkdir();
        
        File[] sorted = FileUtils.sortFiles(folder.listFiles());
        
        assertEquals("Folder not in correct position", folder2, sorted[0]);
        assertEquals("Folder not in correct position", folder3, sorted[1]);
        assertEquals("Folder not in correct position", folder4, sorted[2]);
        assertEquals("Folder not in correct position", folder1, sorted[3]);
        
        assertEquals("File not in correct position", file2, sorted[4]);
        assertEquals("File not in correct position", file3, sorted[5]);
        assertEquals("File not in correct position", file4, sorted[6]);
        assertEquals("File not in correct position", file1, sorted[7]);
    }
    
    // ---------------------------------------------------------------------------------------------
    
    /**
     * Root drive case-sensitivity issue on Windows
     */
    @Test
    public void testGetRelativePath1() {
        File rootFolder = new File("c:/rootfolder");
        File file = new File("C:/rootfolder/dir/file.txt");
        String path = FileUtils.getRelativePath(rootFolder, file);
        assertFalse("Relative Path is wrong: " + path, path.startsWith("/"));
    }

    /**
     * Path should be subset of parent path and preserve case
     */
    @Test
    public void testGetRelativePath2() {
        File rootFolder = new File("/RootFolder");
        File file = new File("/RootFolder/Dir/FileHere.txt");
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue("Relative Path is wrong: " + path, path.equals("Dir/FileHere.txt"));
    }
    
    /**
     * Test for absolute path
     */
    @Test
    public void testGetRelativePath3() {
        File rootFolder = new File("/rootfolder");
        File file = new File("/anotherfolder/dir/file.txt");
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue("Absolute Path is wrong: " + path, path.startsWith("../"));
    }

    // ---------------------------------------------------------------------------------------------
    
    
    @Test
    public void testIsFolderEmpty() throws IOException {
        File folder = TestUtils.createTempFolder("folder");
        assertTrue(FileUtils.isFolderEmpty(folder));
        
        new File(folder, ".DS_Store").createNewFile();
        assertTrue(FileUtils.isFolderEmpty(folder));
        
        new File(folder, "afile.txt").createNewFile();
        assertFalse(FileUtils.isFolderEmpty(folder));
    }
    
}
