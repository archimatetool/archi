/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class IArchiveManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IArchiveManagerTests.class);
    }
    
    
    @Test
    public void testFactory_createArchiveManager() throws IOException {
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        IArchimateModel model = tm.loadModel();
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        assertNotNull(archiveManager);
    }
    
    @Test
    public void testFactory_isArchiveFile() {
        assertFalse(IArchiveManager.FACTORY.isArchiveFile(TestData.TEST_MODEL_FILE_ARCHISURANCE));
        assertTrue(IArchiveManager.FACTORY.isArchiveFile(TestSupport.TEST_MODEL_FILE_ZIPPED));
    }
    
    @Test
    public void testFactory_createArchiveModelURI() {
        URI uri = URI.createURI("archive:file:///" + TestSupport.TEST_MODEL_FILE_ZIPPED.getPath() + "!/model.xml");
        assertEquals(uri, IArchiveManager.FACTORY.createArchiveModelURI(TestSupport.TEST_MODEL_FILE_ZIPPED));
    }
    
    @Test
    public void testFactory_getArchiveFilePath() {
        assertEquals("archive:file:///" + TestSupport.TEST_MODEL_FILE_ZIPPED.getPath(), IArchiveManager.FACTORY.getArchiveFilePath(TestSupport.TEST_MODEL_FILE_ZIPPED));
    }
    
    @Test
    // See https://github.com/Phillipus/archi/issues/81
    public void testFactory_createArchiveModelURI_Hash() {
        File file = new File("this#path/test.archimate");
        File file2 = new File("this%23path/test.archimate");
        
        URI expectedURI = URI.createURI("archive:file:///" + file2.getAbsolutePath() + "!/model.xml");
        assertEquals(expectedURI, IArchiveManager.FACTORY.createArchiveModelURI(file));
    }

    @Test
    // See https://github.com/Phillipus/archi/issues/81
    public void testFactory_getArchiveFilePath_Hash() {
        File file = new File("this#path/test.archimate");
        File file2 = new File("this%23path/test.archimate");
        String path = IArchiveManager.FACTORY.getArchiveFilePath(file);
        assertEquals("archive:file:///" + file2.getAbsolutePath(), path);
    }
}