/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility.handlers;

import static org.junit.Assert.assertEquals;

import org.eclipse.draw2d.geometry.Dimension;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;


public class FixDefaultSizesHandlerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FixDefaultSizesHandlerTests.class);
    }
    
    private static FixDefaultSizesHandler handler;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        handler = new FixDefaultSizesHandler();
        
        TestUtils.ensureDefaultDisplay();
    }
    
    @Test
    public void testGetNewSize_Group() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group.setBounds(0, 0, -1, -1);
        
        IDiagramModelObject note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setBounds(300, 114, -1, -1);
        group.getChildren().add(note);
        
        assertEquals(new Dimension(495, 204), handler.getNewSize(group));
    }   
    
    @Test
    public void testGetNewSize_ArchiMateElement() {
        IDiagramModelArchimateObject parent = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        parent.setBounds(0, 0, -1, -1);
        
        IDiagramModelObject child = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        child.setBounds(300, 120, -1, -1);
        parent.getChildren().add(child);
        
        assertEquals(new Dimension(430, 185), handler.getNewSize(parent));
    }   

    @Test
    public void testGetDefaultSize() {
        IDiagramModelObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        dmo.setBounds(0, 0, -1, -1);
        assertEquals(new Dimension(185, 80), handler.getDefaultSize(dmo));
        
        dmo.setBounds(0, 0, 23, 23);
        assertEquals(new Dimension(23, 23), handler.getDefaultSize(dmo));
    }

    @Test
    public void testGetDefaultSize_ArchimateElement() {
        IDiagramModelArchimateObject dmo = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        dmo.setBounds(0, 0, -1, -1);
        assertEquals(new Dimension(120, 55), handler.getDefaultSize(dmo));
        
        dmo.setBounds(0, 0, 23, 23);
        assertEquals(new Dimension(23, 23), handler.getDefaultSize(dmo));
    }
}
