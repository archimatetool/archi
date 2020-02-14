/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * EContentAdapter that self-adapts to immediate child objects of given classes.
 * If no classes are provided then it acts like a normal AdapterImpl
 * 
 * @author Phillip Beauvoir
 */
public class LightweightEContentAdapter extends EContentAdapter {
    
    private Set<Class<?>> eClasses = new HashSet<>();
    
    private IModelContentListener listener;
    
    public LightweightEContentAdapter(IModelContentListener listener) {
        this.listener = listener;
    }

    public LightweightEContentAdapter(IModelContentListener listener, Class<?>... eClasses) {
        this.listener = listener;
        this.eClasses.addAll(Arrays.asList(eClasses));
    }
    
    /**
     * Add a class that should be adapted
     * @param eClass
     */
    public void addClass(Class<?> eClass) {
        eClasses.add(eClass);
    }

    @Override
    public void notifyChanged(Notification notification) {
        if(!eClasses.isEmpty()) {
            super.notifyChanged(notification);
        }
        
        if(listener != null && notification.getEventType() != Notification.REMOVING_ADAPTER) {
            listener.notifyChanged(notification);
        }
    }
    
    @Override
    protected void addAdapter(Notifier notifier) {
        // Only interested in these child objects of given class
        // This ensures that we aren't adding an adapter to many child objects
        for(Class<?> c : eClasses) {
            if(c.isInstance(notifier)) {
                super.addAdapter(notifier);
            }
        }
    };
}
