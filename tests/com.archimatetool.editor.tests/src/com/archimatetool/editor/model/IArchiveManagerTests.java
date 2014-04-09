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

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateModel;


@SuppressWarnings("nls")
public class IArchiveManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IArchiveManagerTests.class);
    }
    
    
    @Test
    public void testFactory_createArchiveManager() throws IOException {
        ArchimateTestModel tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        IArchimateModel model = tm.loadModel();
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        assertNotNull(archiveManager);
    }
    
    @Test
    public void testFactory_isArchiveFile() {
        assertFalse(IArchiveManager.FACTORY.isArchiveFile(TestSupport.TEST_MODEL_FILE_ARCHISURANCE));
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
}