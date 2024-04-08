/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IProperty;


@SuppressWarnings("nls")
public class PropertyTests {
    
    private IProperty property;
    
    @BeforeEach
    public void runBeforeEachTest() {
        property = IArchimateFactory.eINSTANCE.createProperty();
    }
    
    @Test
    public void testDefaultValues() {
        assertNotNull(property);
        assertEquals("", property.getKey());
        assertEquals("", property.getValue());
    }
    
    @Test
    public void testSetValues() {
        assertNotNull(property);
        
        property.setKey("key");
        property.setValue("value");
        
        assertEquals("key", property.getKey());
        assertEquals("value", property.getValue());
    }

}
