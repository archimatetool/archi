/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.util.ArchimateModelUtils;


@SuppressWarnings("nls")
public class DiagramModelUtilsTests {
    
    IArchimateModel model;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelUtilsTests.class);
    }
    
    @Before
    public void runBeforeEachTest() throws IOException {
        model = TestSupport.loadModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testFindReferencedDiagramsForElement() throws Exception {
        IArchimateElement element = null; 
        
        // Null should return empty list
        List<IDiagramModel> list = DiagramModelUtils.findReferencedDiagramsForElement(element);
        assertNotNull(list);
        assertEquals(0, list.size());
        
        // Get some elements and find 'em
        element = (IArchimateElement)ArchimateModelUtils.getObjectByID(model, "521");
        assertNotNull(element);
        
        list = DiagramModelUtils.findReferencedDiagramsForElement(element);
        assertEquals(6, list.size());
        assertEquals("4025", list.get(0).getId());
        assertEquals("3761", list.get(1).getId());
        assertEquals("3722", list.get(2).getId());
        assertEquals("3999", list.get(3).getId());
        assertEquals("4165", list.get(4).getId());
        assertEquals("4224", list.get(5).getId());
    }
}