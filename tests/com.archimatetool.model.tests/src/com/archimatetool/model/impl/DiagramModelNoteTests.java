/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILegendOptions;
import com.archimatetool.model.ITextAlignment;


@SuppressWarnings("nls")
public class DiagramModelNoteTests extends DiagramModelObjectTests {
    
    private IDiagramModelNote note;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        return note;
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
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, note.getTextAlignment());
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

    @Test
    public void testGetBorderType() {
        assertEquals(0, note.getBorderType());
        note.setBorderType(1);
        assertEquals(1, note.getBorderType());
    }
    
    @Test
    public void testGetProperties() {
        CommonTests.testProperties(note);
    }

    @Test
    public void testIsLegend() {
        assertFalse(note.isLegend());
        
        note.setIsLegend(true);
        assertTrue(note.isLegend());
        assertNotNull(note.getLegendOptions());
        
        note.setIsLegend(false);
        assertFalse(note.isLegend());
        assertNull(note.getLegendOptions());
    }
    
    @Test
    public void testLegendOptions() {
        assertFalse(note.isLegend());
        
        ILegendOptions options = ILegendOptions.create()
                .displayElements(false)
                .displayRelations(false)
                .displaySpecializationElements(false)
                .displaySpecializationRelations(false)
                .rowsPerColumn(14)
                .widthOffset(2)
                .colorScheme(ILegendOptions.COLORS_NONE)
                .sortMethod(ILegendOptions.SORT_NAME);
        
        note.setLegendOptions(options);
        
        assertTrue(note.isLegend());
        assertNotNull(note.getLegendOptions());
    }
    
}
