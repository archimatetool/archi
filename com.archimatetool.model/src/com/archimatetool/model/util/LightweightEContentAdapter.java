/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * EContentAdapter that only self-adapts to immediate child objects of given classes
 * 
 * @author Phillip Beauvoir
 */
public class LightweightEContentAdapter extends EContentAdapter {
    
    private Class<?>[] classes;
    
    public LightweightEContentAdapter(Class<? extends EObject> eClass) {
        this.classes = new Class<?>[] { eClass };
    }

    public LightweightEContentAdapter(Class<? extends EObject>[] classes) {
        this.classes = classes;
    }
    
    @Override
    protected void addAdapter(Notifier notifier) {
        // Only interested in these child objects of given class
        // This ensures that we aren't adding an adapter to many child objects
        for(Class<?> c : classes) {
            if(c.isInstance(notifier)) {
                super.addAdapter(notifier);
            }
        }
    };
}
