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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


@SuppressWarnings("nls")
public class ViewChildrenDataSourceTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewChildrenDataSourceTests.class);
    }

    private static IArchimateModel model;
    private static IDiagramModel dm;
    
    private ViewChildrenDataSource ds;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
        dm = model.getDiagramModels().get(1);
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        ds = new ViewChildrenDataSource(dm);
    }
    
    @Test
    public void getPropertiesDataSourceNotNull() throws JRException {
        ds.next();
        assertNotNull(ds.getPropertiesDataSource());
    }

    @Test
    public void getElementNull() {
        assertNull(ds.getElement());
    }

    @Test
    public void next() throws JRException {
        for(int i = 0; i < 30; i++) {
            assertTrue(ds.next());
        }
        assertFalse(ds.next());
    }
    
    @Test
    public void getFieldValue() throws JRException {
        ds.next();
        
        JRField field = mock(JRField.class);
        
        when(field.getName()).thenReturn("this");
        Object object = ds.getFieldValue(field);
        assertEquals("572", ((IArchimateElement)object).getId());
        
        when(field.getName()).thenReturn("name");
        assertEquals(((IArchimateElement)object).getName(), ds.getFieldValue(field));
    }

    @Test
    public void moveFirst() throws JRException {
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
    
    @Test
    public void size() {
        ds = new ViewChildrenDataSource(dm, "elements");
        assertEquals(30, ds.size());

        ds = new ViewChildrenDataSource(dm, "relations");
        assertEquals(28, ds.size());

        ds = new ViewChildrenDataSource(dm, "elements|relations");
        assertEquals(58, ds.size());
    }
    
    @Test
    public void typesAreCorrect() throws JRException {
        ds = new ViewChildrenDataSource(dm, "elements");
        for(int i = 0; i < 30; i++) {
            assertTrue(ds.next());
        }

        ds = new ViewChildrenDataSource(dm, "relations");
        for(int i = 0; i < 28; i++) {
            assertTrue(ds.next());
        }

        ds = new ViewChildrenDataSource(dm, "relations|elements");
        for(int i = 0; i < 58; i++) {
            assertTrue(ds.next());
        }

        ds = new ViewChildrenDataSource(dm, "business");
        for(int i = 0; i < 15; i++) {
            assertTrue(ds.next());
        }
        
        ds = new ViewChildrenDataSource(dm, "BusinessActor");
        for(int i = 0; i < 2; i++) {
            assertTrue(ds.next());
        }
        
        ds = new ViewChildrenDataSource(dm, "BusinessService");
        for(int i = 0; i < 6; i++) {
            assertTrue(ds.next());
        }

        ds = new ViewChildrenDataSource(dm, "BusinessActor|BusinessService");
        for(int i = 0; i < 8; i++) {
            assertTrue(ds.next());
        }
    }
}
