/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.InternalEObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeaturesEMap;

import junit.framework.JUnit4TestAdapter;

@SuppressWarnings("nls")
public class FeaturesEMapTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FeaturesEMapTests.class);
    }
    
    private IFeaturesEMap map;

    @Before
    public void runBeforeEachTest() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        map = new FeaturesEMap((InternalEObject)element, IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES);
    }

    @Test
    public void putString() {
        map.putString("name1", "value1");
        assertEquals("value1", map.get("name1"));
    }
    
    @Test
    public void putStringValueChanged() {
        map.putString("name1", "value1");
        assertEquals("value1", map.get("name1"));
        
        map.putString("name1", "value2");
        assertEquals("value2", map.get("name1"));
    }

    @Test
    public void putStringDefaultValue() {
        map.putString("name1", "value", "default");
        assertEquals("value", map.get("name1"));

        map.putString("name1", "default", "default");
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void putInt() {
        map.putInt("name1", 1);
        assertEquals("1", map.get("name1"));
    }
    
    @Test
    public void putIntDefaultValue() {
        map.putInt("name1", 1, 0);
        assertEquals("1", map.get("name1"));

        map.putInt("name1", 1, 1);
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void putBoolean() {
        map.putBoolean("name1", true);
        assertEquals("true", map.get("name1"));
        
        map.putBoolean("name1", false);
        assertEquals("false", map.get("name1"));
    }
    
    @Test
    public void putBooleanDefaultValue() {
        map.putBoolean("name1", true, false);
        assertEquals("true", map.get("name1"));

        map.putBoolean("name1", true, true);
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void getString() {
        map.putString("name1", "value1");
        assertEquals("value1", map.getString("name1", ""));
    }

    @Test
    public void getStringDefault() {
        assertEquals("value1", map.getString("name1", "value1"));
    }
    
    @Test
    public void getInt() {
        map.putInt("name1", 1);
        assertEquals(1, map.getInt("name1", 0));
    }

    @Test
    public void getIntDefault() {
        assertEquals(2, map.getInt("name1", 2));
    }

    @Test
    public void getBoolean() {
        map.putBoolean("name1", true);
        assertEquals(true, map.getBoolean("name1", false));
    }

    @Test
    public void getBooleanDefault() {
        assertEquals(true, map.getBoolean("name1", true));
        assertEquals(false, map.getBoolean("name1", false));
    }
    
    @Test
    public void remove() {
        map.putString("name1", "value1");
        assertEquals("value1", map.get("name1"));
        
        map.remove("name1");
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void has() {
        assertFalse(map.has("name1"));
        
        map.putString("name1", "value1");
        assertTrue(map.has("name1"));
    }
}
