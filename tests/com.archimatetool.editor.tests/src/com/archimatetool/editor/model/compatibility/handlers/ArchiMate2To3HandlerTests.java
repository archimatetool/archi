/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class ArchiMate2To3HandlerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchiMate2To3HandlerTests.class);
    }
    
    private static Archimate2To3Handler handler;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        handler = new Archimate2To3Handler();
    }
    
    
    @Test
    public void testIsArchimate2Model() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        String[] oldVersions = { "1.1.0", "2.0.0", "2.1.0", "2.3.0", "3.0.0", "3.1.0", "3.2.0", "3.2.1", "3.3.0", "3.3.1", "3.3.2"};
        
        for(String version : oldVersions) {
            model.setVersion(version);
            assertTrue(handler.isArchimate2Model(model));
        }
        
        String[] newVersions = { "4.0.0", "4.0.1", "4.1.0", "4.1.1", "4.2.0", "5.0.0"};
        
        for(String version : newVersions) {
            model.setVersion(version);
            assertFalse(handler.isArchimate2Model(model));
        }
    }
}
