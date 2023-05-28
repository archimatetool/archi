/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import org.eclipse.emf.ecore.EObject;

/**
 * Interface that defines how an object is uniquely identified
 * 
 * @author Phillip Beauvoir
 */
public interface IObjectMatcher {
    
    /**
     * @param eObject The object to match on
     * @return A matching object in the target model, or null
     */
    EObject getMatchingObject(EObject eObject);

    /**
     * Add a new object to the target model that we will match on
     * @param newObject The object to add
     */
    void add(EObject newObject);
}
