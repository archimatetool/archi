/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;



/**
 * Tests common to many classes
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class CommonTests {
    
    public static void testGetID(IIdentifier id) {
        assertNotNull(id.getId());
        
        id.setId(null);
        assertNull(id.getId());

        id.setId("id-012356");
        assertEquals("id-012356", id.getId());
    }
    
    public static void testGetName(INameable nameable) {
        assertEquals("", nameable.getName());
        nameable.setName("name");
        assertEquals("name", nameable.getName());
    }

    public static void testGetDocumentation(IDocumentable documentable) {
        assertEquals("", documentable.getDocumentation());
        documentable.setDocumentation("doc");
        assertEquals("doc", documentable.getDocumentation());
    }

    public static void testProperties(IProperties properties) {
        testList(properties.getProperties(), IArchimatePackage.eINSTANCE.getProperty());
        
        String key = "some_key", value = "some_value";
        
        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey(key);
        property.setValue(value);
        properties.getProperties().add(property);

        // Check entry is correct
        EList<IProperty> entries = properties.getProperties();
        assertEquals(1, entries.size());
        IProperty testProperty = entries.get(0);
        assertEquals(property, testProperty);
        assertEquals(testProperty.getKey(), key);
        assertEquals(testProperty.getValue(), value);
    }
  
    @SuppressWarnings("unchecked")
    public static <T extends EObject> void testList(EList<T> list, EClass type) {
        assertTrue(list.isEmpty());
        list.add((T)IArchimateFactory.eINSTANCE.create(type));
        assertEquals(1, list.size());
        list.add((T)IArchimateFactory.eINSTANCE.create(type));
        assertEquals(2, list.size());
        list.clear();
        assertTrue(list.isEmpty());
    }
    
    public static void testGetAdapter(IAdapter adapter) {
        String test1 = "test1";
        String test2 = "test2";
        
        // Null key not allowed
        adapter.setAdapter(null, test1);
        assertNull(adapter.getAdapter(null));
        
        // Null value allowed but adapterMap will be null
        adapter.setAdapter("key", null);
        assertNull(adapter.getAdapter("key"));
        
        // Null value because not set
        assertNull(adapter.getAdapter(String.class));
        
        // Set
        adapter.setAdapter(String.class, test1);
        assertSame(test1, adapter.getAdapter(String.class));
        
        // Set new value
        adapter.setAdapter(String.class, test2);
        assertSame(test2, adapter.getAdapter(String.class));
        
        // Set to null
        adapter.setAdapter(String.class, null);
        assertNull(adapter.getAdapter(String.class));
    }
}
