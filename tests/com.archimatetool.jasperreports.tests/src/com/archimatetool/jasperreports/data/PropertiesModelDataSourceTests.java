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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IProperty;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


@SuppressWarnings("nls")
public class PropertiesModelDataSourceTests {
    
    private PropertiesModelDataSource ds;
    private IArchimateModel model;
    
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey("key1");
        property.setValue("value1");
        model.getProperties().add(property);
        
        property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey("key2");
        property.setValue("value2");
        model.getProperties().add(property);
        
        ds = new PropertiesModelDataSource(model);
    }
    
    @Test
    public void getElementNull() {
        assertNull(ds.getElement());
    }

    @Test
    public void next() throws JRException {
        for(int i = 0; i < 2; i++) {
            assertTrue(ds.next());
        }
        assertFalse(ds.next());
    }
    
    @Test
    public void size() {
        assertEquals(2, ds.size());
    }
    
    @Test
    public void getFieldValue() throws JRException {
        ds.next();
        
        JRField field = mock(JRField.class);
        
        when(field.getName()).thenReturn("key");
        Object object = ds.getFieldValue(field);
        assertEquals("key1", object);
        
        when(field.getName()).thenReturn("value");
        object = ds.getFieldValue(field);
        assertEquals("value1", object);
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
}
