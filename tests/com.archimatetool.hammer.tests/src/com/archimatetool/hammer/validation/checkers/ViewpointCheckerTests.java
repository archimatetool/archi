/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;


@SuppressWarnings("nls")
public class ViewpointCheckerTests {
    
    @Test
    public void testGetIssues() {
        List<IArchimateDiagramModel> views = new ArrayList<IArchimateDiagramModel>();
        
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setName("view");
        views.add(dm);
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateElement(IArchimateFactory.eINSTANCE.createNode());
        dm.getChildren().add(dmo1);
        
        ViewpointChecker checker = new ViewpointChecker(views);
        
        // Should be ok
        List<IIssue> issues = checker.getIssues();
        assertEquals(0, issues.size());
        
        // Set viewpoint
        dm.setViewpoint("organization");
        
        issues = checker.getIssues();
        assertEquals(1, issues.size());
        assertSame(dmo1, issues.get(0).getObject());
    }
    
}
