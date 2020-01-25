/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.hammer.validation.issues.IIssueCategory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;


public class ValidatorTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ValidatorTests.class);
    }
    
    static IArchimateModel model;
    static Validator validator;
    

    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModel();
        validator = new Validator(model);
    }
    
    @Test
    public void testValidatorHasModel() {
        assertSame(model, validator.getModel());
    }
    
    @Test
    public void testValidate() {
        List<Object> list = validator.validate();
        
        assertFalse(list.isEmpty());
        
        for(Object object : list) {
            assertTrue(object instanceof IIssueCategory);
        }
    }
    
    @Test
    public void testGetArchimateElements() {
        assertEquals(120, validator.getArchimateElements().size());
    }

    @Test
    public void testGetArchimateRelationships() {
        assertEquals(178, validator.getArchimateRelationships().size());
    }
    
    @Test
    public void testGetArchimateViews() {
        assertEquals(17, validator.getArchimateViews().size());
    }
}
