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
public class UUIDFactory {
    
    public static String createID(IIdentifier object) {
        // Note - in future we could create an id based in the object
        return UUID.randomUUID().toString();
    }

    /**
     * Generate and set a new ID for a given object
     * @param eObject
     */
    public static void generateNewID(IIdentifier object) {
        object.setId(createID(object));
    }

    /**
     * Generate new IDs for the given objects and all of its child contents that have identifiers
     * @param eObject The eObject
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