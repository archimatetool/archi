/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

/**
 * Abstract CommandLineProvider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractCommandLineProvider implements ICommandLineProvider {
    
    public boolean doLog = true;
    
    protected void logMessage(String message) {
        if(doLog) {
            System.out.println(getLogPrefix() + " " + message); //$NON-NLS-1$
        }
    }
    
    protected void logError(String message) {
        if(doLog) {
            System.err.println(getLogPrefix() + " " + message); //$NON-NLS-1$
        }
    }
    
    protected String getLogPrefix() {
        return ""; //$NON-NLS-1$
    }
    
}
