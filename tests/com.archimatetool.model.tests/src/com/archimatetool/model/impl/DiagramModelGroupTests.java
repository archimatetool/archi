/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.ITextAlignment;


public class DiagramModelGroupTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelGroupTests.class);
    }
    
    private IDiagramModelGroup group;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        return group;
    }

    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, group.getTextAlignment());
    }

    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        group.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        
        IDiagramModelGroup copy = (IDiagramModelGroup)group.getCopy();
        
        assertNotSame(group, copy);
        
        assertNotSame(group.getChildren(), copy.getChildren());
        assertTrue(copy.getChildren().isEmpty());
        
        assertNotSame(group.getProperties(), copy.getProperties());
        assertEquals(group.getProperties().size(), copy.getProperties().size());
    }

    @Test
    public void testGetChildren() {
        CommonTests.testList(group.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelArchimateObject());
        CommonTests.testList(group.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        CommonTests.testList(group.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelNote());
    }
    
    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(group);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(group);
    }
    
    @Test
    public void testGetBorderType() {
        assertEquals(0, group.getBorderType());
        group.setBorderType(1);
        assertEquals(1, group.getBorderType());
    }
    
}
