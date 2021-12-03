/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline.providers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;


/**
 * Load Model From File Provider
 * 
 * @author Phillip Beauvoir
 */
public class LoadModelFromFileProvider extends AbstractCommandLineProvider {
    
    static final String PREFIX = Messages.LoadModelFromFileProvider_0;
    
    static final String OPTION_LOAD_FILE_MODEL = "loadModel"; //$NON-NLS-1$

    public LoadModelFromFileProvider() {
    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        
        // Load Model
        Option option = Option.builder()
                .longOpt(OPTION_LOAD_FILE_MODEL)
                .hasArg()
                .argName(Messages.LoadModelFromFileProvider_1)
                .desc(Messages.LoadModelFromFileProvider_2)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(commandLine.hasOption(OPTION_LOAD_FILE_MODEL)) {
            String filePath = commandLine.getOptionValue(OPTION_LOAD_FILE_MODEL);
            File file = new File(filePath);
            
            IArchimateModel model = IEditorModelManager.INSTANCE.load(file);
            
            if(model == null) {
                throw new IOException(Messages.LoadModelFromFileProvider_3);
            }
            
            CommandLineState.setModel(model);
            
            logMessage(NLS.bind(Messages.LoadModelFromFileProvider_4, model.getName()));
        }
    }
    
    @Override
    public int getPriority() {
        return PRIORITY_LOAD_OR_CREATE_MODEL;
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
}
