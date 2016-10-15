/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;

import junit.framework.JUnit4TestAdapter;


public class JunctionsCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JunctionsCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        elements.add(e1);
        
        IArchimateElement junction = IArchimateFactory.eINSTANCE.createJunction();
        elements.add(junction);
        
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        elements.add(e2);
        
        IArchimateRelationship rel1 = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        rel1.connect(e1, junction);
        
        IArchimateRelationship rel2 = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        rel2.connect(junction, e2);
        
        JunctionsChecker checker = new JunctionsChecker(elements);
        
        // Should be OK
        List<IIssue> issues = checker.getIssues();
        assertEquals(0, issues.size());
        
        // Add a different relationship
        IArchimateRelationship rel3 = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        rel3.connect(junction, e2);
        
        issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(junction, issues.get(0).getObject());
    }
    
    
}
