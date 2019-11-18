/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.InternalEObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeaturesEList;

import junit.framework.JUnit4TestAdapter;

@SuppressWarnings("nls")
public class FeaturesEListTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FeaturesEListTests.class);
    }
    
    private IFeaturesEList list;

    @Before
    public void runBeforeEachTest() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        list = new FeaturesEList(IFeature.class, (InternalEObject)element, IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES);
    }

    @Test
    public void putString() {
        list.putString("name1", "value1");
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
    }
    
    @Test
    public void putStringValueChanged() {
        list.putString("name1", "value1");
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
        
        list.putString("name1", "value2");
        assertEquals("name1", list.get(0).getName());
        assertEquals("value2", list.get(0).getValue());
    }

    @Test
    public void putStringDefaultValue() {
        list.putString("name1", "value", "default");
        assertEquals("name1", list.get(0).getName());
        assertEquals("value", list.get(0).getValue());

        list.putString("name1", "default", "default");
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putInt() {
        list.putInt("name1", 1);
        assertEquals("name1", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
    }
    
    @Test
    public void putIntDefaultValue() {
        list.putInt("name1", 1, 0);
        assertEquals("name1", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());

        list.putInt("name1", 1, 1);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putBoolean() {
        list.putBoolean("name1", true);
        assertEquals("name1", list.get(0).getName());
        assertEquals("true", list.get(0).getValue());
        
        list.putBoolean("name1", false);
        assertEquals("false", list.get(0).getValue());
    }
    
    @Test
    public void putBooleanDefaultValue() {
        list.putBoolean("name1", true, false);
        assertEquals("name1", list.get(0).getName());
        assertEquals("true", list.get(0).getValue());

        list.putBoolean("name1", true, true);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void getString() {
        list.putString("name1", "value1");
        assertEquals("value1", list.getString("name1", ""));
    }

    @Test
    public void getStringDefault() {
        assertEquals("value1", list.getString("name1", "value1"));
    }
    
    @Test
    public void getInt() {
        list.putInt("name1", 1);
        assertEquals(1, list.getInt("name1", 0));
    }

    @Test
    public void getIntDefault() {
        assertEquals(2, list.getInt("name1", 2));
    }

    @Test
    public void getBoolean() {
        list.putBoolean("name1", true);
        assertEquals(true, list.getBoolean("name1", false));
    }

    @Test
    public void getBooleanDefault() {
        assertEquals(true, list.getBoolean("name1", true));
        assertEquals(false, list.getBoolean("name1", false));
    }
    
    @Test
    public void remove() {
        list.putString("name1", "value1");
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
        
        list.remove("name1");
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void has() {
        assertFalse(list.has("name1"));
        
        list.putString("name1", "value1");
        assertTrue(list.has("name1"));
    }
    
    @Test
    public void getFeature() {
        list.putString("name1", "value1");
        assertNotNull(list.getFeature("name1"));
        
        assertNull(list.getFeature("bogus"));
    }
}
