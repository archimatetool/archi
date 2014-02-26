/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.swt.graphics.Image;


/**
 * Editor Label Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IEditorLabelProvider {

    String EXTENSIONPOINT = "com.archimatetool.editor.labelProvider"; //$NON-NLS-1$
    
    /**
     * @param element The element
     * @return An image for an element
     */
    Image getImage(Object element);
    
    /**
     * @param element The element
     * @return The label for an object
     */
    String getLabel(Object element);
}
