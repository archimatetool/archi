/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILegendOptions;

/**
 * Legend Options
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class LegendOptions implements ILegendOptions {
    
    private static final int DISPLAY_ELEMENTS = 1 << 0;
    private static final int DISPLAY_RELATIONS = 1 << 1;
    private static final int DISPLAY_SPECIALIZATION_ELEMENTS = 1 << 2;
    private static final int DISPLAY_SPECIALIZATION_RELATIONS = 1 << 3;
    private static final int DISPLAY_DEFAULTS = DISPLAY_ELEMENTS | DISPLAY_RELATIONS | DISPLAY_SPECIALIZATION_ELEMENTS | DISPLAY_SPECIALIZATION_RELATIONS;
    
    private final static Pattern displayRegexPattern = Pattern.compile("display=(\\d+)");
    private final static Pattern rowsRegexPattern = Pattern.compile("rows=(\\d+)");
    private final static Pattern offsetRegexPattern = Pattern.compile("offset=(-?\\d+)");
    private final static Pattern colorRegexPattern = Pattern.compile("color=(\\d+)");
    private final static Pattern sortRegexPattern = Pattern.compile("sort=(\\d+)");
    
    private boolean displayElements = true;
    private boolean displayRelations = true;
    private boolean displaySpecializationElements = true;
    private boolean displaySpecializationRelations = true;

    private int rowsPerColumn = ROWS_PER_COLUMN_DEFAULT;
    private int widthOffset = 0;
    private int colorScheme = COLORS_DEFAULT;
    private int sortMethod = SORT_DEFAULT;
    
    public LegendOptions() {
    }

    LegendOptions(IDiagramModelNote note) {
        String feature = note.getFeatures().getString(IDiagramModelNote.FEATURE_LEGEND, null);
        
        if(feature != null) {
            int displayOptions = parseInteger(feature, displayRegexPattern, DISPLAY_DEFAULTS);
            displayElements = (displayOptions & DISPLAY_ELEMENTS) != 0;
            displayRelations = (displayOptions & DISPLAY_RELATIONS) != 0;
            displaySpecializationElements = (displayOptions & DISPLAY_SPECIALIZATION_ELEMENTS) != 0;
            displaySpecializationRelations = (displayOptions & DISPLAY_SPECIALIZATION_RELATIONS) != 0;
            
            rowsPerColumn = parseInteger(feature, rowsRegexPattern, rowsPerColumn);
            widthOffset = parseInteger(feature, offsetRegexPattern, widthOffset);
            colorScheme = parseInteger(feature, colorRegexPattern, colorScheme);
            sortMethod = parseInteger(feature, sortRegexPattern, sortMethod);
        }
    }
    
    @Override
    public ILegendOptions displayElements(boolean val) {
        displayElements = val;
        return this;
    }
    
    @Override
    public boolean displayElements() {
        return displayElements;
    }
    
    @Override
    public ILegendOptions displayRelations(boolean val) {
        displayRelations = val;
        return this;
    }
    
    @Override
    public boolean displayRelations() {
        return displayRelations;
    }
    
    @Override
    public ILegendOptions displaySpecializationElements(boolean val) {
        displaySpecializationElements = val;
        return this;
    }
    
    @Override
    public boolean displaySpecializationElements() {
        return displaySpecializationElements;
    }
    
    @Override
    public ILegendOptions displaySpecializationRelations(boolean val) {
        displaySpecializationRelations = val;
        return this;
    }
    
    @Override
    public boolean displaySpecializationRelations() {
        return displaySpecializationRelations;
    }
    
    @Override
    public ILegendOptions rowsPerColumn(int val) {
        rowsPerColumn = Math.min(Math.max(val, ROWS_PER_COLUMN_MIN), ROWS_PER_COLUMN_MAX);
        return this;
    }
    
    @Override
    public int getRowsPerColumn() {
        return rowsPerColumn;
    }
    
    @Override
    public ILegendOptions widthOffset(int val) {
        widthOffset = Math.min(Math.max(val, WIDTH_OFFSET_MIN), WIDTH_OFFSET_MAX);
        return this;
    }
    
    @Override
    public int getWidthOffset() {
        return widthOffset;
    }
    
    @Override
    public ILegendOptions colorScheme(int val) {
        colorScheme = Math.min(Math.max(val, COLORS_NONE), COLORS_USER);
        return this;
    }
    
    @Override
    public int getColorScheme() {
        return colorScheme;
    }
    
    @Override
    public ILegendOptions sortMethod(int val) {
        sortMethod = Math.min(Math.max(val, SORT_NAME), SORT_CATEGORY);
        return this;
    }
    
    @Override
    public int getSortMethod() {
        return sortMethod;
    }
    
    @Override
    public String toFeatureString() {
        int displayState = displayElements() ? DISPLAY_ELEMENTS : 0;
        displayState |= displayRelations() ? DISPLAY_RELATIONS : 0;
        displayState |= displaySpecializationElements() ? DISPLAY_SPECIALIZATION_ELEMENTS : 0;
        displayState |= displaySpecializationRelations() ? DISPLAY_SPECIALIZATION_RELATIONS : 0;
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("display=");
        sb.append(displayState);
        
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
    
    private int parseInteger(String feature, Pattern pattern, int defaultValue) {
        Matcher matcher = pattern.matcher(feature);
        if(matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            }
            catch(NumberFormatException ex) {
            }
        }
        
        return defaultValue;
    }
}