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
public abstract class AbstractObjectUIProvider implements IObjectUIProvider, Cloneable {
    
    /**
     * The instance of object for this provider.
     * If this is null then methods act on the EClass.
     */
    private EObject instance;
    
    protected AbstractObjectUIProvider() {
    }
    
    // Don't set this unless you are the ObjectUIFactory or a Unit Test
    // The instance needs to be of the same EClass as returned by providerFor() or a IDiagramModelArchimateComponent that has a concept of that EClass
    AbstractObjectUIProvider setInstance(EObject instance) {
        this.instance = instance;
        return this;
    }
    
    protected EObject getInstance() {
        return instance;
    }
    
    @Override
    protected AbstractObjectUIProvider clone() throws CloneNotSupportedException {
        return (AbstractObjectUIProvider)super.clone();
    }
}
