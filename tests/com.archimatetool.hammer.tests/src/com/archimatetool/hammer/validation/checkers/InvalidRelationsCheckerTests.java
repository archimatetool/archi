/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.archimatetool.hammer.validation.issues.ErrorType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class InvalidRelationsCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(InvalidRelationsCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        List<IArchimateRelationship> relations = new ArrayList<IArchimateRelationship>();
        
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setName("relation");
        relation.setSource(IArchimateFactory.eINSTANCE.createBusinessActor());
        relation.setTarget(IArchimateFactory.eINSTANCE.createBusinessRole());
        relations.add(relation);
        
        InvalidRelationsChecker checker = new InvalidRelationsChecker(relations);
        
        // Should be ok
        List<IIssue> issues = checker.getIssues();
        assertEquals(0, issues.size());
        
        // Now set bogus relationship
        relation.setTarget(IArchimateFactory.eINSTANCE.createNode());
        issues = checker.getIssues();
        
        assertEquals(1, issues.size());
        assertTrue(issues.get(0) instanceof ErrorType);
        assertSame(relation, issues.get(0).getObject());
    }
    
    
}
