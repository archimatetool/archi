/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

/**
 * IViewpoint
 * 
 * @author Phillip Beauvoir
 */
public interface IViewpoint {
    
    /**
     * @return The internal ID of the viewpoint
     */
    String getID();

    /**
     * @return The name of the Viewpoint
     */
    String getName();

    /**
     * @return true if eClass is an allowed Archimate concept class
     */
    boolean isAllowedConcept(EClass eClass);
    
}