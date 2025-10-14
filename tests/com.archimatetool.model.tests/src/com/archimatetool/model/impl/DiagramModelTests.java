/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;


@SuppressWarnings("nls")
public abstract class DiagramModelTests {
    
    private IArchimateModel model;
    protected IDiagramModel dm;
    
    @BeforeEach
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
        CommonTests.testGetID(dm);
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
        
        // Test we can access an adapter value in the parent chain
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        IFolder folder = model.getDefaultFolderForObject(dm);
        folder.getElements().add(dm);
        
        model.setAdapter("key1", "value1");
        folder.setAdapter("key2", "value2");
        
        assertEquals("value1", dm.getAdapter("key1"));
        assertEquals("value2", dm.getAdapter("key2"));
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
