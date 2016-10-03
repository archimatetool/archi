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
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


public class UnusedRelationsCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UnusedRelationsCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        
        List<IArchimateRelationship> relations = new ArrayList<IArchimateRelationship>();
        
        IArchimateRelationship relation = (IArchimateRelationship)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getAssociationRelationship());
        relations.add(relation);
        
        IArchimateElement e1 = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        IArchimateElement e2 = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        relation.connect(e1, e2);
        
        UnusedRelationsChecker checker = new UnusedRelationsChecker(relations);
        
        // Should not be OK
        List<IIssue> issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(relation, issues.get(0).getObject());
        
        // Add it to a diagram
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateElement(e1);
        model.getDefaultDiagramModel().getChildren().add(dmo1);
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateElement(e2);
        model.getDefaultDiagramModel().getChildren().add(dmo2);
        
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setArchimateRelationship(relation);
        conn.connect(dmo1, dmo2);
        
        // Should be OK
        issues = checker.getIssues();
        assertEquals(0, issues.size());
    }
    
    
}
