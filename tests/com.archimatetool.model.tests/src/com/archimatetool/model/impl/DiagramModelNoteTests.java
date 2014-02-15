/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IFontAttribute;


@SuppressWarnings("nls")
public class DiagramModelNoteTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelNoteTests.class);
    }
    
    private IDiagramModelNote note;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        return note;
    }

    @Before
    public void runBeforeEachDiagramModelArchimateObjectTest() {
        note = (IDiagramModelNote)getComponent();
    }


    @Test
    public void testGetContent() {
        assertEquals("", note.getContent());
        note.setContent("This is it");
        assertEquals("This is it", note.getContent());
    }

    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(IFontAttribute.TEXT_ALIGNMENT_LEFT, note.getDefaultTextAlignment());
    }
    
    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        note.setContent("hello");
        
        IDiagramModelNote copy = (IDiagramModelNote)note.getCopy();
        
        assertNotSame(note, copy);
        assertEquals(note.getContent(), note.getContent());
    }

}
