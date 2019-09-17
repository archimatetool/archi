/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * AdapterImpl that sends Notifications to a listener.
 * 
 * @author Phillip Beauvoir
 */
public class LightweightAdapter extends AdapterImpl {
    
    private IModelContentListener listener;
    
    public LightweightAdapter(IModelContentListener listener) {
        this.listener = listener;
    }

    @Override
    public void notifyChanged(Notification notification) {
        if(listener != null) {
            listener.notifyChanged(notification);
        }
    }
}
