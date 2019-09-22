/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;


@SuppressWarnings("nls")
public abstract class DiagramModelTests {
    
    private IArchimateModel model;
    protected IDiagramModel dm;
    
    @Before
    public void runBeforeEachDiagramModelTest() {
        dm = getDiagramModel();
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
    }

    protected abstract IDiagramModel getDiagramModel();
    
    @Test
    public void testGetName() {
        CommonTests.testGetName(dm);
    }

    @Test
    public void testGetDiagramModel() {
        assertSame(dm, dm.getDiagramModel());
    }
    
    @Test
    public void testGetArchimateModel() {
        assertNull(dm.getArchimateModel());
        
        model.getFolder(FolderType.DIAGRAMS).getElements().add(dm);
        assertSame(model, dm.getArchimateModel());
    }

    @Test
    public void testGetID() {
        assertNotNull(dm.getId());
    }

    @Test
    public void testGetConnectionRouterType() {
        assertEquals(IDiagramModel.CONNECTION_ROUTER_BENDPOINT, dm.getConnectionRouterType());
        dm.setConnectionRouterType(IDiagramModel.CONNECTION_ROUTER_MANHATTAN);
        assertEquals(IDiagramModel.CONNECTION_ROUTER_MANHATTAN, dm.getConnectionRouterType());
    }
    
    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(dm);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(dm);
    }

    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(dm);
    }

    @Test
    public void testGetCopy() {
        dm.setName("name");
        dm.setDocumentation("doc");
        
        dm.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        dm.getFeatures().add(IArchimateFactory.eINSTANCE.createFeature());
        
        IDiagramModel copy = (IDiagramModel)dm.getCopy();
        
        assertNotSame(dm, copy);
        assertNotNull(copy.getId());
        assertNotEquals(dm.getId(), copy.getId());
        assertEquals(dm.getName(), copy.getName());
        assertEquals(dm.getDocumentation(), copy.getDocumentation());
        
        assertNotSame(dm.getProperties(), copy.getProperties());
        assertEquals(dm.getProperties().size(), copy.getProperties().size());
        
        assertNotSame(dm.getFeatures(), copy.getFeatures());
        assertEquals(dm.getFeatures().size(), copy.getFeatures().size());

        assertNotSame(dm.getChildren(), copy.getChildren());
    }
}
