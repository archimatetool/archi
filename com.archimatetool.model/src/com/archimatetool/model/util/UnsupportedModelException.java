/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.io.IOException;

/**
 * Unsupported model
 * 
 * @author Phillip Beauvoir
 */
public class UnsupportedModelException extends IOException {

    public UnsupportedModelException() {
    }

    public UnsupportedModelException(String message) {
        super(message);
    }
    
    public UnsupportedModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
