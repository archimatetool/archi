/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.InternalEObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeature;

@SuppressWarnings("nls")
public class FeaturesEListTests {
    
    private FeaturesEList list;

    @BeforeEach
    public void runBeforeEachTest() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createArtifact();
        list = new FeaturesEList((InternalEObject)element, IArchimatePackage.ARCHIMATE_CONCEPT__FEATURES);
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
    public void putStringNullWithDefault() {
        IFeature feature = list.putString("name1", "value", "default");
        assertNotNull(feature);
        
        feature = list.putString("name1", null, "default");
        assertEquals("name1", list.get(0).getName());
        assertEquals("", list.get(0).getValue());
    }
    
    @Test
    public void putStringNullWithNullDefault() {
        IFeature feature = list.putString("name1", "value", null);
        assertNotNull(feature);
        
        feature = list.putString("name1", null, null);
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
    public void getFeature2() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        list.add(feature1);
        list.add(feature2);
        list.add(feature3);
        
        assertEquals(feature1, list.getFeature("name1"));
        assertEquals(feature2, list.getFeature("name2"));
        assertEquals(feature3, list.getFeature("name3"));
        
        IFeature feature = list.putString("name1", "value1");
        assertEquals(feature, list.getFeature("name1"));
        assertSame(feature, feature1);
    }

    @Test
    public void add() {
        IFeature feature = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        
        // Add the feature
        boolean result = list.add(feature);
        assertTrue(result);
        
        // Cannot add the first feature again
        result = list.add(feature);
        assertFalse(result);
        
        // Cannot add a different feature of the same name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name", "value2");
        result = list.add(feature2);
        assertFalse(result);

        // Cannot add the first feature again, even if name and value change
        feature.setName("name2");
        feature.setValue("value2");
        result = list.add(feature);
        assertFalse(result);

        // Can add another feature with a different name
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value3");
        result = list.add(feature3);
        assertTrue(result);
    }
    
    @Test
    public void addIndex() {
        IFeature feature = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        
        // Add the feature
        list.add(0, feature);
        assertEquals(0, list.indexOf(feature));
        
        // Cannot add the first feature again
        list.add(0, feature);
        assertEquals(1, list.size());
        
        // Cannot add a different feature of the same name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name", "value2");
        list.add(0, feature2);
        assertEquals(1, list.size());
        
        // Cannot add the first feature again, even if name and value change
        feature.setName("name2");
        feature.setValue("value2");
        list.add(0, feature);
        assertEquals(1, list.size());
        
        // Can add another feature with a different name
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value3");
        list.add(0, feature3);
        assertEquals(2, list.size());
        assertEquals(0, list.indexOf(feature3));
    }
    
