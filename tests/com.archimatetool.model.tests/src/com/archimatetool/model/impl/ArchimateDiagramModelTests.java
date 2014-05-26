/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;


public class ArchimateDiagramModelTests extends DiagramModelTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateDiagramModelTests.class);
    }
    
    private IArchimateDiagramModel adm;
    
    @Override
    protected IDiagramModel getDiagramModel() {
        adm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        return adm;
    }
    
    
    @Test
    public void testGetViewpoint() {
        assertEquals(0, adm.getViewpoint());
        adm.setViewpoint(1);
        assertEquals(1, adm.getViewpoint());
    }

    @Test
    public void testGetChildren() {
        CommonTests.testList(adm.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelArchimateObject());
        CommonTests.testList(adm.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        CommonTests.testList(adm.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelNote());
    }

}
