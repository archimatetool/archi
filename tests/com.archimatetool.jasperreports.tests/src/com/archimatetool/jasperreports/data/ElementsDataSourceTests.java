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

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


@SuppressWarnings("nls")
public class ElementsDataSourceTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ElementsDataSourceTests.class);
    }

    private static IArchimateModel model;
    
    private ElementsDataSource ds;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
    }
    
    @Test
    public void constructor_DiagramModel() {
        ds = new ElementsDataSource(model.getDiagramModels().get(1));
        assertEquals(30, ds.size());
    }
    
    @Test
    public void getPropertiesDataSourceNotNull() throws JRException {
        ds = new ElementsDataSource(model, "BusinessActor");
        ds.next();
        assertNotNull(ds.getPropertiesDataSource());
    }

    @Test
    public void getElementFirstNull() {
        ds = new ElementsDataSource(model, "BusinessActor");
        assertNull(ds.getElement());
    }

    @Test
    public void next() throws JRException {
        ds = new ElementsDataSource(model, "BusinessActor");
        
        for(int i = 0; i < 17; i++) {
            assertTrue(ds.next());
        }
        assertFalse(ds.next());
    }
    
    @Test
    public void getFieldValue() throws JRException {
        ds = new ElementsDataSource(model, "BusinessActor");
        
        ds.next();
        
        JRField field = mock(JRField.class);
        
        when(field.getName()).thenReturn("this");
        Object object = ds.getFieldValue(field);
        assertTrue(object instanceof IBusinessActor);
        assertEquals("275", ((IBusinessActor)object).getId());
        
        when(field.getName()).thenReturn("name");
        assertEquals(((IBusinessActor)object).getName(), ds.getFieldValue(field));
    }

    @Test
    public void size() {
        ds = new ElementsDataSource(model, "elements");
        assertEquals(120, ds.size());

        ds = new ElementsDataSource(model, "relations");
        assertEquals(178, ds.size());
    }
    
    @Test
    public void moveFirst() throws JRException {
        ds = new ElementsDataSource(model, "BusinessActor");
        
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
        ds = new ElementsDataSource(model, "BusinessActor");
        
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
