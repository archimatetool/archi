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
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class NestedElementsCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NestedElementsCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        List<IArchimateDiagramModel> views = new ArrayList<IArchimateDiagramModel>();
        
        // Create a View
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName("view");
        views.add(dm);
        
        // Create a parent object
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateElement(IArchimateFactory.eINSTANCE.createGrouping());
        dm.getChildren().add(dmo1);
        
        // Add a child object with no connection
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateElement(IArchimateFactory.eINSTANCE.createLocation());
        dmo1.getChildren().add(dmo2);
        
        NestedElementsChecker checker = new NestedElementsChecker(views);
        
        // Should not be ok as we have no relations
        List<IIssue> issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(dmo2, issues.get(0).getObject());
        
        // Add an Association relationship
        //IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IArchimateRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.connect(dmo1.getArchimateElement(), dmo2.getArchimateElement());
        //conn.setArchimateRelationship(relation1);
        //conn.connect(dmo1, dmo2);
        
        // Should not be ok
        issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(dmo2, issues.get(0).getObject());
        
        // Add a valid relationship
        ICompositionRelationship relation2 = IArchimateFactory.eINSTANCE.createCompositionRelationship();
        relation2.connect(dmo1.getArchimateElement(), dmo2.getArchimateElement());
        //conn.setArchimateRelationship(relation2);
        //conn.connect(dmo1, dmo2);
        
        // Should be OK because we have a valid relationship
        issues = checker.getIssues();
        assertEquals(0, issues.size());
        
        // Remove the invalid relationship
        relation1.disconnect();
        
        // And now should be OK
        issues = checker.getIssues();
        assertEquals(0, issues.size());
    }
    
}
