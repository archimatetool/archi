/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ITextAlignment;


@SuppressWarnings("nls")
public class SketchModelStickyTests extends DiagramModelObjectTests {
    
    private ISketchModelSticky sticky;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        sticky = IArchimateFactory.eINSTANCE.createSketchModelSticky();
        sticky.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        return sticky;
    }


    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, sticky.getTextAlignment());
    }

    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        sticky.setContent("hello");
        sticky.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        
        ISketchModelSticky copy = (ISketchModelSticky)sticky.getCopy();
        
        assertNotSame(sticky, copy);
        
        assertEquals(sticky.getContent(), copy.getContent());
        
        assertNotSame(sticky.getChildren(), copy.getChildren());
        assertTrue(copy.getChildren().isEmpty());
        
        assertNotSame(sticky.getProperties(), copy.getProperties());
        assertEquals(sticky.getProperties().size(), copy.getProperties().size());
    }

    @Test
    public void testGetChildren() {
        CommonTests.testList(sticky.getChildren(), IArchimatePackage.eINSTANCE.getDiagramModelGroup());
        CommonTests.testList(sticky.getChildren(), IArchimatePackage.eINSTANCE.getSketchModelActor());
        CommonTests.testList(sticky.getChildren(), IArchimatePackage.eINSTANCE.getSketchModelActor());
    }
    
    @Test
    public void testGetContent() {
        assertEquals("", sticky.getContent());
        sticky.setContent("doc");
        assertEquals("doc", sticky.getContent());
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(sticky);
    }

}
