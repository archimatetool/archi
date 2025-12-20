/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class ArchiveManagerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private IDiagramModel dm;
    private ArchiveManager archiveManager;
    
    @AfterAll
    public static void runOnceAfterAllTests() throws IOException {
        FileUtils.deleteFolder(TestUtils.TMP_FOLDER);
    }
    
    @BeforeEach
    public void runBeforeEachTest() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        dm = model.getDefaultDiagramModel();
        archiveManager = (ArchiveManager)model.getAdapter(IArchiveManager.class);
    }
    
    @Test
    public void testCreateArchiveManager() throws Exception {
        assertNotNull(archiveManager);
        assertTrue(archiveManager.getImagePaths().isEmpty());
        assertTrue(archiveManager.getLoadedImagePaths().isEmpty());
        assertFalse(archiveManager.hasImages());
    }
    
    @Test
    public void testAddImageFromFile_Exception() {
        IOException thrown = assertThrows(IOException.class, () -> {
            archiveManager.addImageFromFile(new File("bogus.pomp"));
        });
        
        assertEquals("Cannot find file", thrown.getMessage());
    }
    
    @Test
    public void testAddImageFromFile_WrongFileFormat() {
        IOException thrown = assertThrows(IOException.class, () -> {
            archiveManager.addImageFromFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        });
        
        assertEquals("Not a supported image file", thrown.getMessage());
    }

    @Test
    public void testAddImageFromFile() throws Exception {
        // Add a DiagramModelImage object
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        // Loaded images and image refs should be empty
        assertTrue(archiveManager.getLoadedImagePaths().isEmpty());
        assertTrue(archiveManager.getImagePaths().isEmpty());

        // Load an image from file
        File imgFile = new File(TestSupport.getTestDataFolder(), "/img/img1.png");
        String pathName = archiveManager.addImageFromFile(imgFile);
        
        // Path name should be set
        assertNotNull(pathName);
        
        // Should be one loaded image
        assertEquals(1, archiveManager.getLoadedImagePaths().size());
        
        // And no referenced image
        assertTrue(archiveManager.getImagePaths().isEmpty());
        
        // Now set the image path
        dmImage.setImagePath(pathName);
        
        // Should be one loaded image
        assertEquals(1, archiveManager.getLoadedImagePaths().size());
        assertTrue(archiveManager.getLoadedImagePaths().contains(pathName));
        
        // And one referenced image
        assertEquals(1, archiveManager.getImagePaths().size());
        assertTrue(archiveManager.getImagePaths().contains(pathName));
        
        // If we add the image again...
        String pathName2 = archiveManager.addImageFromFile(imgFile);
        
        // Things should be the same
        assertEquals(pathName, pathName2);
        assertEquals(1, archiveManager.getLoadedImagePaths().size());
    }
    
    @Test
    public void testCreateImage_Null() throws Exception {
        assertNull(archiveManager.createImage("something"));
    }
    
    @Test
    public void testCreateImage() throws Exception {
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        File imgFile = new File(TestSupport.getTestDataFolder(), "/img/img1.png");
        String pathName = archiveManager.addImageFromFile(imgFile);
        
        Image image = archiveManager.createImage(pathName);
        assertNotNull(image);
        image.dispose();
    }
    
    @Test
    public void testGetImagePaths() {
        assertTrue(archiveManager.getImagePaths().isEmpty());
        
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        String pathName = "/aPath.png";
        dmImage.setImagePath(pathName);
        
        assertEquals(1, archiveManager.getImagePaths().size());
        assertTrue(archiveManager.getImagePaths().contains(pathName));
    }
    
    @Test
    public void testLoadImages() throws Exception {
        model.setFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        
        assertTrue(archiveManager.getLoadedImagePaths().isEmpty());
        
        archiveManager.loadImages();
        assertEquals(2, archiveManager.getLoadedImagePaths().size());
    }
    
    @Test
    public void testLoadImagesFromModelFile() throws Exception {
        // File is null, returns false
        boolean result = archiveManager.loadImagesFromModelFile(null);
        assertFalse(result);
        
        // File contains no images, returns false
        result = archiveManager.loadImagesFromModelFile(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        assertFalse(result);
        
        // Loaded images is empty
        assertTrue(archiveManager.getLoadedImagePaths().isEmpty());
        
        // Load some images
        result = archiveManager.loadImagesFromModelFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        assertTrue(result);
        assertEquals(2, archiveManager.getLoadedImagePaths().size());
        
        // But should not be in use
        assertEquals(0, archiveManager.getImagePaths().size());
    }
    
    @Test
    public void testHasImages() throws IOException {
        assertFalse(archiveManager.hasImages());
        
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        // Not good enough to just set the image path, the matching image has to be loaded
        dmImage.setImagePath("somePath");
        assertFalse(archiveManager.hasImages());
        
        // So load the image
        archiveManager.loadImagesFromModelFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        dmImage.setImagePath(archiveManager.getLoadedImagePaths().iterator().next());
        assertTrue(archiveManager.hasImages());
    }    
    
    @Test
    public void testSaveModel() throws IOException {
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        File file = TestUtils.createTempFile(".archimate");
        model.setFile(file);
        
        archiveManager.saveModel();
        
        // Not an archive file
        assertTrue(file.exists());
        assertFalse(IArchiveManager.FACTORY.isArchiveFile(file));
        
        // Is an archive file
        archiveManager.loadImagesFromModelFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        dmImage.setImagePath(archiveManager.getLoadedImagePaths().iterator().next());
        
        archiveManager.saveModel();
        assertTrue(IArchiveManager.FACTORY.isArchiveFile(file));
        
        file.delete();
    }
    
    @Test
    public void testSaveModel_ResourceSame() throws IOException {
        File file = TestUtils.createTempFile(".archimate");
        model.setFile(file);
        
        Resource resource = model.eResource();
        assertNull(resource);
        
        archiveManager.saveModel();
        resource = model.eResource();
        assertNotNull(resource);
        
        // Change file name
        file = TestUtils.createTempFile(".archimate");
        model.setFile(file);
        archiveManager.saveModel();

        assertSame(resource, model.eResource());
    }
    
    @Test
    public void testClone() throws IOException {
        archiveManager.loadImagesFromModelFile(TestSupport.TEST_MODEL_FILE_ZIPPED);
        
        IArchiveManager clone = archiveManager.clone(model);
        assertNotSame(clone, archiveManager);
        
        for(String entryName : archiveManager.getLoadedImagePaths()) {
            assertSame(archiveManager.getBytesFromEntry(entryName), clone.getBytesFromEntry(entryName));
        }
    }
    
    @Test
    public void testSaveModelAsNonArchiveFormat() throws Exception {
        File tmpFolder = TestUtils.createTempFolder("modelTest");
        
        // Add 3 notes to the model
        IDiagramModelNote note1 = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note1.setBounds(10, 10, -1, -1);
        dm.getChildren().add(note1);
        
        IDiagramModelNote note2 = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note2.setBounds(100, 10, -1, -1);
        dm.getChildren().add(note2);

        IDiagramModelNote note3 = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note3.setBounds(200, 10, -1, -1);
        dm.getChildren().add(note3);
        
        // Load three images from file and set the paths to the notes
        File imgFile1 = new File(TestSupport.getTestDataFolder(), "/img/img1.png");
        String pathName1 = archiveManager.addImageFromFile(imgFile1);
        note1.setImagePath(pathName1);
        
        File imgFile2 = new File(TestSupport.getTestDataFolder(), "/img/img2.png");
        String pathName2 = archiveManager.addImageFromFile(imgFile2);
        note2.setImagePath(pathName2);
        
        File imgFile3 = new File(TestSupport.getTestDataFolder(), "/img/img3.png");
        String pathName3 = archiveManager.addImageFromFile(imgFile3);
        note3.setImagePath(pathName3);

        // Save to archive file
        File file = new File(tmpFolder, "model.archimate");
        model.setFile(file);
        archiveManager.saveModel();
        assertTrue(file.exists());
        assertTrue(IArchiveManager.FACTORY.isArchiveFile(file));
        
        // Now add a .git folder alongside the model
        Path gitPath = Files.createDirectory(Path.of(tmpFolder.getPath(), ".git"));
        assertTrue(Files.exists(gitPath));
        
        // Save the model again and now it should not be archive format
        archiveManager.saveModel();
        assertFalse(IArchiveManager.FACTORY.isArchiveFile(file));
        
        // Check the model file and image files exist
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), "model.archimate")));
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName1)));
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName2)));
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName3)));
        
        // Add some other files to the images folder. These ones should not be deleted.
        Path img1 = Files.createFile(Path.of(tmpFolder.getPath(), "images/1.png"));
        Path img2 = Files.createFile(Path.of(tmpFolder.getPath(), "images/2.png"));
        Path img3 = Files.createFile(Path.of(tmpFolder.getPath(), "images/3.png"));
        Path subFolder = Files.createDirectory(Path.of(tmpFolder.getPath(), "images/subfolder"));
        
        // Now remove all images from the model
        note1.setImagePath(null);
        note2.setImagePath(null);
        note3.setImagePath(null);
        
        // Save the model
        archiveManager.saveModel();
        
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), "model.archimate")));
        assertFalse(IArchiveManager.FACTORY.isArchiveFile(file));
        
        // The image files should not exist
        assertFalse(Files.exists(Path.of(tmpFolder.getPath(), pathName1)));
        assertFalse(Files.exists(Path.of(tmpFolder.getPath(), pathName2)));
        assertFalse(Files.exists(Path.of(tmpFolder.getPath(), pathName3)));
        
        // But the other ones should exist
        assertTrue(Files.exists(img1));
        assertTrue(Files.exists(img2));
        assertTrue(Files.exists(img3));
        assertTrue(Files.exists(subFolder));
        
        // Restore the image paths
        note1.setImagePath(pathName1);
        note2.setImagePath(pathName2);
        note3.setImagePath(pathName3);
        
        // Save the model
        archiveManager.saveModel();
        
        // The image files should exist
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName1)));
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName2)));
        assertTrue(Files.exists(Path.of(tmpFolder.getPath(), pathName3)));
        
        // And the other ones should exist
        assertTrue(Files.exists(img1));
        assertTrue(Files.exists(img2));
        assertTrue(Files.exists(img3));
        assertTrue(Files.exists(subFolder));
    }

}