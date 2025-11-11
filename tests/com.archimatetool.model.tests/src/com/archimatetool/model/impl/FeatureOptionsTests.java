/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


@SuppressWarnings("nls")
public class FeatureOptionsTests {
    
    @Test
    public void setValue() {
        FeatureOptions options = new FeatureOptions() {};
        
        assertThrows(IllegalArgumentException.class, () -> options.getValue(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> options.getValue("key", null));

        assertThrows(IllegalArgumentException.class, () -> options.setValue(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> options.setValue("key", null));
        
        options.setValue("key", "value");
        assertEquals("value", options.getValue("key", ""));
        
        options.setValue("key", 1);
        assertEquals("string", options.getValue("key", "string"));
        assertEquals(1, options.getValue("key", 2));
        
        options.setValue("key", true);
        assertEquals("string", options.getValue("key", "string"));
        assertEquals(true, options.getValue("key", false));
    }
    
    @Test
    public void parseInteger() {
        FeatureOptions options = new FeatureOptions() {};
        
        assertEquals(10, options.parseInteger("something=else,key=10,something=else", "key", 1));
        assertEquals(10, options.parseInteger("something=else,key=10,something=else", "key", 1));
        assertEquals(-10, options.parseInteger("something=else,key=-10,something=else", "key", 10));
        assertEquals(-20, options.parseInteger("something=else,key=-20,something=else", "key", 1));
        
        assertEquals(1, options.parseInteger("something=else,bogus=10,something=else", "key", 1));
    }
    
    @Test
    public void parseBoolean() {
        FeatureOptions options = new FeatureOptions() {};
        
        assertTrue(options.parseBoolean("something=else,key=true,something=else", "key", false));
        assertFalse(options.parseBoolean("something=else,key=false,something=else", "key", true));
        
        assertTrue(options.parseBoolean("something=else,key=bogus,something=else", "key", true));
        assertFalse(options.parseBoolean("something=else,key=bogus,something=else", "key", false));
        
        assertTrue(options.parseBoolean("something=else,bogus=true,something=else", "key", true));
        assertFalse(options.parseBoolean("something=else,bogus=false,something=else", "key", false));
    }
    
    @Test
    public void parseString() {
        FeatureOptions options = new FeatureOptions() {};
        
        assertEquals("value", options.parseString("something=else,key=value,something=else", "key", ""));
        assertEquals("alt", options.parseString("something=else,bogus=value,something=else", "key", "alt"));
    }
    
    @Test
    public void toFeatureString() {
        FeatureOptions options = new FeatureOptions() {};
        assertNull(options.toFeatureString());
        
        options = new FeatureOptions() {
            @Override
            public String toFeatureString() {
                return "key=" + getValue("key", "");
            }
        };
        
        options.setValue("key", "value");
        assertEquals("key=value", options.toFeatureString());
    }
}
