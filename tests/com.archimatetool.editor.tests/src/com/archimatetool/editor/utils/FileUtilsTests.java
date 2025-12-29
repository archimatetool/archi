/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.tests.TestUtils;



/**
 * FileUtilsTests
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FileUtilsTests {

    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeAll
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterAll
    public static void runOnceAfterAllTests() throws IOException {
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }
    
    @BeforeEach
    public void runBeforeEachTest() {
    }
    
    @AfterEach
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
        assertEquals(".txt", ext, "Wrong file extension");
    }
    
    /**
     * getFileExtension() works on no extension
     */
    @Test
    public void getFileExtension2() {
        File file = new File("test/file");
        String ext = FileUtils.getFileExtension(file);
        assertEquals("", ext, "Wrong file extension");
    }
    
    // ---------------------------------------------------------------------------------------------

    /**
     * getFileNameWithoutExtension() works and is correct case
     */
    @Test
    public void getFileNameWithoutExtension1() {
        File file = new File("test/File.txt");
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("File", name, "Wrong file extension");
    }
    
    /**
     * getFileNameWithoutExtension() works on no extension and is correct case
     */
    @Test
    public void getFileNameWithoutExtension2() {
        File file = new File("test/File");
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("File", name, "Wrong file extension");
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
        assertTrue(srcFile.exists(), "Test Source File should exist");
        
        FileUtils.moveFile(srcFile, tgtFile);
        assertFalse(srcFile.exists(), "Source File should not exist");
        assertTrue(tgtFile.exists(), "Target File should exist");
    }
    
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void deleteFolder() throws Exception {
        File folder = TestUtils.createTempFolder("delete_folder");
        FileUtils.copyFolder(new File(TestSupport.getTestDataFolder(), "filetest/testfolder"), folder, null);
        FileUtils.deleteFolder(folder);
        assertFalse(folder.exists(), "Deleted Folder should not exist");
    }
    
    /**
     * Shouldn't be able to delete a single file
     */
    @Test
    public void deleteFolder_IfFile() throws Exception {
        File file = TestUtils.createTempFile(".del");
        assertTrue(file.exists(), "Test File should exist");
        
        FileUtils.deleteFolder(file);
        assertTrue(file.exists(), "File should exist");
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
    public void deleteDir() throws Exception {
        File folder = TestUtils.createTempFolder("delete_folder");
        
        Path sub1 = Files.createDirectory(folder.toPath().resolve("sub1"));
        Path sub2 = Files.createDirectory(folder.toPath().resolve("sub2"));
        
        Files.createFile(folder.toPath().resolve("file1"));
        Files.createFile(sub1.resolve("file2"));
        Files.createFile(sub2.resolve("file3"));
        
        FileUtils.deleteDir(folder.toPath());
        assertFalse(folder.exists(), "Deleted Folder should not exist");
    }
    
    @Test
    public void deleteDir_NotDirectory() throws Exception {
        File file = TestUtils.createTempFile(".tmp");
        
        IOException thrown = assertThrows(IOException.class, () -> {
            FileUtils.deleteDir(file.toPath());
        });
        
        assertTrue(thrown.getMessage().contains("is not a directory"));
    }
    
    @Test
    public void deleteDir_NotExists() {
        assertThrows(NoSuchFileException.class, () -> {
            FileUtils.deleteDir(Path.of("/aFolder/absolutely_bogus/"));
        });
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
        
        assertEquals(folder2, sorted[0], "Folder not in correct position");
        assertEquals(folder3, sorted[1], "Folder not in correct position");
        assertEquals(folder4, sorted[2], "Folder not in correct position");
        assertEquals(folder1, sorted[3], "Folder not in correct position");
        
        assertEquals(file2, sorted[4], "File not in correct position");
        assertEquals(file3, sorted[5], "File not in correct position");
        assertEquals(file4, sorted[6], "File not in correct position");
        assertEquals(file1, sorted[7], "File not in correct position");
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
        assertFalse(path.startsWith("/"), "Relative Path is wrong: " + path);
    }

    /**
     * Path should be subset of parent path and preserve case
     */
    @Test
    public void testGetRelativePath2() {
        File rootFolder = new File("/RootFolder");
        File file = new File("/RootFolder/Dir/FileHere.txt");
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue(path.equals("Dir/FileHere.txt"), "Relative Path is wrong: " + path);
    }
    
    /**
     * Test for absolute path
     */
    @Test
    public void testGetRelativePath3() {
        File rootFolder = new File("/rootfolder");
        File file = new File("/anotherfolder/dir/file.txt");
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue(path.startsWith("../"), "Absolute Path is wrong: " + path);
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
