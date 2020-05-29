/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.Iterator;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IIdentifier;


/**
 * UUID Factory
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class UUIDFactory {
    
    /**
     * The prefix to use for all new UUIDs. This makes it XML friendly for NCNAME and ID types.
     */
    public static final String PREFIX = "id-";
    
    /**
     * Generate a new UUID for an object
     * @param object
     * @return the UUID
     */
    public static String createID(IIdentifier object) {
        // Note - in future we could create an id based on the object type
        return PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate and set a new ID for a given object
     * @param object The object for which to generate a new ID
     */
    public static void generateNewID(IIdentifier object) {
        object.setId(createID(object));
    }

    /**
     * Generate a new ID for the given object and all of its child contents that have identifiers
     * @param object The object for which to generate new IDs and its children, if any
     */
    public static void generateNewIDs(EObject object) {
        if(object instanceof IIdentifier) {
            generateNewID((IIdentifier)object);
        }
        
        for(Iterator<EObject> iter = object.eAllContents(); iter.hasNext();) {
            object = iter.next();
            if(object instanceof IIdentifier) {
                generateNewID((IIdentifier)object);
            }
        }
    }
}