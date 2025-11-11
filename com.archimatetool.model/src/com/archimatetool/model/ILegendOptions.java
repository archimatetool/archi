/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import com.archimatetool.model.impl.LegendOptions;

/**
 * Legend Options
 * 
 * @author Phillip Beauvoir
 */
public interface ILegendOptions extends IFeatureOptions {

    int ROWS_PER_COLUMN_DEFAULT = 15;
    int ROWS_PER_COLUMN_MIN = 1;
    int ROWS_PER_COLUMN_MAX = 100;

    int WIDTH_OFFSET_MIN = -200;
    int WIDTH_OFFSET_MAX = 200;

    int COLORS_NONE = 0;
    int COLORS_CORE = 1;
    int COLORS_USER = 2;
    int COLORS_DEFAULT = COLORS_CORE;

    int SORT_NAME = 0;
    int SORT_CATEGORY = 1;
    int SORT_DEFAULT = SORT_CATEGORY;
    
    /**
     * Create a new LegendOptions instance
     */
    static ILegendOptions create() {
        return new LegendOptions();
    }

    /**
     * Set whether to display elements in the legend
     */
    ILegendOptions displayElements(boolean val);

    /**
     * @return whether to display elements in the legend
     */
    boolean displayElements();

    /**
     * Set whether to display relations in the legend
     */
    ILegendOptions displayRelations(boolean val);

    /**
     * @return whether to display relations in the legend
     */
    boolean displayRelations();

    /**
     * Set whether to display specialization elements in the legend
     */
    ILegendOptions displaySpecializationElements(boolean val);

    /**
     * @return whether to display specialization elements in the legend
     */
    boolean displaySpecializationElements();

    /**
     * Set whether to display specialization relations in the legend
     */
    ILegendOptions displaySpecializationRelations(boolean val);

    /**
     * @return whether to display specialization relations in the legend
     */
    boolean displaySpecializationRelations();

    /**
     * Set rows per column
     */
    ILegendOptions rowsPerColumn(int val);

    /**
     * @return rows per column
     */
    int getRowsPerColumn();

    /**
     * Set column width offset
     */
    ILegendOptions widthOffset(int val);

    /**
     * @return column width offset
     */
    int getWidthOffset();

    /**
     * Set color scheme
     */
    ILegendOptions colorScheme(int val);

    /**
     * @return color scheme
     */
    int getColorScheme();

    /**
     * Set sort method
     */
    ILegendOptions sortMethod(int val);

    /**
     * @return sort method
     */
    int getSortMethod();

}