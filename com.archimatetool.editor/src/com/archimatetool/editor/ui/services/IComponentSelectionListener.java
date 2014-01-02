/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;



/**
 * Listener Interface for component selections such as menu items, palettes, tools etc.
 * 
 * @author Phillip Beauvoir
 */
public interface IComponentSelectionListener {

    /**
     * @param component The source component that gave rise to the Selection
     * @param selection The actual Selection
     */
    void componentSelectionChanged(Object component, Object selection);
}
