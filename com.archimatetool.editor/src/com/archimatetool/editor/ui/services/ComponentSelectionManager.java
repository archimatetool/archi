/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;

import java.util.concurrent.CopyOnWriteArrayList;




/**
 * Component Selection Changed Manager - used for Hints View
 * 
 * @author Phillip Beauvoir
 */
public final class ComponentSelectionManager {

    public static final ComponentSelectionManager INSTANCE = new ComponentSelectionManager();
    
    private CopyOnWriteArrayList<IComponentSelectionListener> listeners = new CopyOnWriteArrayList<>();

    public void addSelectionListener(IComponentSelectionListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeSelectionListener(IComponentSelectionListener listener) {
        listeners.remove(listener);
    }

    public void fireSelectionEvent(final Object source, final Object selection) {
        if(selection == null) {
            return;
        }
        
        for(IComponentSelectionListener listener : listeners) {
            listener.componentSelectionChanged(source, selection);
        }
    }
}
