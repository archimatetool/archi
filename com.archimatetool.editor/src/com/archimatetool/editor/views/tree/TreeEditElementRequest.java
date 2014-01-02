/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import com.archimatetool.editor.ui.services.UIRequest;


/**
 * Tree Edit Name in-place command
 * 
 * @author Phillip Beauvoir
 */
public class TreeEditElementRequest extends UIRequest {
    
    public static final String REQUEST_NAME = "request.editElement"; //$NON-NLS-1$

    /**
     * @param source The source of the request
     * @param selection The selection
     * @param doReveal Whether to reveal the selection
     */
    public TreeEditElementRequest(Object source, Object element) {
        super(source, REQUEST_NAME, element);
    }
}
