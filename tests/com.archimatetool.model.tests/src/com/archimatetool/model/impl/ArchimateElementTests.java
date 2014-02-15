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

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;


@SuppressWarnings("nls")
public abstract class ArchimateElementTests {
    
    protected abstract IArchimateElement getArchimateElement();
    
    protected IArchimateModel model;
    protected IArchimateElement element;
    
    @Before
    public void runBeforeEachArchimateElementTest() {
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        element = getArchimateElement();
    }

    
    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(element);
    }
        
    @Test
    public void testGetArchimateModel() {
        assertNull(element.getArchimateModel());
        
        model.getDefaultFolderForElement(element).getElements().add(element);
        assertSame(model, element.getArchimateModel());
    }
    
    @Test
    public void testGetCopy() {
        element.setName("name");
        element.setDocumentation("doc");
        element.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        
        IArchimateElement copy = (IArchimateElement)element.getCopy();
        
        assertNotSame(element, copy);
        assertNull(copy.getId());
        assertEquals(element.getName(), copy.getName());
        assertEquals(element.getDocumentation(), copy.getDocumentation());
        assertNotSame(element.getProperties(), copy.getProperties());
        assertEquals(element.getProperties().size(), copy.getProperties().size());
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(element);
    }

    @Test
    public void testGetID() {
        assertNull(element.getId());
        
        model.getDefaultFolderForElement(element).getElements().add(element);
        assertNotNull(element.getId());
    }
        
    @Test
    public void testGetName() {
        CommonTests.testGetName(element);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(element);
    }
 
}
