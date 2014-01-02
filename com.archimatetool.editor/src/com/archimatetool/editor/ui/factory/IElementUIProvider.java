/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 * Interface for Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IElementUIProvider {
    
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
     * @return The default short name for this type of object
     */
    String getDefaultShortName();
    
    /**
     * @return The iconic image to use for this object
     */
    Image getImage();
    
    /**
     * @param instance The instance
     * @return The iconic image to use for a particular instance of this object
     */
    Image getImage(EObject instance);

    /**
     * @return The iconic image descriptor to use for this object
     */
    ImageDescriptor getImageDescriptor();
    
    /**
     * @return The default colour to use for this object (usually a fill color)
     */
    Color getDefaultColor();

    /**
     * @return The default line colour to use for this object
     */
    Color getDefaultLineColor();
}
