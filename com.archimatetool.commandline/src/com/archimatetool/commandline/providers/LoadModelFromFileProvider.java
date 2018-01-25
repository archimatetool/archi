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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.commandline.AbstractCommandLineProvider;
import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;


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
    
    public void run(CommandLine commandLine) throws Exception {
        if(commandLine.hasOption(OPTION_LOAD_FILE_MODEL)) {
            String filePath = commandLine.getOptionValue(OPTION_LOAD_FILE_MODEL);
            File file = new File(filePath);
            
            IArchimateModel model = loadModel(file);
            
            if(model == null) {
                throw new IOException(Messages.LoadModelFromFileProvider_3);
            }
            
            CommandLineState.setModel(model);
            
            logMessage(NLS.bind(Messages.LoadModelFromFileProvider_4, model.getName()));
        }
    }
    
    static IArchimateModel loadModel(File file) throws IOException {
        if(file == null || !file.exists()) {
            return null;
        }
        
        // Ascertain if this is an archive file
        boolean useArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(file);

        // Create the Resource
        Resource resource = ArchimateResourceFactory.createNewResource(useArchiveFormat ?
                                                       IArchiveManager.FACTORY.createArchiveModelURI(file) :
                                                       URI.createFileURI(file.getAbsolutePath()));

        // Check model compatibility
        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);
        
        // Load model
        resource.load(null);
        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        
        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }

        model.setFile(file);
        model.setDefaults();
        
        // Add an Archive Manager and load images
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        archiveManager.loadImages();
        
        // Add a Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);

        return model;
    }

    public int getPriority() {
        return PRIORITY_LOAD_OR_CREATE_MODEL;
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
}
