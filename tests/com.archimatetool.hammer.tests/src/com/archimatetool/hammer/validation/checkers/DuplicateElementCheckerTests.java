/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class DuplicateElementCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DuplicateElementCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        e1.setName("fido1");
        elements.add(e1);
        
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        e2.setName("fido2");
        elements.add(e2);
        
        DuplicateElementChecker checker = new DuplicateElementChecker(elements);
        
        // Should be OK
        List<IIssue> issues = checker.getIssues();
        assertTrue(issues.isEmpty());
        
        // Set name the same
        e2.setName("fido1");
        issues = checker.getIssues();
        assertEquals(2, issues.size());
        assertTrue(issues.get(0) instanceof WarningType);
        assertTrue(issues.get(1) instanceof WarningType);
        assertSame(e1, issues.get(0).getObject());
        assertSame(e2, issues.get(1).getObject());
    }
    
    @Test
    public void testJunctionsAllowedSameNames() {
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createJunction();
        elements.add(e1);
        
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createJunction();
        elements.add(e2);
        
        DuplicateElementChecker checker = new DuplicateElementChecker(elements);
        
        // Should be OK
        List<IIssue> issues = checker.getIssues();
        assertTrue(issues.isEmpty());
        
        // Set name the same
        e1.setName("And");
        e2.setName("And");
        issues = checker.getIssues();
        assertTrue(issues.isEmpty());
    }

}
