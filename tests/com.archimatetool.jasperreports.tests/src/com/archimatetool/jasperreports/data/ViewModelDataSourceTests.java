/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


@SuppressWarnings("nls")
public class ViewModelDataSourceTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewModelDataSourceTests.class);
    }

    private static IArchimateModel model;
    
    private ViewModelDataSource ds;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        ds = new ViewModelDataSource(model);
    }
    
    @Test
    public void testGetViewpointName() throws JRException {
        String name = ds.getViewpointName();
        assertNull(name);
        
        ds.next();
        assertEquals("Actor Co-operation viewpoint", ds.getViewpointName());
        
        ds.next();
        assertEquals("Application Behaviour viewpoint", ds.getViewpointName());

        ds.next();
        assertEquals("Application Co-operation viewpoint", ds.getViewpointName());
    }
    
    @Test
    public void testGetPropertiesDataSource() throws JRException {
        ds.next();
        assertNotNull(ds.getPropertiesDataSource());
    }
    
    @Test
    public void testGetChildElementsDataSource() throws JRException {
        ds.next();
        assertNotNull(ds.getChildElementsDataSource());
    }

    @Test
    public void testGetElement() {
        assertNull(ds.getElement());
    }

    @Test
    public void testNext() throws JRException {
        for(int i = 0; i < 17; i++) {
            assertTrue(ds.next());
        }
        assertFalse(ds.next());
    }
    
    @Test
    public void testGetFieldValue() throws JRException {
        ds.next();
        
        JRField field = mock(JRField.class);
        
        System.setProperty("JASPER_IMAGE_PATH", "/img");
        when(field.getName()).thenReturn("imagePath");
        assertEquals("/img/4165.png", ds.getFieldValue(field));
        
        when(field.getName()).thenReturn("viewpoint");
        assertEquals("Actor Co-operation viewpoint", ds.getFieldValue(field));
    }

    @Test
    public void testMoveFirst() throws JRException {
        assertNull(ds.getElement());
        
        ds.next();
        Object first = ds.getElement();
        assertNotNull(first);
        
        ds.next();
        Object next = ds.getElement();
        assertNotNull(next);
        
        ds.moveFirst();
        ds.next();
        assertEquals(first, ds.getElement());
    }
}
