/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;



/**
 * Listener Interface for UI Requests
 * 
 * @author Phillip Beauvoir
 */
public interface IUIRequestListener {

    /**
     * Request an Action
     * @param request
     */
    void requestAction(UIRequest request);
}
