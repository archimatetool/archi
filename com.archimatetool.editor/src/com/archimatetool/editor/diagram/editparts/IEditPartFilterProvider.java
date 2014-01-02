/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;


/**
 * Provider for EditPart Filtering
 * 
 * @author Phillip Beauvoir
 */
public interface IEditPartFilterProvider {
    /**
     * Add an EditPart Filter
     * @param filter
     */
    void addEditPartFilter(IEditPartFilter filter);

    /**
     * Remove an EditPart Filter
     * @param filter
     */
    void removeEditPartFilter(IEditPartFilter filter);

    /**
     * @return All EditPart Filters or null if no filters
     */
    IEditPartFilter[] getEditPartFilters();
    
    /**
     * @param <T>
     * @param T Type
     * @return EditPart Filters of type T
     */
    <T> T[] getEditPartFilters(Class<T> T);
}