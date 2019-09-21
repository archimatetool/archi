/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Interface for Object Filter
 * 
 * @author Phillip Beauvoir
 */
public interface IObjectFilter {

    /**
     * Get the required object for this Property Section from the given object
     * For example, object might be an EditPart but we want the underlying model element.
     * @param object
     * @return The required object or null
     */
    Object adaptObject(Object object);

    /**
     * @return True if the feature should be exposed on the object
     * @deprecated Use shouldExposeFeature(EObject eObject, String featureName)
     */
    boolean shouldExposeFeature(EObject eObject, EAttribute feature);

    /**
     * @return True if the feature should be exposed on the object
     */
    boolean shouldExposeFeature(EObject eObject, String featureName);

    /**
     * @param object
     * @return true if object is the required type for this property section
     */
    boolean isRequiredType(Object object);
    
    /**
     * @return The underlying adaptable type for this property section
     * For example, the underlying type for a fill color in an EditPart is a IDiagramModelObject
     */
    Class<?> getAdaptableType();
}