/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IViewPart;


/**
 * Interface for Model View
 * 
 * @author Phillip Beauvoir
 */
public interface IModelView extends IViewPart {

    /**
     * @return The Viewer
     */
    StructuredViewer getViewer();
}