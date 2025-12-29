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
import org.junit.jupiter.api.Test;

import com.archimatetool.jasperreports.TestSupport;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import net.sf.jasperreports.engine.JRException;


@SuppressWarnings("nls")
public class ElementsDataSourceTests {
    
    private static ArchimateTestModel tm;
    private static IArchimateModel model;
    
    @BeforeAll
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
    }
    
    @Test
    public void constructor_DiagramModel() {
        ElementsDataSource ds = new ElementsDataSource((IDiagramModel)tm.getObjectByID("4056")); // Layered View
        assertEquals(30, ds.size());
    }
    
    @Test
    public void getPropertiesDataSourceNotNull() throws JRException {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        ds.next();
        assertNotNull(ds.getPropertiesDataSource());
    }

    @Test
    public void getElementFirstNull() {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        assertNull(ds.getElement());
    }

    @Test
    public void next() throws JRException {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        
        for(int i = 0; i < 17; i++) {
            assertTrue(ds.next());
        }
        assertFalse(ds.next());
    }
    
    @Test
    public void getFieldValue() throws JRException {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        
        ds.next();
        
        Object object = ds.getFieldValue(TestSupport.mockJRField("this"));
        assertTrue(object instanceof IBusinessActor);
        assertEquals("275", ((IBusinessActor)object).getId());
        
        assertEquals(((IBusinessActor)object).getName(), ds.getFieldValue(TestSupport.mockJRField("name")));
    }

    @Test
    public void size() {
        ElementsDataSource ds = new ElementsDataSource(model, "elements");
        assertEquals(120, ds.size());

        ds = new ElementsDataSource(model, "relations");
        assertEquals(176, ds.size());
    }
    
    @Test
    public void moveFirst() throws JRException {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        
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
    public void getReferencedViews() throws JRException {
        ElementsDataSource ds = new ElementsDataSource(model, "BusinessActor");
        
        assertNull(ds.getReferencedViews());
        
        ds.next();

        ViewModelDataSource vmds = ds.getReferencedViews();
        assertNull(vmds.getElement());

        vmds.next();
        assertEquals("4165", vmds.getElement().getId());

        vmds.next();
        assertEquals("4056", vmds.getElement().getId());

        vmds.next();
        assertEquals("3698", vmds.getElement().getId());
    }

}
