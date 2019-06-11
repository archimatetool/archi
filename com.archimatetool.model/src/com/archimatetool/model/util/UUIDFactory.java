/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.UUID;

import com.archimatetool.model.IIdentifier;


/**
 * UUID Factory
 * 
 * @author Phillip Beauvoir
 */
public class UUIDFactory {
    
    public static String createID(IIdentifier object) {
        return "id-" + UUID.randomUUID().toString(); //$NON-NLS-1$
    }

}