/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * Interface for a Command Line Provider
 * 
 * @author Phillip Beauvoir
 */
public interface ICommandLineProvider {
    
    String EXTENSION_ID = "com.archimatetool.commandline.commandlineProvider"; //$NON-NLS-1$
    
    int PRIORITY_LOAD_OR_CREATE_MODEL = 10;
    int PRIORITY_IMPORT = 20;
    int PRIORITY_RUN_SCRIPT = 30;
    int PRIORITY_REPORT_OR_EXPORT = 40;
    int PRIORITY_SAVE_MODEL = 50;

    /**
     * Run the command
     * @param commandLine options
     * @throws Exception
     */
    default void run(CommandLine commandLine) throws Exception {}
    
    /**
     * @return A list of options that this provider supports or null if none
     */
    default Options getOptions() {
        return null;
    }
    
    /**
     * @return priority of this provider
     * A value of PRIORITY_REPORT_OR_EXPORT is the default
     */
    default int getPriority() {
        return PRIORITY_REPORT_OR_EXPORT;
    }
    
}
