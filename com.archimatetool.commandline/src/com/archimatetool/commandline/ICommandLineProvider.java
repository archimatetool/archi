/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;


/**
 * Interface for a Command Line Provider
 * 
 * @author Phillip Beauvoir
 */
public interface ICommandLineProvider {
    
    String EXTENSION_ID = "com.archimatetool.commandline.commandlineProvider"; //$NON-NLS-1$

    void run(String[] args) throws Exception;
    
    
}
