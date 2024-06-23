/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;

import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.widgets.Display;


/**
 * UI Request Manager
 * Clients can listen and make requests for UI actions such as tree selections, editing elements
 * 
 * @author Phillip Beauvoir
 */
public final class UIRequestManager {

    public static final UIRequestManager INSTANCE = new UIRequestManager();
    
    private CopyOnWriteArrayList<IUIRequestListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(IUIRequestListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(IUIRequestListener listener) {
        listeners.remove(listener);
    }
    
    public void fireRequestAsync(UIRequest request) {
        Display.getCurrent().asyncExec(() -> {
            fireRequest(request);
        });
    }

    public void fireRequest(UIRequest request) {
        if(request != null) {
            for(IUIRequestListener listener : listeners) {
                listener.requestAction(request);
            }
        }
    }
}
