/**
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.compatibility;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Interface to fix backward compatibility issues
 * 
 * @author Phillip Beauvoir
 */
public interface ICompatibilityHandler {
    
    /**
     * Extension ID
     */
    String EXTENSION_ID = "uk.ac.bolton.archimate.compatibility.compatibilityHandler";

    /**
     * Fix the issue
     * @param resource
     * @throws CompatibilityHandlerException
     */
    void fixCompatibility(Resource resource) throws CompatibilityHandlerException;
}