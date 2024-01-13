/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class ArchiveManagerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private IDiagramModel dm;
    private ArchiveManager archiveManager;
    
    @Before
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
    public void testCreateImage_Null() {
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
    public void testHasImages() throws IOException {
        assertFalse(archiveManager.hasImages());
        
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        // Not good enough to just set the image path, the matching image has to be loaded
        dmImage.setImagePath("somePath");
        assertFalse(archiveManager.hasImages());
        
        // Load an image
        File imgFile = new File(TestSupport.getTestDataFolder(), "/img/img1.png");
        String pathName = archiveManager.addImageFromFile(imgFile);
        dmImage.setImagePath(pathName);
        assertTrue(archiveManager.hasImages());
    }    
    
    @Test
    public void testSaveModel() throws IOException {
        IDiagramModelImage dmImage = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        dm.getChildren().add(dmImage);
        
        File file = TestUtils.createTempFile(".archimate");
        model.setFile(file);
        
        archiveManager.saveModel();
        assertTrue(file.exists());
        
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
}