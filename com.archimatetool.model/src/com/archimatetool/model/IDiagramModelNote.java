/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import java.util.Map;
import java.util.Map.Entry;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Note</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.archimatetool.model.IArchimatePackage#getDiagramModelNote()
 * @model extendedMetaData="name='Note'"
 * @generated
 */
@SuppressWarnings("nls")
public interface IDiagramModelNote extends IDiagramModelObject, ITextContent, ITextPosition, IProperties, IBorderType, IIconic {

    int BORDER_DOGEAR = 0; // Default
    int BORDER_RECTANGLE = 1;
    int BORDER_NONE = 2;
    
    String LEGEND_MODEL_NAME = "DiagramModelLegend";
    
    String FEATURE_LEGEND = "legend";
    
    int LEGEND_ROWS_DEFAULT = 15;
    int LEGEND_ROWS_MIN = 1;
    int LEGEND_ROWS_MAX = 100;
    
    int LEGEND_OFFSET_DEFAULT = 0;
    int LEGEND_OFFSET_MIN = -200;
    int LEGEND_OFFSET_MAX = 200;
    
    int LEGEND_DISPLAY_ELEMENTS = 1 << 0;
    int LEGEND_DISPLAY_RELATIONS = 1 << 1;
    int LEGEND_DISPLAY_CONCEPTS = LEGEND_DISPLAY_ELEMENTS + LEGEND_DISPLAY_RELATIONS;
    
    int LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS = 1 << 2;
    int LEGEND_DISPLAY_SPECIALIZATION_RELATIONS = 1 << 3;
    int LEGEND_DISPLAY_SPECIALIZATIONS = LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS | LEGEND_DISPLAY_SPECIALIZATION_RELATIONS;

    int LEGEND_DISPLAY_ALL_CONCEPTS_AND_SPECIALIZATIONS = LEGEND_DISPLAY_CONCEPTS | LEGEND_DISPLAY_SPECIALIZATIONS;
    
    int LEGEND_DISPLAY_DEFAULTS = LEGEND_DISPLAY_ALL_CONCEPTS_AND_SPECIALIZATIONS;
    
    int LEGEND_COLORS_NONE = 0;
    int LEGEND_COLORS_CORE = 1;
    int LEGEND_COLORS_USER = 2;

    /**
     * @return true if this note is used to display a legend
     */
    boolean isLegend();
    
    /**
     * Set legend options in a feature
     * @param displayOptions the display state options
     * @param rows number of rows
     * @param offset margin between rows
     * @param legendColorScheme color scheme
     */
    void setLegendOptions(Integer displayOptions, int rows, int offset, int legendColorScheme);
    
    /**
     * @return legend display state options
     */
    int getLegendDisplayOptions();
    
    /**
     * @return legend rows
     */
    int getLegendRowsPerColumn();

    /**
     * @return legend offset
     */
    int getLegendOffset();
    
    /**
     * @return legend color scheme
     */
    int getLegendColorScheme();

    /**
     * Create a string with legend display options to be saved to model
     */
    static String createLegendOptionsString(int displayState, int rows, int offset, int legendColorScheme) {
        rows = Math.min(Math.max(rows, LEGEND_ROWS_MIN), LEGEND_ROWS_MAX);
        offset = Math.min(Math.max(offset, LEGEND_OFFSET_MIN), LEGEND_OFFSET_MAX);
        legendColorScheme = Math.min(Math.max(legendColorScheme, LEGEND_COLORS_NONE), LEGEND_COLORS_USER);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("display=");
        sb.append(displayState);
        
        sb.append(",");
        sb.append("rows=");
        sb.append(rows);
        
        sb.append(",");
        sb.append("offset=");
        sb.append(offset);
        
        sb.append(",");
        sb.append("color=");
        sb.append(legendColorScheme);

        return sb.toString();
    }
    
    /**
     * Create a string with legend display state using a map of options to be saved to model
     */
    static String createLegendOptionsString(Map<Integer, Boolean> displayOptions, int rows, int offset, int legendColorScheme) {
        int displayState = 0;
        
        if(displayOptions != null) {
            for(Entry<Integer, Boolean> entry : displayOptions.entrySet()) {
                if(Boolean.TRUE.equals(entry.getValue())) {
                    displayState |= entry.getKey();
                }
            }
        }
        
        return createLegendOptionsString(displayState, rows, offset, legendColorScheme);
    }

} // IDiagramModelNote
