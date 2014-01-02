/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import com.archimatetool.editor.ui.services.UIRequest;



/**
 * Tree Selection Request
 * 
 * @author Phillip Beauvoir
 */
public class TreeSelectionRequest extends UIRequest {
    
    public static final String REQUEST_NAME = "request.selectElements"; //$NON-NLS-1$

    private boolean fReveal;
    private IStructuredSelection fSelection;
    
    /**
     * @param source The source of the request
     * @param selection The selection
     * @param doReveal Whether to reveal the selection
     */
    public TreeSelectionRequest(Object source, IStructuredSelection selection, boolean doReveal) {
        super(source, REQUEST_NAME, selection);
        fSelection = selection;
        fReveal = doReveal;
    }
    
    /**
     * @return The selection
     */
    public IStructuredSelection getSelection() {
        return fSelection;
    }

    /**
     * @return Whether to reveal the selection
     */
    public boolean doReveal() {
        return fReveal;
    }
    
    /**
     * Implementers can over-ride this to determine if the selection should occur
     * @param viewer
     * @return True if the selection should occur
     */
    public boolean shouldSelect(Viewer viewer) {
        return true;
    }
}
