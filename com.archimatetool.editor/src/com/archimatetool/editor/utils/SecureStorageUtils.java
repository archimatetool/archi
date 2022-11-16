/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.IOException;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.osgi.framework.Bundle;

/**
 * Secure Storage Utilities
 * 
 * @author Phillip Beauvoir
 */
public final class SecureStorageUtils  {
    
    /**
     * @param bundle The bundle
     * @return The ISecurePreferences for bundle
     */
    public static ISecurePreferences getSecurePreferences(Bundle bundle) {
        return getSecurePreferences(bundle.getSymbolicName());
    }
    
    /**
     * @param pathName The pathName
     * @return The ISecurePreferences for pathName
     */
    public static ISecurePreferences getSecurePreferences(String pathName) {
        return SecurePreferencesFactory.getDefault().node(pathName);
    }

    /**
     * Put a value if it is not empty, else remove it
     */
    public static void putOrRemove(ISecurePreferences node, String key, String value, boolean encrypt) throws StorageException, IOException {
        if(StringUtils.isSet(value)) {
            node.put(key, value, encrypt);
        }
        else {
            node.remove(key);
        }
        
        node.flush();
    }
}

