/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;

import junit.framework.JUnit4TestAdapter;


public class UUIDFactoryTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UUIDFactoryTests.class);
    }
    
    
    @Test
    public void createID_Concept() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertEquals(36, id.length());
    }
    
    @Test
    public void createID_Folder() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createFolder());
        assertEquals(36, id.length());
    }

    @Test
    public void createID_Model() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createArchimateModel());
        assertEquals(36, id.length());
    }
} 
