/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelNote;
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
    public void testLegendIsNotLegendButHasDefaultValues() {
        assertFalse(note.isLegend());
        int options = note.getLegendDisplayOptions();
        assertEquals(IDiagramModelNote.LEGEND_DISPLAY_DEFAULTS, options);
        assertEquals(IDiagramModelNote.LEGEND_ROWS_DEFAULT, note.getLegendRowsPerColumn());
        assertEquals(IDiagramModelNote.LEGEND_OFFSET_DEFAULT, note.getLegendOffset());
        assertEquals(IDiagramModelNote.LEGEND_COLORS_CORE, note.getLegendColorScheme());
    }
    
    @Test
    public void testLegendDisplayConcepts() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, 14, 20, 0);
        int options = note.getLegendDisplayOptions();
        assertTrue(note.isLegend());
        assertEquals(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, options);
        assertEquals(14, note.getLegendRowsPerColumn());
        assertEquals(20, note.getLegendOffset());
        assertEquals(0, note.getLegendColorScheme());
    }

    @Test
    public void testLegendDisplayElements() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, 23, 12, 0);
        int options = note.getLegendDisplayOptions();
        assertTrue(note.isLegend());
        assertEquals(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, options);
        assertEquals(23, note.getLegendRowsPerColumn());
        assertEquals(12, note.getLegendOffset());
        assertEquals(0, note.getLegendColorScheme());
    }
    
    @Test
    public void testLegendDisplayRelations() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_RELATIONS, 14, -20, 0);
        int options = note.getLegendDisplayOptions();
        assertTrue(note.isLegend());
        assertEquals(IDiagramModelNote.LEGEND_DISPLAY_RELATIONS, options);
        assertEquals(14, note.getLegendRowsPerColumn());
        assertEquals(-20, note.getLegendOffset());
        assertEquals(0, note.getLegendColorScheme());
    }
    
    @Test
    public void testLegendColorOptions() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, 12, 0, IDiagramModelNote.LEGEND_COLORS_CORE);
        assertTrue(note.isLegend());
        assertEquals(IDiagramModelNote.LEGEND_COLORS_CORE, note.getLegendColorScheme());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, 12, 0, IDiagramModelNote.LEGEND_COLORS_USER);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_USER, note.getLegendColorScheme());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, 12, 0, IDiagramModelNote.LEGEND_COLORS_NONE);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_NONE, note.getLegendColorScheme());
    }

    @Test
    public void testLegendCombination() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, 12, 12, IDiagramModelNote.LEGEND_COLORS_CORE);
        int options = note.getLegendDisplayOptions();
        assertTrue((options & IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS) != 0);
        assertTrue((options & IDiagramModelNote.LEGEND_DISPLAY_RELATIONS) != 0);
        assertFalse((options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS) != 0);
        assertFalse((options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_RELATIONS) != 0);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_CORE, note.getLegendColorScheme());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATIONS, 12, 12, IDiagramModelNote.LEGEND_COLORS_USER);
        options = note.getLegendDisplayOptions();
        assertFalse((options & IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS) != 0);
        assertFalse((options & IDiagramModelNote.LEGEND_DISPLAY_RELATIONS) != 0);
        assertTrue((options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS) != 0);
        assertTrue((options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_RELATIONS) != 0);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_USER, note.getLegendColorScheme());
    }
    
    @Test
    public void testLegendRemoved() {
        note.setLegendOptions(null, 0, 0, 0);
        int options = note.getLegendDisplayOptions();
        assertFalse(note.isLegend());
        assertEquals(IDiagramModelNote.LEGEND_DISPLAY_DEFAULTS, options);
        assertEquals(IDiagramModelNote.LEGEND_ROWS_DEFAULT, note.getLegendRowsPerColumn());
        assertEquals(IDiagramModelNote.LEGEND_OFFSET_DEFAULT, note.getLegendOffset());
        assertEquals(IDiagramModelNote.LEGEND_COLORS_NONE, note.getLegendOffset());
    }
    
    @Test
    public void testLegendRowsBounds() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, IDiagramModelNote.LEGEND_ROWS_MIN - 1, IDiagramModelNote.LEGEND_OFFSET_DEFAULT, 0);
        assertEquals(IDiagramModelNote.LEGEND_ROWS_MIN, note.getLegendRowsPerColumn());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, IDiagramModelNote.LEGEND_ROWS_MAX + 1, IDiagramModelNote.LEGEND_OFFSET_DEFAULT, 0);
        assertEquals(IDiagramModelNote.LEGEND_ROWS_MAX, note.getLegendRowsPerColumn());
    }
    
    @Test
    public void testLegendOffsetBounds() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, IDiagramModelNote.LEGEND_ROWS_DEFAULT, IDiagramModelNote.LEGEND_OFFSET_MIN - 1, 0);
        assertEquals(IDiagramModelNote.LEGEND_OFFSET_MIN, note.getLegendOffset());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, IDiagramModelNote.LEGEND_ROWS_DEFAULT, IDiagramModelNote.LEGEND_OFFSET_MAX + 1, 0);
        assertEquals(IDiagramModelNote.LEGEND_OFFSET_MAX, note.getLegendOffset());
    }
    
    @Test
    public void testLegendColorOptionBounds() {
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, 12, 0, IDiagramModelNote.LEGEND_COLORS_NONE - 1);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_NONE, note.getLegendColorScheme());
        
        note.setLegendOptions(IDiagramModelNote.LEGEND_DISPLAY_CONCEPTS, 12, 0, IDiagramModelNote.LEGEND_COLORS_USER + 1);
        assertEquals(IDiagramModelNote.LEGEND_COLORS_USER, note.getLegendColorScheme());
    }

    @Test
    public void testCreateLegendOptionsString() {
        Map<Integer, Boolean> options = new HashMap<>();
        
        options.put(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, null); // Null should be ignored
        
        String state = IDiagramModelNote.createLegendOptionsString(options, 12, 12, IDiagramModelNote.LEGEND_COLORS_CORE);
        int displayVal = 0;
        assertEquals("display=" + displayVal + ",rows=12,offset=12,color=1", state);
        
        options.put(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, true);
        options.put(IDiagramModelNote.LEGEND_DISPLAY_RELATIONS, true);
        options.put(IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS, true);
        options.put(IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_RELATIONS, true);
        
        state = IDiagramModelNote.createLegendOptionsString(options, 10, 10, IDiagramModelNote.LEGEND_COLORS_USER);
        displayVal = IDiagramModelNote.LEGEND_DISPLAY_ALL_CONCEPTS_AND_SPECIALIZATIONS;
        assertEquals("display=" + displayVal + ",rows=10,offset=10,color=2", state);
    }
}
