/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.bolton.archimate.Testing;


/**
 * FileUtilsTests
 *
 * @author Phillip Beauvoir
 */
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
        FileUtils.deleteFolder(Testing.TMP_FOLDER);
    }
    
    @Before
    public void runBeforeEachTest() {
    }
    
    @After
    public void runAfterEachTest() {
    }
    
    // ---------------------------------------------------------------------------------------------

    /**
     * Will compare a given source Folder with a target Folder and compare that the files
     * therein are all there and are the same sizes
     * @param srcFolder
     * @param targetFolder
     * @throws IOException
     */
    private void checkSourceAndTargetFolderSame(File srcFolder, File targetFolder) throws IOException {
        File[] srcFiles = srcFolder.listFiles();
        for(int i = 0; i < srcFiles.length; i++) {
            File srcFile = srcFiles[i];
            if(srcFile.isDirectory()) {
                File subFolder = new File(targetFolder, srcFile.getName());
                checkSourceAndTargetFolderSame(srcFile, subFolder);
            }
            else {
                File targetFile = new File(targetFolder, srcFile.getName());
                checkSourceAndTargetFileSame(srcFile, targetFile);
            }
        }
    }
    
    /**
     * Will compare a given source File with a target File and compare that the file is there
     * and is the same size as the source File
     * @param srcFile
     * @param targetFile
     * @throws IOException
     */
    private void checkSourceAndTargetFileSame(File srcFile, File targetFile) throws IOException {
        if(!srcFile.exists()) {
            throw new IOException("Source File doesn't exist: " + targetFile); //$NON-NLS-1$
        }
        if(!targetFile.exists()) {
            throw new IOException("Target File doesn't exist: " + targetFile); //$NON-NLS-1$
        }
        if(targetFile.length() != srcFile.length()) {
            throw new IOException("Files don't compare in size: " + srcFile + " and " + targetFile); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }


    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    /**
     * getFileExtension() works and is lower case
     */
    @Test
    public void getFileExtension1() {
        File file = new File("test/file.TXT"); //$NON-NLS-1$
        String ext = FileUtils.getFileExtension(file);
        assertEquals("Wrong file extension", ".txt", ext); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * getFileExtension() works on no extension
     */
    @Test
    public void getFileExtension2() {
        File file = new File("test/file"); //$NON-NLS-1$
        String ext = FileUtils.getFileExtension(file);
        assertEquals("Wrong file extension", "", ext); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    // ---------------------------------------------------------------------------------------------

    /**
     * getFileNameWithoutExtension() works and is correct case
     */
    @Test
    public void getFileNameWithoutExtension1() {
        File file = new File("test/File.txt"); //$NON-NLS-1$
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("Wrong file extension", "File", name); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * getFileNameWithoutExtension() works on no extension and is correct case
     */
    @Test
    public void getFileNameWithoutExtension2() {
        File file = new File("test/File"); //$NON-NLS-1$
        String name = FileUtils.getFileNameWithoutExtension(file);
        assertEquals("Wrong file extension", "File", name); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Don't copy to same folder
     */
    @Test
    public void copyFolder_SameFolder() {
        try {
            File folderSrc = Testing.getTempFolder("src"); //$NON-NLS-1$
            FileUtils.copyFolder(folderSrc, folderSrc);
            // Shouldn't reach here
            fail("Should have thrown an Exception"); //$NON-NLS-1$
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
            File folderSrc = Testing.getTempFolder("src"); //$NON-NLS-1$
            File folderDest = Testing.getTempFolder("src/dest"); //$NON-NLS-1$
            FileUtils.copyFolder(folderSrc, folderDest);
            // Shouldn't reach here
            fail("Should have thrown an Exception"); //$NON-NLS-1$
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
        File folderSrc = Testing.TESTDATA_FOLDER;
        File folderTgt = Testing.getTempFolder("tgt"); //$NON-NLS-1$
        FileUtils.copyFolder(folderSrc, folderTgt);
        
        checkSourceAndTargetFolderSame(folderSrc, folderTgt);
        assertTrue(true);
    }

    /**
     * Copy Folder rejects non-existing folders
     */
    @Test
    public void copyFolder_NotExists() {
        File folderSrc = new File("absolutely_bogus/"); //$NON-NLS-1$
        File folderTgt = Testing.getTempFolder("tgt"); //$NON-NLS-1$
        try {
            FileUtils.copyFolder(folderSrc, folderTgt);
            // Shouldn't reach here
            fail("Should have thrown an Exception"); //$NON-NLS-1$
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
            File fileSrc = Testing.getTempFile(".txt"); //$NON-NLS-1$
            FileUtils.copyFile(fileSrc, fileSrc, false);
            // Shouldn't reach here
            fail("Should have thrown an Exception"); //$NON-NLS-1$
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
        File fileSrc = new File(Testing.TESTDATA_FOLDER, "readme.txt"); //$NON-NLS-1$
        File fileTgt = Testing.getTempFile(".txt"); //$NON-NLS-1$
        FileUtils.copyFile(fileSrc, fileTgt, false);
        
        checkSourceAndTargetFileSame(fileSrc, fileTgt);
        assertTrue(true);
    }

    /**
     * Copy File rejects non-existing folders
     */
    @Test
    public void copyFile_NotExists() throws Exception {
        File fileSrc = new File("absolutely_bogus.txt"); //$NON-NLS-1$
        File fileTgt = Testing.getTempFile(".txt"); //$NON-NLS-1$
        try {
            FileUtils.copyFile(fileSrc, fileTgt, false);
            // Shouldn't reach here
            fail("Should have thrown an Exception"); //$NON-NLS-1$
        } catch(Exception ex) {
            assertTrue(true);
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void moveFile() throws Exception {
        File folderSrc = Testing.getTempFolder("src"); //$NON-NLS-1$
        File srcFile = new File(folderSrc, "temp.xml"); //$NON-NLS-1$
        
        File folderTgt = Testing.getTempFolder("tgt"); //$NON-NLS-1$
        File tgtFile = new File(folderTgt, "temp.xml"); //$NON-NLS-1$
        
        FileUtils.copyFile(new File(Testing.TESTDATA_FOLDER, "readme.txt"), srcFile, false); //$NON-NLS-1$
        assertTrue("Test Source File should exist", srcFile.exists()); //$NON-NLS-1$
        
        FileUtils.moveFile(srcFile, tgtFile);
        assertFalse("Source File should not exist", srcFile.exists()); //$NON-NLS-1$
        assertTrue("Target File should exist", tgtFile.exists()); //$NON-NLS-1$
    }
    
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void deleteFolder() throws Exception {
        File folder = Testing.getTempFolder("delete_folder"); //$NON-NLS-1$
        FileUtils.copyFolder(new File("testdata", "testfolder"), folder, null); //$NON-NLS-1$ //$NON-NLS-2$
        FileUtils.deleteFolder(folder);
        assertFalse("Deleted Folder should not exist", folder.exists()); //$NON-NLS-1$
    }
    
    /**
     * Shouldn't be able to delete a single file
     */
    @Test
    public void deleteFolder_IfFile() throws Exception {
        File file = Testing.getTempFile(".del"); //$NON-NLS-1$
        assertTrue("Test File should exist", file.exists()); //$NON-NLS-1$
        
        FileUtils.deleteFolder(file);
        assertTrue("File should exist", file.exists()); //$NON-NLS-1$
    }
    
    /**
     * DeleteFolder ignores non-existing folders
     */
    @Test
    public void deleteFolder_NotExists() {
        File folderSrc = new File("/aFolder/absolutely_bogus/"); //$NON-NLS-1$
        try {
            FileUtils.deleteFolder(folderSrc);
            assertTrue(true);
        } catch(IOException ex) {
            fail("Shouldn't throw Exception"); //$NON-NLS-1$
        }
    }

    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testSortFiles() throws Exception {
        File folder = Testing.getTempFolder("sort_folder"); //$NON-NLS-1$
        
        File file1 = new File(folder, "d.txt"); file1.createNewFile(); //$NON-NLS-1$
        File file2 = new File(folder, "a.txt"); file2.createNewFile(); //$NON-NLS-1$
        File file3 = new File(folder, "b.txt"); file3.createNewFile(); //$NON-NLS-1$
        File file4 = new File(folder, "c.txt"); file4.createNewFile(); //$NON-NLS-1$
        
        File folder1 = new File(folder, "/d"); folder1.mkdir(); //$NON-NLS-1$
        File folder2 = new File(folder, "/a"); folder2.mkdir(); //$NON-NLS-1$
        File folder3 = new File(folder, "/b"); folder3.mkdir(); //$NON-NLS-1$
        File folder4 = new File(folder, "/c"); folder4.mkdir(); //$NON-NLS-1$
        
        File[] sorted = FileUtils.sortFiles(folder.listFiles());
        
        assertEquals("Folder not in correct position", folder2, sorted[0]); //$NON-NLS-1$
        assertEquals("Folder not in correct position", folder3, sorted[1]); //$NON-NLS-1$
        assertEquals("Folder not in correct position", folder4, sorted[2]); //$NON-NLS-1$
        assertEquals("Folder not in correct position", folder1, sorted[3]); //$NON-NLS-1$
        
        assertEquals("File not in correct position", file2, sorted[4]); //$NON-NLS-1$
        assertEquals("File not in correct position", file3, sorted[5]); //$NON-NLS-1$
        assertEquals("File not in correct position", file4, sorted[6]); //$NON-NLS-1$
        assertEquals("File not in correct position", file1, sorted[7]); //$NON-NLS-1$
    }
    
    // ---------------------------------------------------------------------------------------------
    
    /**
     * Root drive case-sensitivity issue on Windows
     */
    @Test
    public void testGetRelativePath1() {
        File rootFolder = new File("c:/rootfolder"); //$NON-NLS-1$
        File file = new File("C:/rootfolder/dir/file.txt"); //$NON-NLS-1$
        String path = FileUtils.getRelativePath(rootFolder, file);
        assertFalse("Relative Path is wrong: " + path, path.startsWith("/")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Path should be subset of parent path and preserve case
     */
    @Test
    public void testGetRelativePath2() {
        File rootFolder = new File("/RootFolder"); //$NON-NLS-1$
        File file = new File("/RootFolder/Dir/FileHere.txt"); //$NON-NLS-1$
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue("Relative Path is wrong: " + path, path.equals("Dir/FileHere.txt")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Test for absolute path
     */
    @Test
    public void testGetRelativePath3() {
        File rootFolder = new File("/rootfolder"); //$NON-NLS-1$
        File file = new File("/anotherfolder/dir/file.txt"); //$NON-NLS-1$
        String path = FileUtils.getRelativePath(file, rootFolder);
        assertTrue("Absolute Path is wrong: " + path, path.startsWith("../")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // ---------------------------------------------------------------------------------------------
    
}
