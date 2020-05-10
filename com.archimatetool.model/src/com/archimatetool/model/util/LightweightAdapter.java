/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * Convenience AdapterImpl that sends Notifications to a IModelContentListener
 * and takes care of adding and removing itself from EObjects' eAdapater lists.
 * 
 * @author Phillip Beauvoir
 */
public class LightweightAdapter extends AdapterImpl {
    
    private IModelContentListener listener;
    
    public LightweightAdapter(IModelContentListener listener) {
        this.listener = listener;
    }
    
    /**
     * This AdapterImpl will be added to aach EObject's eAdapters
     * @param eObjects
     */
    public void add(EObject... eObjects) {
        for(EObject eObject : eObjects) {
            if(eObject != null) {
                eObject.eAdapters().add(this);
            }
        }
    }
    
    /**
     * This AdapterImpl will be removed from aach EObject's eAdapters
     * @param eObjects
     */
    public void remove(EObject... eObjects) {
        for(EObject eObject : eObjects) {
            if(eObject != null) {
                eObject.eAdapters().remove(this);
            }
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        if(listener != null) {
            listener.notifyChanged(notification);
        }
    }
}
