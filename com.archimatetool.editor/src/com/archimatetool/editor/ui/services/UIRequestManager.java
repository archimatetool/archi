/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;


/**
 * UI Request Manager
 * Clients can listen and make requests for UI actions such as tree selections, editing elements
 * 
 * @author Phillip Beauvoir
 */
public final class UIRequestManager {

    public static final UIRequestManager INSTANCE = new UIRequestManager();
    
    private List<IUIRequestListener> listeners = new ArrayList<IUIRequestListener>();

    public void addListener(IUIRequestListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(IUIRequestListener listener) {
        listeners.remove(listener);
    }

    public void fireRequest(final UIRequest request) {
        if(request == null) {
            return;
        }
        
        Object[] listenersArray = listeners.toArray();
        for(int i = 0; i < listenersArray.length; i++) {
            final IUIRequestListener l = (IUIRequestListener)listenersArray[i];
            SafeRunner.run(new SafeRunnable() {
                public void run() {
                    l.requestAction(request);
                }
            });
        }
    }
}
