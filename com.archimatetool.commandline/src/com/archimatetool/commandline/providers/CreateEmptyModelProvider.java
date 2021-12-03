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
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.UUIDFactory;


/**
 * Create Empty Model
 * 
 * @author Phillip Beauvoir
 */
public class CreateEmptyModelProvider extends AbstractCommandLineProvider {
    
    static final String PREFIX = Messages.CreateEmptyModelProvider_0;
    
    static final String OPTION_CREATE_EMPTY_MODEL = "createEmptyModel"; //$NON-NLS-1$

    public CreateEmptyModelProvider() {
    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        
        // Create Model
        Option option = Option.builder()
                .longOpt(OPTION_CREATE_EMPTY_MODEL)
                .hasArg()
                .optionalArg(true)
                .argName(Messages.CreateEmptyModelProvider_1)
                .desc(Messages.CreateEmptyModelProvider_2)
                .build();
        options.addOption(option);

        return options;
    }
    
    @Override
    public void run(CommandLine commandLine) throws Exception {
        if(commandLine.hasOption(OPTION_CREATE_EMPTY_MODEL)) {
            IArchimateModel model = null;
            
            // Template option?
            String template = commandLine.getOptionValue(OPTION_CREATE_EMPTY_MODEL);
            if(template != null) {
                model = createEmptyModelFromTemplate(template);
            }
            else {
                model = createEmptyModel();
            }
            
            CommandLineState.setModel(model);
        }
    }
    
    // Code taken from com.archimatetool.templates.impl.wizard.NewArchimateModelFromTemplateWizard
    private IArchimateModel createEmptyModelFromTemplate(String template) throws IOException {
        File fileTemplate = new File(template);
        
        if(!fileTemplate.exists()) {
            throw new IOException(NLS.bind(Messages.CreateEmptyModelProvider_3, template));
        }
        
        File tmp = File.createTempFile("~architemplate", null); //$NON-NLS-1$
        tmp.deleteOnExit();

        File file = ZipUtils.extractZipEntry(fileTemplate, "model.archimate", tmp); //$NON-NLS-1$
        IArchimateModel model = IEditorModelManager.INSTANCE.load(file);

        if(model == null) {
            logError(Messages.CreateEmptyModelProvider_4);
            return null;
        }

        // New name
        model.setName(Messages.CreateEmptyModelProvider_5 + " " + model.getName()); //$NON-NLS-1$

        // Set latest model version
        model.setVersion(ModelVersion.VERSION);

        // Set file to null
        model.setFile(null);

        // New IDs
        UUIDFactory.generateNewIDs(model);

        logMessage(NLS.bind(Messages.CreateEmptyModelProvider_6, fileTemplate));

        return model;
    }
    
    private IArchimateModel createEmptyModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        // Add an Archive Manager
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        
        // Add a Command Stack
        CommandStack cmdStack = new CommandStack();
        model.setAdapter(CommandStack.class, cmdStack);
        
        logMessage(Messages.CreateEmptyModelProvider_7);
        
        return model;
    }
    
    @Override
    public int getPriority() {
        return PRIORITY_LOAD_OR_CREATE_MODEL - 2;
    }
    
    @Override
    protected String getLogPrefix() {
        return PREFIX;
    }
}
