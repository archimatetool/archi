/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.archimatetool.model.IIdentifier;


/**
 * Adapter to allocate and/or store unique IDs for child objects of an Archimate model as they are added or de-serialised.
 *
 * IDs in IIdentifier need to be unique for the model so we need a system to allocate and track them.
 * This adapter will listen for child elements added to the model, both when deserialising from file
 * and when the user creates a child element. The IDs are cached to check for duplicates.
 * 
 * In order for this to work a model object should be added to the main ArchimateModel first *before* any of its
 * child objects are added (would be orphaned otherwise).
 * 
 * IDs are not removed from the cache if an element is deleted in case the user performs an Undo operation.
 * 
 * @author Phillip Beauvoir
 */
public class IDAdapter extends EContentAdapter {

    /**
     * Keep track of unique IDs. An element's ID has to be unique.
     */
    private List<String> fUsedIDs = new ArrayList<String>();

    @Override
    public void notifyChanged(Notification msg) {
        super.notifyChanged(msg);

        if(msg.getEventType() == Notification.ADD) {
            if(msg.getNewValue() instanceof IIdentifier) {
                IIdentifier element = (IIdentifier)msg.getNewValue();
                String id = element.getId();
                // Element has no ID so allocate one
                if(id == null) {
                    element.setId(getNewID());
                }
                // Register the ID to the list when loading in from file
                else {
                    registerID(id);
                }
            }
        }
    }

    public void registerID(String id) {
        if(id != null && !fUsedIDs.contains(id)) {
            fUsedIDs.add(id); 
        }
    }
    
    /**
     * @return A new unique ID to be used for objects in the model
     */
    public String getNewID() {
        String id;
        do {
            id = UUID.randomUUID().toString().split("-")[0]; //$NON-NLS-1$
        }
        while(fUsedIDs.contains(id));
        
        registerID(id); 
        
        return id;
    }
}