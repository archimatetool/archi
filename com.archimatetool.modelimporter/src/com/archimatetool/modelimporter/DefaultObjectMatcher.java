/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IProfile;

/**
 * Default Object Matcher matching on Id or Profile
 * 
 * @author Phillip Beauvoir
 */
public class DefaultObjectMatcher implements IObjectMatcher {
    
    private Map<String, EObject> map = new HashMap<>();
    
    public DefaultObjectMatcher(IArchimateModel targetModel) {
        for(Iterator<EObject> iter = targetModel.eAllContents(); iter.hasNext();) {
            add(iter.next());
        }
    }

    @Override
    public EObject getMatchingObject(EObject eObject) {
        return map.get(getObjectKey(eObject));
    }
    
    @Override
    public void add(EObject newObject) {
        String key = getObjectKey(newObject);
        if(key != null) {
            map.put(key, newObject);
        }
    }
    
    private String getObjectKey(EObject eObject) {
        // Profile uses Concept Type and Name (case-insensitive)
        if(eObject instanceof IProfile) {
            return ((IProfile)eObject).getConceptType() + ((IProfile)eObject).getName().toLowerCase();
        }
        // Else use ID
        else if(eObject instanceof IIdentifier) {
            return ((IIdentifier)eObject).getId();
        }

        return null;
    }
}