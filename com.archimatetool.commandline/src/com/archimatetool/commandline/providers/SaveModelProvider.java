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
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.ModelVersion;


/**
 * Save Model to File Provider
 * 
 * @author Phillip Beauvoir
 */
public class SaveModelProvider extends AbstractCommandLineProvider {
    
    static final String PREFIX = Messages.SaveModelProvider_0;
    
    static final String OPTION_SAVE_MODEL = "saveModel"; //$NON-NLS-1$

    public SaveModelProvider() {
    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        
        // Load Model
        Option option = Option.builder()
                .longOpt(OPTION_SAVE_MODEL)
                .hasArg()
                .argName(Messages.SaveModelProvider_1)
                .desc(Messages.SaveModelProvider_2)
                .build();
        options.addOption(option);
        
        return options;
    }
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(commandLine.hasOption(OPTION_SAVE_MODEL)) {
            IArchimateModel model = CommandLineState.getModel();
            
            if(model == null) {
                throw new IOException(Messages.SaveModelProvider_3);
            }
            
            String filePath = commandLine.getOptionValue(OPTION_SAVE_MODEL);
            File file = new File(filePath);
            saveModel(model, file);
            logMessage(NLS.bind(Messages.SaveModelProvider_4, file.getPath()));
        }
    }
    
    private void saveModel(IArchimateModel model, File file) throws IOException {
        // Set model version
        model.setVersion(ModelVersion.VERSION);
        
        // File
        model.setFile(file);
        
        // Use Archive Manager to save contents
        IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
        if(archiveManager == null) {
            archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
            model.setAdapter(IArchiveManager.class, archiveManager);
        }
        archiveManager.saveModel();

        // Set CommandStack Save point if we have one
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.markSaveLocation();
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY_SAVE_MODEL;
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
}
