/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.jasperreports.TestSupport;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import net.sf.jasperreports.engine.JRException;


@SuppressWarnings("nls")
public class ViewChildrenDataSourceTests {
    
    private static IDiagramModel dm;
    
    private ViewChildrenDataSource ds;
    
    @BeforeAll
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        tm.loadModel();
        dm = (IDiagramModel)tm.getObjectByID("4056"); // Layered View
    }
    
    @BeforeEach
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
        
        Object object = ds.getFieldValue(TestSupport.mockJRField("this"));
        assertEquals("572", ((IArchimateElement)object).getId());
        
        assertEquals(((IArchimateElement)object).getName(), ds.getFieldValue(TestSupport.mockJRField("name")));
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
