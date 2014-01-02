/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IViewPart;


/**
 * Interface for Application View that updates AbstractModelSelectionAction types
 * 
 * @author Phillip Beauvoir
 */
public interface IModelSelectionView extends IViewPart {

    /**
     * @return The Selection Provider of this view
     */
    ISelectionProvider getSelectionProvider();
}