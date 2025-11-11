/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILegendOptions;

/**
 * Legend Options
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class LegendOptions extends FeatureOptions implements ILegendOptions {
    
    private static final int DISPLAY_ELEMENTS = 1 << 0;
    private static final int DISPLAY_RELATIONS = 1 << 1;
    private static final int DISPLAY_SPECIALIZATION_ELEMENTS = 1 << 2;
    private static final int DISPLAY_SPECIALIZATION_RELATIONS = 1 << 3;
    private static final int DISPLAY_DEFAULTS = DISPLAY_ELEMENTS | DISPLAY_RELATIONS | DISPLAY_SPECIALIZATION_ELEMENTS | DISPLAY_SPECIALIZATION_RELATIONS;
    
    public LegendOptions() {
    }
    
    LegendOptions(IDiagramModelNote note) {
        String featureValue = note.getFeatures().getString(IDiagramModelNote.FEATURE_LEGEND, null);
        if(featureValue != null) {
            int displayOptions = parseInteger(featureValue, "display", DISPLAY_DEFAULTS);
            displayElements((displayOptions & DISPLAY_ELEMENTS) != 0);
            displayRelations((displayOptions & DISPLAY_RELATIONS) != 0);
            displaySpecializationElements((displayOptions & DISPLAY_SPECIALIZATION_ELEMENTS) != 0);
            displaySpecializationRelations((displayOptions & DISPLAY_SPECIALIZATION_RELATIONS) != 0);
            
            rowsPerColumn(parseInteger(featureValue, "rows", ROWS_PER_COLUMN_DEFAULT));
            widthOffset(parseInteger(featureValue, "offset", 0));
            colorScheme(parseInteger(featureValue, "color", COLORS_DEFAULT));
            sortMethod(parseInteger(featureValue, "sort", SORT_DEFAULT));
        }
    }
    
    @Override
    public ILegendOptions displayElements(boolean val) {
        return setValue("elements", val);
    }
    
    @Override
    public boolean displayElements() {
        return getValue("elements", true);
    }
    
    @Override
    public ILegendOptions displayRelations(boolean val) {
        return setValue("relations", val);
    }
    
    @Override
    public boolean displayRelations() {
        return getValue("relations", true);
    }
    
    @Override
    public ILegendOptions displaySpecializationElements(boolean val) {
        return setValue("elements_spec", val);
    }
    
    @Override
    public boolean displaySpecializationElements() {
        return getValue("elements_spec", true);
    }
    
    @Override
    public ILegendOptions displaySpecializationRelations(boolean val) {
        return setValue("relations_spec", val);
    }
    
    @Override
    public boolean displaySpecializationRelations() {
        return getValue("relations_spec", true);
    }
    
    @Override
    public ILegendOptions rowsPerColumn(int val) {
        return setValue("rows", Math.min(Math.max(val, ROWS_PER_COLUMN_MIN), ROWS_PER_COLUMN_MAX));
    }
    
    @Override
    public int getRowsPerColumn() {
        return getValue("rows", ROWS_PER_COLUMN_DEFAULT);
    }
    
    @Override
    public ILegendOptions widthOffset(int val) {
        return setValue("width", Math.min(Math.max(val, WIDTH_OFFSET_MIN), WIDTH_OFFSET_MAX));
    }
    
    @Override
    public int getWidthOffset() {
        return getValue("width", 0);
    }
    
    @Override
    public ILegendOptions colorScheme(int val) {
        return setValue("color", Math.min(Math.max(val, COLORS_NONE), COLORS_USER));
    }
    
    @Override
    public int getColorScheme() {
        return getValue("color", COLORS_DEFAULT);
    }
    
    @Override
    public ILegendOptions sortMethod(int val) {
        return setValue("sort", Math.min(Math.max(val, SORT_NAME), SORT_CATEGORY));
    }
    
    @Override
    public int getSortMethod() {
        return getValue("sort", SORT_DEFAULT);
    }
    
    @Override
    public String toFeatureString() {
        int displayOptions = displayElements() ? DISPLAY_ELEMENTS : 0;
        displayOptions |= displayRelations() ? DISPLAY_RELATIONS : 0;
        displayOptions |= displaySpecializationElements() ? DISPLAY_SPECIALIZATION_ELEMENTS : 0;
        displayOptions |= displaySpecializationRelations() ? DISPLAY_SPECIALIZATION_RELATIONS : 0;
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("display=");
        sb.append(displayOptions);
        
        sb.append(",");
        sb.append("rows=");
        sb.append(getRowsPerColumn());
        
        sb.append(",");
        sb.append("offset=");
        sb.append(getWidthOffset());
        
        sb.append(",");
        sb.append("color=");
        sb.append(getColorScheme());
        
        sb.append(",");
        sb.append("sort=");
        sb.append(getSortMethod());

        return sb.toString();
    }
}