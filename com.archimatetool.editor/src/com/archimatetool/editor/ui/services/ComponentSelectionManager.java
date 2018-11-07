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
 * Component Selection Changed Manager - used for Hints View
 * 
 * @author Phillip Beauvoir
 */
public final class ComponentSelectionManager {

    public static final ComponentSelectionManager INSTANCE = new ComponentSelectionManager();
    
    private List<IComponentSelectionListener> listeners = new ArrayList<IComponentSelectionListener>();

    public void addSelectionListener(IComponentSelectionListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeSelectionListener(IComponentSelectionListener listener) {
        listeners.remove(listener);
    }

    public void fireSelectionEvent(final Object source, final Object selection) {
        if(selection == null) {
            return;
        }
        
        Object[] listenersArray = listeners.toArray();
        for(int i = 0; i < listenersArray.length; i++) {
            final IComponentSelectionListener l = (IComponentSelectionListener)listenersArray[i];
            SafeRunner.run(new SafeRunnable() {
                @Override
                public void run() {
                    l.componentSelectionChanged(source, selection);
                }
            });
        }
    }
}
