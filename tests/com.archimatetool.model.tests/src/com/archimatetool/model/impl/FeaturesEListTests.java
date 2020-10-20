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
import static org.junit.Assert.assertSame;
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
        IFeature feature = list.putString("name1", "value1");
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
    }
    
    @Test
    public void putStringValueChanged() {
        IFeature feature = list.putString("name1", "value1");
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
        
        IFeature feature2 = list.putString("name1", "value2");
        assertSame(feature, feature2);
        assertEquals("name1", list.get(0).getName());
        assertEquals("value2", list.get(0).getValue());
    }

    @Test
    public void putStringDefaultValue() {
        IFeature feature = list.putString("name1", "value", "default");
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("value", list.get(0).getValue());

        feature = list.putString("name1", "default", "default");
        assertNull(feature);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putStringWithDefaultValueShouldNotAddFeature() {
        IFeature feature = list.putString("name1", "default", "default");
        assertNull(feature);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putInt() {
        IFeature feature = list.putInt("name1", 1);
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());
    }
    
    @Test
    public void putIntDefaultValue() {
        IFeature feature = list.putInt("name1", 1, 0);
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("1", list.get(0).getValue());

        feature = list.putInt("name1", 1, 1);
        assertNull(feature);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putIntWithDefaultValueShouldNotAddFeature() {
        IFeature feature = list.putInt("name1", 1, 1);
        assertNull(feature);
        assertTrue(list.isEmpty());
    }

    @Test
    public void putBoolean() {
        IFeature feature = list.putBoolean("name1", true);
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("true", list.get(0).getValue());
        
        IFeature feature2 = list.putBoolean("name1", false);
        assertSame(feature, feature2);
        assertEquals("false", list.get(0).getValue());
    }
    
    @Test
    public void putBooleanDefaultValue() {
        IFeature feature = list.putBoolean("name1", true, false);
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("true", list.get(0).getValue());

        feature = list.putBoolean("name1", true, true);
        assertNull(feature);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void putBooleanWithDefaultValueShouldNotAddFeature() {
        IFeature feature = list.putBoolean("name1", true, true);
        assertNull(feature);
        assertTrue(list.isEmpty());
    }

    @Test
    public void getString() {
        IFeature feature = list.putString("name1", "value1");
        assertNotNull(feature);
        assertEquals("value1", list.getString("name1", ""));
    }

    @Test
    public void getStringDefault() {
        assertEquals("value1", list.getString("name1", "value1"));
    }
    
    @Test
    public void getInt() {
        IFeature feature = list.putInt("name1", 1);
        assertNotNull(feature);
        assertEquals(1, list.getInt("name1", 0));
    }

    @Test
    public void getIntDefault() {
        assertEquals(2, list.getInt("name1", 2));
    }

    @Test
    public void getBoolean() {
        IFeature feature = list.putBoolean("name1", true);
        assertNotNull(feature);
        assertEquals(true, list.getBoolean("name1", false));
    }

    @Test
    public void getBooleanDefault() {
        assertEquals(true, list.getBoolean("name1", true));
        assertEquals(false, list.getBoolean("name1", false));
    }
    
    @Test
    public void remove() {
        IFeature feature = list.putString("name1", "value1");
        assertNotNull(feature);
        assertEquals("name1", list.get(0).getName());
        assertEquals("value1", list.get(0).getValue());
        
        boolean result = list.remove("name1");
        assertTrue(result);
        assertTrue(list.isEmpty());
        
        result = list.remove("bogus");
        assertFalse(result);
    }
    
    @Test
    public void has() {
        assertFalse(list.has("name1"));
        
        list.putString("name1", "value1");
        assertTrue(list.has("name1"));
    }
    
    @Test
    public void getFeature() {
        IFeature feature = list.putString("name1", "value1");
        assertNotNull(feature);
        assertNotNull(list.getFeature("name1"));
        
        assertNull(list.getFeature("bogus"));
    }
    
    @Test
    public void noDuplicateFeature1() {
        IFeature feature = IArchimateFactory.eINSTANCE.createFeature();
        feature.setName("uniqueName");
        feature.setValue("someValue");
        
        // Add the feature
        boolean result = list.add(feature);
        assertTrue(result);
        
        // Should not be able to add the first feature again
        result = list.add(feature);
        assertFalse(result);

        // Should not be able to add the first feature again, even if name and value change
        feature.setName("uniqueName2");
        feature.setValue("someValue2");
        result = list.add(feature);
        assertFalse(result);
    }
    
    @Test
    public void noDuplicateFeature2() {
        IFeature feature = IArchimateFactory.eINSTANCE.createFeature();
        feature.setName("uniqueName");
        feature.setValue("someValue");
        
        // Add the feature
        boolean result = list.add(feature);
        assertTrue(result);
        
        // Should not be able to add a different feature object with the same name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature();
        feature2.setName("uniqueName");
        feature2.setValue("someValue2");
        result = list.add(feature2);
        assertFalse(result);
    }

    @Test
    public void noDuplicateFeature3() {
        IFeature feature = IArchimateFactory.eINSTANCE.createFeature();
        feature.setName("uniqueName");
        feature.setValue("someValue");
        
        // Add the feature
        boolean result = list.add(feature);
        assertTrue(result);
        
        // Should be able to add a different feature object with a different name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature();
        feature2.setName("uniqueName2");
        feature2.setValue("someValue2");
        result = list.add(feature2);
        assertTrue(result);
    }
    
    @Test
    public void noDuplicateFeature4() {
        // Create another list
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        FeaturesEList list2 = new FeaturesEList(IFeature.class, (InternalEObject)element, IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES);

        // Add Feature to first list
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature();
        feature1.setName("uniqueName");
        feature1.setValue("someValue");
        list.add(feature1);
        
        // Add feature with same name to second list
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature();
        feature2.setName("uniqueName");
        feature2.setValue("someValue");
        list2.add(feature2);

        // Should not be able to add first list to second list
        boolean result = list2.addAll(list);
        assertFalse(result);
        
        // remove it
        list2.remove(feature2);
        
        // Should be able to add it now
        result = list2.addAll(list);
        assertTrue(result);
    }

}
