/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;


import org.eclipse.swt.graphics.Image;

/**
 * Implementers of this interface can supply a label and image for an object
 * This can be used for non-eCore objects and can show the label and image in, for example, the Properties View
 * 
 * @author Phillip Beauvoir
 * @deprecated As of 5.7.0 
 *             This was only used downstream by coArchi2 for the Properties View label and image.
 *             This functionality is now in PropertiesLabelProvider using Adapters
 */
public interface IArchiLabelProvider {
    
    /**
     * @return The label or null for the given object
     */
    String getLabel(Object element);
    
    /**
     * @return The image or null for the given object
     */
    Image getImage(Object element);
}
