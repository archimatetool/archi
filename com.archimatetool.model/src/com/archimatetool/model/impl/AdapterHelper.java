/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.HashMap;
import java.util.Map;

import com.archimatetool.model.IAdapter;

/**
 * Helper for managing IAdapter map entries. Avoids repeating the same logic in model classes. 
 * 
 * @author Phillip Beauvoir
 */
class AdapterHelper {

    /**
     * Get a value by its key from the adapterMap.
     * If the value is null in this object, getAdapter(key) is called
     * on the object's eContainer and up the eContainer chain until it's found or returns null if not found.
     * 
     * @param object The caller's IAdapter object
     * @param adapterMap The caller's adapterMap
     * @param key The key to get the value for
     * @return The key value or null
     */
    static Object getValue(IAdapter object, Map<Object, Object> adapterMap, Object key) {
        // No null keys
        if(key == null) {
            return null;
        }
        
        // If adapterMap is not null get value
        Object value = adapterMap != null ? adapterMap.get(key) : null;
        
        // If value is null try eContainer parent if it's an IAdapter
        if(value == null && object.eContainer() instanceof IAdapter adapter) {
            return adapter.getAdapter(key);
        }
        
        return value;
    }

    /**
     * Set a value in the adapterMap
     * If the value is null and the key is present in the adapterMap it is removed
     * 
     * @param adapterMap The caller's adapterMap 
     * @param key Key for the map
     * @param value Value for the map
     * @return The adapterMap or null if the value was null and the adapterMap was already null
     */
    static Map<Object, Object> setValue(Map<Object, Object> adapterMap, Object key, Object value) {
        // Lazily create Map if key and value is not null
        if(adapterMap == null && key != null && value != null) {
            adapterMap = new HashMap<>(4);
        }
        
        // If adapterMap is not null and value is null remove key, else put it
        if(adapterMap != null) {
            adapterMap.compute(key, (k, v) -> value);
        }
        
        return adapterMap;
    }
}
