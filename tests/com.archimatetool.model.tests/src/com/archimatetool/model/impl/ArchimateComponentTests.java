/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;


@SuppressWarnings("nls")
public abstract class ArchimateComponentTests {
    
    protected abstract IArchimateComponent getArchimateComponent();
    
    protected IArchimateModel model;
    protected IArchimateComponent component;
    
    @Before
    public void runBeforeEachArchimateComponentTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        component = getArchimateComponent();
    }

    
    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(component);
    }
        
    @Test
    public void testGetArchimateModel() {
        assertNull(component.getArchimateModel());
        
        model.getDefaultFolderForElement(component).getElements().add(component);
        assertSame(model, component.getArchimateModel());
    }
    
    @Test
    public void testGetCopy() {
        component.setName("name");
        component.setDocumentation("doc");
        component.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        
        IArchimateComponent copy = (IArchimateComponent)component.getCopy();
        
        assertNotSame(component, copy);
        assertNull(copy.getId());
        assertEquals(component.getName(), copy.getName());
        assertEquals(component.getDocumentation(), copy.getDocumentation());
        assertNotSame(component.getProperties(), copy.getProperties());
        assertEquals(component.getProperties().size(), copy.getProperties().size());
        assertNotSame(component.getSourceRelationships(), copy.getSourceRelationships());
        assertNotSame(component.getTargetRelationships(), copy.getTargetRelationships());
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(component);
    }

    @Test
    public void testGetID() {
        assertNull(component.getId());
        
        model.getDefaultFolderForElement(component).getElements().add(component);
        assertNotNull(component.getId());
    }
        
    @Test
    public void testGetName() {
        CommonTests.testGetName(component);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(component);
    }
    
    @Test
    public void testGetSourceRelationships() {
        assertEquals(0, component.getSourceRelationships().size());
        component.getSourceRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, component.getSourceRelationships().size());
    }
    
    @Test
    public void testGetTargetRelationships() {
        assertEquals(0, component.getTargetRelationships().size());
        component.getTargetRelationships().add(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        assertEquals(1, component.getTargetRelationships().size());
    }
}
