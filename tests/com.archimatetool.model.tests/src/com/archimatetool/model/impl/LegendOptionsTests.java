/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILegendOptions;


@SuppressWarnings("nls")
public class LegendOptionsTests {
    
    @Test
    public void defaultOptions() {
        ILegendOptions options = new LegendOptions();
        
        assertTrue(options.displayElements());
        assertTrue(options.displayRelations());
        assertTrue(options.displaySpecializationElements());
        assertTrue(options.displaySpecializationRelations());
        assertEquals(ILegendOptions.ROWS_PER_COLUMN_DEFAULT, options.getRowsPerColumn());
        assertEquals(0, options.getWidthOffset());
        assertEquals(ILegendOptions.COLORS_DEFAULT, options.getColorScheme());
        assertEquals(ILegendOptions.SORT_DEFAULT, options.getSortMethod());
    }
    
    @Test
    public void defaultOptionsWithNote() {
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        testDefaults(new LegendOptions(note));
        
        note.getFeatures().putString(IDiagramModelNote.FEATURE_LEGEND, "bogus");
        testDefaults(new LegendOptions(note));
    }
    
    private void testDefaults(LegendOptions options) {
        assertTrue(options.displayElements());
        assertTrue(options.displayRelations());
        assertTrue(options.displaySpecializationElements());
        assertTrue(options.displaySpecializationRelations());
        assertEquals(ILegendOptions.ROWS_PER_COLUMN_DEFAULT, options.getRowsPerColumn());
        assertEquals(0, options.getWidthOffset());
        assertEquals(ILegendOptions.COLORS_DEFAULT, options.getColorScheme());
        assertEquals(ILegendOptions.SORT_DEFAULT, options.getSortMethod());
    }
    
    @Test
    public void builder() {
        ILegendOptions options = new LegendOptions()
                .displayElements(false)
                .displayRelations(false)
                .displaySpecializationElements(false)
                .displaySpecializationRelations(false)
                .rowsPerColumn(14)
                .widthOffset(2)
                .colorScheme(ILegendOptions.COLORS_NONE)
                .sortMethod(ILegendOptions.SORT_NAME);
        
        assertFalse(options.displayElements());
        assertFalse(options.displayRelations());
        assertFalse(options.displaySpecializationElements());
        assertFalse(options.displaySpecializationRelations());
        assertEquals(14, options.getRowsPerColumn());
        assertEquals(2, options.getWidthOffset());
        assertEquals(ILegendOptions.COLORS_NONE, options.getColorScheme());
        assertEquals(ILegendOptions.SORT_NAME, options.getSortMethod());
    }

    @Test
    public void bounds() {
        ILegendOptions options = new LegendOptions()
                .rowsPerColumn(ILegendOptions.ROWS_PER_COLUMN_MIN - 1)
                .widthOffset(ILegendOptions.WIDTH_OFFSET_MIN - 1)
                .colorScheme(ILegendOptions.COLORS_NONE - 1)
                .sortMethod(ILegendOptions.SORT_NAME - 1);
        assertEquals(ILegendOptions.ROWS_PER_COLUMN_MIN, options.getRowsPerColumn());
        assertEquals(ILegendOptions.WIDTH_OFFSET_MIN, options.getWidthOffset());
        assertEquals(ILegendOptions.COLORS_NONE, options.getColorScheme());
        assertEquals(ILegendOptions.SORT_NAME, options.getSortMethod());
        
        options = new LegendOptions()
                .rowsPerColumn(ILegendOptions.ROWS_PER_COLUMN_MAX + 1)
                .widthOffset(ILegendOptions.WIDTH_OFFSET_MAX + 1)
                .colorScheme(ILegendOptions.COLORS_USER + 1)
                .sortMethod(ILegendOptions.SORT_CATEGORY + 1);
        assertEquals(ILegendOptions.ROWS_PER_COLUMN_MAX, options.getRowsPerColumn());
        assertEquals(ILegendOptions.WIDTH_OFFSET_MAX, options.getWidthOffset());
        assertEquals(ILegendOptions.COLORS_USER, options.getColorScheme());
        assertEquals(ILegendOptions.SORT_CATEGORY, options.getSortMethod());
    }
    
    @Test
    public void toFeatureString() {
        ILegendOptions options = new LegendOptions();
        assertEquals("display=15,rows=15,offset=0,color=1,sort=1", options.toFeatureString());
        
        options = new LegendOptions()
                .displayElements(false)
                .displayRelations(false)
                .displaySpecializationElements(false)
                .displaySpecializationRelations(false)
                .rowsPerColumn(14)
                .widthOffset(2)
                .colorScheme(ILegendOptions.COLORS_NONE)
                .sortMethod(ILegendOptions.SORT_NAME);
        
        assertEquals("display=0,rows=14,offset=2,color=0,sort=0", options.toFeatureString());
    }
}
