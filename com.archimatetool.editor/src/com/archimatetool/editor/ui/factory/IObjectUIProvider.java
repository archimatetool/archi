/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.emf.ecore.EAttribute;
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
    EditPart createEditPart();
    
    /**
     * @return The default name for this type of object
     */
    String getDefaultName();
    
    /**
     * @return The iconic image to use for this object
     */
    Image getImage();
    
    /**
     * @return The iconic image descriptor to use for this object
     */
    ImageDescriptor getImageDescriptor();
    
    /**
     * @param feature The feature in question
     * @return True if this object should expose a feature in the UI
     * @deprecated Use shouldExposeFeature(String featureName)
     */
    boolean shouldExposeFeature(EAttribute feature);

    /**
     * @param feature The feature in question
     * @return True if this object should expose a feature in the UI
     */
    boolean shouldExposeFeature(String featureName);
}
