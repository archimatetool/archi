/**
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.compatibility;

/**
 * Later Model Version Exception
 * 
 * @author Phillip Beauvoir
 */
public class LaterModelVersionException extends Exception {
    private String version;
    
    public LaterModelVersionException(String version) {
        this.version = version;
    }
    
    public String getVersion() {
        return version;
    }
}