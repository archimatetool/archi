/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

/**
 * IViewpoint
 * 
 * @author Phillip Beauvoir
 */
public interface IViewpoint {
    
    /**
     * @param object
     * @return Whether the object is visible
     */
    boolean isElementVisible(EObject object);

    /**
     * @return Allowed types
     */
    EClass[] getAllowedTypes();
    
    /**
     * @return Allowed class type
     */
    boolean isAllowedType(EClass type);
    
    /**
     * @return The name of the Viewpoint
     */
    String getName();
    
    /**
     * @return The image of the Viewpoint
     */
    Image getImage();
}