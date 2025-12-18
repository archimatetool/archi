/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;


/**
 * Interface for Core Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IObjectUIProvider {
    
    String EXTENSIONPOINT_ID = "com.archimatetool.editor.objectUIProvider"; //$NON-NLS-1$
    
    /**
     * @return The EClass for which this is an Element UI Provider
     */
    EClass providerFor();

    /**
     * @return A new GEF EditPart for this type of class
     */
    default EditPart createEditPart() {
        return null;
    }
    
    /**
     * @return The default name for this type of object
     */
    default String getDefaultName() {
        return ""; //$NON-NLS-1$
    }
    
    /**
     * @return The iconic image to use for this object
     */
    default Image getImage() {
        return null;
    }
    
    /**
     * @return The iconic image descriptor to use for this object
     */
    default ImageDescriptor getImageDescriptor() {
        return null;
    }
    
    /**
     * @param featureName The feature in question. If null, default is to return false
     * @return True if this object should expose a feature in the UI
     */
    default boolean shouldExposeFeature(String featureName) {
        return featureName != null;
    }
    
    /**
     * @param featureName The feature in question
     * @return a default value for a given feature. Default is null
     */
    default Object getDefaultFeatureValue(String featureName) {
        return null;
    }
    
    /**
     * @param featureName The feature in question
     * @return a value for a given feature. Default is null
     */
    default Object getFeatureValue(String featureName) {
        return null;
    }
}
