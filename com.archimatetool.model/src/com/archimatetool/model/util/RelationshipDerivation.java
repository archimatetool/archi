/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;


/**
 * Represents the derived/direct state of a Relationship
 * 
 * @author Phillip Beauvoir
 */
public enum RelationshipDerivation {
    /** The relationship exists and is derived. */
    DERIVED,
    
    /** The relationship exists and is direct. */
    DIRECT,
    
    /** No relationship mapping is defined for the given source, target, or relation type. */
    INVALID
}
