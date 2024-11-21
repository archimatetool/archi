/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.emf.ecore.EObject;




/**
 * Abstract Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractObjectUIProvider implements IObjectUIProvider {
    
    /**
     * The instance of object for this provider.
     * If this is null then we are concerned with the class.
     */
    protected EObject instance;
    
    protected AbstractObjectUIProvider() {
    }
    
    // Don't call this unless you are a Unit Test or a Factory
    // The instance needs to be of the same EClass as this is a provider for
    void setInstance(EObject instance) {
        this.instance = instance;
    }
    
}
