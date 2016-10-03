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
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


public class UnusedElementsCheckerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UnusedElementsCheckerTests.class);
    }
    
    @Test
    public void testGetIssues() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        
        IArchimateElement element = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        elements.add(element);
        
        UnusedElementsChecker checker = new UnusedElementsChecker(elements);
        
        // Should not be OK
        List<IIssue> issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(element, issues.get(0).getObject());
        
        // Add it to a diagram
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        model.getDefaultDiagramModel().getChildren().add(dmo);
        
        // Should be OK
        issues = checker.getIssues();
        assertEquals(0, issues.size());
    }
    
    
}