    @Test
    public void addAll1() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add them again
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAll2() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        
        // Cannot add a list of non-unique names
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(1, list.size());
    }
    
    @Test
    public void addAll3() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add a list of already existing feature names
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1", "someValue");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2", "otherValue");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3", "otherValue");
        
        list.addAll(List.of(feature1a, feature2a, feature3a));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAll4() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add the same features but with different names
        
        feature1.setName("name1a");
        feature2.setName("name2a");
        feature3.setName("name3a");
        
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAll5() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Can add some new ones
        
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1a", "value");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2a", "value");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3a", "value");
        
        list.addAll(List.of(feature1a, feature2a, feature3a));
        assertEquals(6, list.size());
    }

    @Test
    public void addAllIndex1() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add them again
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }

    @Test
    public void addAllIndex2() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        
        // Cannot add a list of non-unique names
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(1, list.size());
    }

    @Test
    public void addAllIndex3() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add a list of already existing feature names
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1", "someValue");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2", "otherValue");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3", "otherValue");
        
        list.addAll(0, List.of(feature1a, feature2a, feature3a));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAllIndex4() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add the same features but with different names
        
        feature1.setName("name1a");
        feature2.setName("name2a");
        feature3.setName("name3a");
        
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAllIndex5() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAll(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Can add some new ones
        
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1a", "value");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2a", "value");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3a", "value");
        
        list.addAll(0, List.of(feature1a, feature2a, feature3a));
        assertEquals(6, list.size());
        assertEquals(0, list.indexOf(feature1a));
        assertEquals(1, list.indexOf(feature2a));
        assertEquals(2, list.indexOf(feature3a));
        assertEquals(3, list.indexOf(feature1));
        assertEquals(4, list.indexOf(feature2));
        assertEquals(5, list.indexOf(feature3));
    }

    @Test
    public void addUnique() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        
        // Add the feature
        list.addUnique(feature1);
        assertEquals(1, list.size());
        
        // Cannot add the first feature again
        list.addUnique(feature1);
        assertEquals(1, list.size());
        
        // Cannot add a different feature of the same name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        list.addUnique(feature2);
        assertEquals(1, list.size());

        // Cannot add the first feature again, even if name and value change
        feature1.setName("name2");
        feature1.setValue("value2");
        list.addUnique(feature1);
        assertEquals(1, list.size());

        // Can add another feature with a different name
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value3");
        list.addUnique(feature3);
        assertEquals(2, list.size());
    }
    
    @Test
    public void addUniqueIndex() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        
        // Add the feature
        list.addUnique(0, feature1);
        assertEquals(1, list.size());
        
        // Cannot add the first feature again
        list.addUnique(0, feature1);
        assertEquals(1, list.size());
        
        // Cannot add a different feature of the same name
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name", "value");
        list.addUnique(0, feature2);
        assertEquals(1, list.size());

        // Cannot add the first feature again, even if name and value change
        feature1.setName("name2");
        feature1.setValue("value2");
        list.addUnique(0, feature1);
        assertEquals(1, list.size());

        // Can add another feature with a different name
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value3");
        list.addUnique(0, feature3);
        assertEquals(2, list.size());
    }

    @Test
    public void addAllUniqueIndex1() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add them again
        list.addAllUnique(List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAllUniqueIndex2() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("same", "value");
        
        // Cannot add a list of non-unique names
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(1, list.size());
    }
    
    @Test
    public void addAllUniqueIndex3() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add a list of already existing feature names
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1", "someValue");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2", "otherValue");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3", "otherValue");
        
        list.addAllUnique(0, List.of(feature1a, feature2a, feature3a));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAllUniqueIndex4() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Cannot add the same features but with different names
        
        feature1.setName("name1a");
        feature2.setName("name2a");
        feature3.setName("name3a");
        
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
    }
    
    @Test
    public void addAllUniqueIndex5() {
        IFeature feature1 = IArchimateFactory.eINSTANCE.createFeature("name1", "value");
        IFeature feature2 = IArchimateFactory.eINSTANCE.createFeature("name2", "value");
        IFeature feature3 = IArchimateFactory.eINSTANCE.createFeature("name3", "value");
        
        // Can add all unique features
        list.addAllUnique(0, List.of(feature1, feature2, feature3));
        assertEquals(3, list.size());
        
        // Can add some new ones
        
        IFeature feature1a = IArchimateFactory.eINSTANCE.createFeature("name1a", "value");
        IFeature feature2a = IArchimateFactory.eINSTANCE.createFeature("name2a", "value");
        IFeature feature3a = IArchimateFactory.eINSTANCE.createFeature("name3a", "value");
        
        list.addAllUnique(0, List.of(feature1a, feature2a, feature3a));
        assertEquals(6, list.size());
        assertEquals(0, list.indexOf(feature1a));
        assertEquals(1, list.indexOf(feature2a));
        assertEquals(2, list.indexOf(feature3a));
        assertEquals(3, list.indexOf(feature1));
        assertEquals(4, list.indexOf(feature2));
        assertEquals(5, list.indexOf(feature3));
    }

}
